package vn.edu.hcmuaf.fit;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

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
        Context context = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        
        // Enable JSP support
        context.setParentClassLoader(Main.class.getClassLoader());

        System.out.println("========================================");
        System.out.println("Starting Tomcat on port: " + port);
        System.out.println("Webapp directory: " + webappDirLocation);
        System.out.println("========================================");

        // Start server
        tomcat.start();
        tomcat.getServer().await();
    }
}
