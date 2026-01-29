package database;

public class DatabaseConfig {

    // Configurações do MySQL
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "3306";
    public static final String DB_NAME = "csmoveis";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "Xandy120803@";

    // URL completa de conexão
    public static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
            "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";

    // Driver
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
}
