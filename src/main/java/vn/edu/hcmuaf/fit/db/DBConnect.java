package vn.edu.hcmuaf.fit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    
    public static Connection get() {
        // Ưu tiên đọc biến môi trường từ Railway MySQL (có hoặc không có dấu gạch dưới)
        String host = getEnv("MYSQL_HOST", getEnv("MYSQLHOST", "localhost"));
        String port = getEnv("MYSQL_PORT", getEnv("MYSQLPORT", "3306"));
        String dbName = getEnv("MYSQL_DATABASE", getEnv("MYSQLDATABASE", "dataweb"));
        
        // Nếu có custom DATABASE_URL thì dùng nó, nếu không thì tự build từ host, port
        String url = getEnv("DATABASE_URL", null);
        if (url == null) {
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=UTF-8";
        }
        
        String user = getEnv("MYSQL_USER", getEnv("MYSQLUSER", getEnv("DB_USER", "root")));
        String pass = getEnv("MYSQL_PASSWORD", getEnv("MYSQLPASSWORD", getEnv("DB_PASSWORD", "12345")));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("LOG: Database Connection Failed!");
            System.err.println("LOG: URL used: " + url);
            e.printStackTrace();
            return null;
        }
    }
    
    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }

    public static void main(String[] args) {
        Connection conn = get();
        if (conn != null) {
            System.out.println("Kết nối thành công!");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Kết nối thất bại!");
        }
    }
}
