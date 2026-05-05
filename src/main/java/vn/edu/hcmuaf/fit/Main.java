package vn.edu.hcmuaf.fit;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Main class để chạy ứng dụng với Embedded Tomcat
 * Dùng cho deployment trên Railway, Heroku, hoặc chạy standalone
 */
public class Main {
    public static void main(String[] args) throws LifecycleException {
        // Lấy port từ environment variable (Railway, Heroku) hoặc dùng 8080 mặc định
        String portEnv = System.getenv("PORT");
        int port = portEnv != null ? Integer.parseInt(portEnv) : 8080;

        // Khởi tạo Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // Trigger connector creation

        // Cấu hình webapp context
        String webappDirLocation = "src/main/webapp/";
        File webappDir = new File(webappDirLocation);
        
        Context context;
        if (webappDir.exists()) {
            // Development mode
            System.out.println("========================================");
            System.out.println("Running in DEVELOPMENT mode");
            System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
            System.out.println("========================================");
            context = tomcat.addWebapp("", webappDir.getAbsolutePath());
        } else {
            // Production mode - extract webapp từ JAR
            System.out.println("========================================");
            System.out.println("Running in PRODUCTION mode");
            System.out.println("Extracting webapp from JAR...");
            try {
                File tempWebapp = extractWebappFromJar();
                System.out.println("Webapp extracted to: " + tempWebapp.getAbsolutePath());
                System.out.println("========================================");
                context = tomcat.addWebapp("", tempWebapp.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to extract webapp from JAR!");
                e.printStackTrace();
                throw new RuntimeException("Cannot start application", e);
            }
        }
        
        // Enable JSP support
        context.setParentClassLoader(Main.class.getClassLoader());

        System.out.println("Starting Tomcat on port: " + port);
        System.out.println("========================================");

        // Start server
        tomcat.start();
        tomcat.getServer().await();
    }
    
    /**
     * Extract webapp resources từ JAR ra temp directory
     */
    private static File extractWebappFromJar() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "webapp-" + System.currentTimeMillis());
        tempDir.mkdirs();
        
        // Lấy đường dẫn JAR file hiện tại
        String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        
        if (jarPath.endsWith(".jar")) {
            try (JarFile jarFile = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    
                    // Chỉ extract các file trong thư mục webapp/
                    if (name.startsWith("webapp/")) {
                        String relativePath = name.substring("webapp/".length());
                        if (relativePath.isEmpty()) continue;
                        
                        File destFile = new File(tempDir, relativePath);
                        
                        if (entry.isDirectory()) {
                            destFile.mkdirs();
                        } else {
                            destFile.getParentFile().mkdirs();
                            try (InputStream is = jarFile.getInputStream(entry)) {
                                Files.copy(is, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    }
                }
            }
        }
        
        return tempDir;
    }
}
