package pe.edu.pucp.dbManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static DBManager instance;
    private final Properties properties;
    private final String url;
    private final String user;
    private final String password;
    private final String DB_CREDENTIALS_FILE = "db.properties";

    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("No se encontro el driver MySQL en el classpath", ex);
        }

        properties = new Properties();
        try (InputStream inputStream = abrirArchivoCredenciales()) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo leer " + DB_CREDENTIALS_FILE, ex);
        }

        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String database = properties.getProperty("database");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?sslMode=DISABLED&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    private InputStream abrirArchivoCredenciales() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = contextClassLoader != null
                ? contextClassLoader.getResourceAsStream(DB_CREDENTIALS_FILE)
                : null;

        if (inputStream == null) {
            inputStream = DBManager.class.getClassLoader().getResourceAsStream(DB_CREDENTIALS_FILE);
        }

        if (inputStream == null) {
            throw new RuntimeException(DB_CREDENTIALS_FILE
                    + " no fue encontrado en el classpath. Verifica que exista en KillaREST/src/main/resources.");
        }

        return inputStream;
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
