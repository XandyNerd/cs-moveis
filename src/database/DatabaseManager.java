package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerenciador de conexão com MySQL (Singleton)
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Construtor privado (Singleton)
     */
    private DatabaseManager() {
        conectar();
    }

    /**
     * Obtém a instância única do DatabaseManager
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Conecta ao banco de dados MySQL
     */
    private void conectar() {
        try {
            // Carrega o driver MySQL
            Class.forName(DatabaseConfig.DB_DRIVER);

            // Conecta ao banco
            connection = DriverManager.getConnection(
                    DatabaseConfig.DB_URL,
                    DatabaseConfig.DB_USER,
                    DatabaseConfig.DB_PASSWORD);

            System.out.println("✅ Conectado ao MySQL com sucesso!");
            System.out.println("📊 Banco: " + DatabaseConfig.DB_NAME);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL não encontrado!");
            System.err.println("   Certifique-se de que mysql-connector-java está no classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao MySQL!");
            System.err.println("   Host: " + DatabaseConfig.DB_HOST);
            System.err.println("   Porta: " + DatabaseConfig.DB_PORT);
            System.err.println("   Banco: " + DatabaseConfig.DB_NAME);
            System.err.println("   Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtém a conexão ativa
     */
    public Connection getConnection() {
        try {
            // Reconecta se a conexão foi fechada
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠️ Reconectando ao banco...");
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar conexão");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Fecha a conexão
     */
    public void fechar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexão com MySQL fechada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao fechar conexão");
            e.printStackTrace();
        }
    }

    /**
     * Testa se a conexão está ativa
     */
    public boolean testarConexao() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
