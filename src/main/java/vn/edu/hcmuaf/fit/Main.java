package vn.edu.hcmuaf.fit;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class để chạy ứng dụng với Embedded Tomcat
 * Dùng cho deployment trên Railway, Heroku, hoặc chạy standalone
 */
public class Main {
    public static void main(String[] args) throws LifecycleException, IOException {
        // Giảm thiểu log của Tomcat để tránh limit log/sec trên Railway
        Logger.getLogger("org.apache").setLevel(Level.WARNING);
        Logger.getLogger("org.apache.jasper").setLevel(Level.SEVERE);
        // Lấy port từ environment variable (Railway, Heroku) hoặc dùng 8080 mặc định
        String portEnv = System.getenv("PORT");
        int port = portEnv != null ? Integer.parseInt(portEnv) : 8080;

        // Khởi tạo Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.getConnector(); // Trigger connector creation

        System.out.println("========================================");
        System.out.println("Starting Tomcat on port: " + port);
        System.out.println("========================================");

        // Xác định webapp directory
        File webappDir = getWebappDirectory();
        
        System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
        System.out.println("Webapp exists: " + webappDir.exists());
        
        if (webappDir.exists()) {
            System.out.println("Webapp files:");
            String[] files = webappDir.list();
            if (files != null) {
                for (int i = 0; i < Math.min(files.length, 10); i++) {
                    System.out.println("  - " + files[i]);
                }
            }
        }

        // Cấu hình webapp context
        Context context = tomcat.addWebapp("", webappDir.getAbsolutePath());
        
        // Vô hiệu hóa scan các file/dir không cần thiết để tăng tốc độ và tránh lỗi UnixException
        StandardJarScanner jarScanner = (StandardJarScanner) context.getJarScanner();
        jarScanner.setScanClassPath(true); // Cần thiết để scan TLD trong fat jar
        jarScanner.setScanManifest(false);
        jarScanner.setScanAllFiles(false);
        jarScanner.setScanAllDirectories(false);
        
        // Cấu hình bộ lọc chỉ scan TLD trong fat jar của chúng ta, bỏ qua các jar hệ thống
        org.apache.tomcat.util.scan.StandardJarScanFilter filter = new org.apache.tomcat.util.scan.StandardJarScanFilter();
        filter.setDefaultTldScan(false);
        // Dockerfile trên Railway đổi tên thành app.jar, trên local là webapp-*.jar
        filter.setTldScan("webapp-*.jar,app.jar");
        jarScanner.setJarScanFilter(filter);
        
        // Enable JSP support
        context.setParentClassLoader(Main.class.getClassLoader());
        
        System.out.println("Context path: " + context.getPath());
        System.out.println("========================================");

        // Start server
        tomcat.start();
        tomcat.getServer().await();
    }
    
    /**
     * Lấy webapp directory - tự động detect development vs production
     */
    private static File getWebappDirectory() throws IOException {
        // Try development path first
        File devWebapp = new File("src/main/webapp");
        if (devWebapp.exists() && devWebapp.isDirectory()) {
            System.out.println("Running in DEVELOPMENT mode");
            return devWebapp;
        }
        
        // Production mode - extract from JAR
        System.out.println("Running in PRODUCTION mode");
        System.out.println("Extracting webapp from JAR...");
        
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "webapp-" + System.currentTimeMillis());
        tempDir.mkdirs();
        
        // Get JAR file path
        URL location = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = location.getPath();
        
        System.out.println("JAR location: " + jarPath);
        
        if (jarPath.endsWith(".jar")) {
            extractWebappFromJar(new File(jarPath), tempDir);
        } else {
            // Running from IDE or exploded directory
            System.out.println("Not running from JAR, checking classpath...");
            extractWebappFromClasspath(tempDir);
        }
        
        return tempDir;
    }
    
    /**
     * Extract webapp từ JAR file
     */
    private static void extractWebappFromJar(File jarFile, File destDir) throws IOException {
        int count = 0;
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                
                if (name.startsWith("webapp/")) {
                    String relativePath = name.substring("webapp/".length());
                    if (relativePath.isEmpty()) continue;
                    
                    File destFile = new File(destDir, relativePath);
                    
                    if (entry.isDirectory()) {
                        destFile.mkdirs();
                    } else {
                        destFile.getParentFile().mkdirs();
                        try (InputStream is = jar.getInputStream(entry)) {
                            Files.copy(is, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            count++;
                        }
                    }
                }
            }
        }
        
        System.out.println("Extracted " + count + " files from JAR");
        
        // Verify
        File indexJsp = new File(destDir, "index.jsp");
        File webXml = new File(destDir, "WEB-INF/web.xml");
        System.out.println("index.jsp exists: " + indexJsp.exists());
        System.out.println("web.xml exists: " + webXml.exists());
    }
    
    /**
     * Extract webapp từ classpath (fallback)
     */
    private static void extractWebappFromClasspath(File destDir) throws IOException {
        // Try to copy from classpath resources
        ClassLoader classLoader = Main.class.getClassLoader();
        URL webappUrl = classLoader.getResource("webapp");
        
        if (webappUrl != null) {
            System.out.println("Found webapp in classpath: " + webappUrl);
            // Copy resources...
        } else {
            System.err.println("WARNING: webapp not found in classpath!");
        }
    }
}
