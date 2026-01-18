package com.deadlinereminder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String PROPERTIES_FILE = "database.properties";

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/deadlineDB?serverTimezone=UTC";
        String user = "root";
        String pass = "password";

        try (java.io.InputStream input = new java.io.FileInputStream(PROPERTIES_FILE)) {
            java.util.Properties prop = new java.util.Properties();
            prop.load(input);

            url = prop.getProperty("db.url", url);
            user = prop.getProperty("db.user", user);
            pass = prop.getProperty("db.password", pass);
        } catch (java.io.IOException ex) {
            System.out.println("Could not load " + PROPERTIES_FILE + " file provided, using defaults.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found. Make sure the connector jar is on the classpath.");
            // We can't do much without the driver, but we'll let DriverManager try or fail
        }
        
        return DriverManager.getConnection(url, user, pass);
    }
}
