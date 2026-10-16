package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final Properties LOCAL_PROPERTIES = loadLocalProperties();

    private static final String URL = getConfig("DB_URL", "db.url");
    private static final String USER = getConfig("DB_USER", "db.user");
    private static final String PASSWORD = getConfig("DB_PASSWORD", "db.password");

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getInstance() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static Properties loadLocalProperties() {
        Properties properties = new Properties();
        try (InputStream input = DBUtil.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        return properties;
    }

    private static String getConfig(String environmentName, String propertyName) {
        String value = System.getenv(environmentName);
        if (value == null || value.isBlank()) {
            value = LOCAL_PROPERTIES.getProperty(propertyName);
        }
        if (value == null || value.isBlank()) {
            throw new ExceptionInInitializerError(
                    environmentName + " or " + propertyName + " is required."
            );
        }
        return value.trim();
    }
}
