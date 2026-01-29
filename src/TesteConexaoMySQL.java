import database.DatabaseManager;
import java.sql.Connection;

/**
 * Teste simples para verificar conexão com MySQL
 */
public class TesteConexaoMySQL {

    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("🔄 TESTANDO CONEXÃO COM MYSQL");
        System.out.println("========================================\n");

        try {
            // Tenta conectar
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ CONEXÃO ESTABELECIDA COM SUCESSO!");
                System.out.println("   📊 Database: csmoveis");
                System.out.println("   🖥️  Host: localhost:3306");
                System.out.println("   👤 User: root");
                System.out.println("\n🎉 MySQL está funcionando!");

            } else {
                System.out.println("❌ Falha ao conectar!");
                System.out.println("   A conexão retornou null ou está fechada.");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("\n❌ DRIVER MYSQL NÃO ENCONTRADO!");
            System.err.println("   Erro: " + e.getMessage());
            System.err.println("\n💡 SOLUÇÃO:");
            System.err.println("   1. Baixe: mysql-connector-j-8.3.0.jar");
            System.err.println("   2. Coloque em: lib/mysql-connector-j-8.3.0.jar");
            System.err.println("   3. Recarregue o VS Code");

        } catch (Exception e) {
            System.err.println("\n❌ ERRO DE CONEXÃO!");
            System.err.println("   " + e.getMessage());
            System.err.println("\n💡 VERIFIQUE:");
            System.err.println("   • MySQL está rodando?");
            System.err.println("   • Senha está correta? (Xandy120803@)");
            System.err.println("   • Banco 'csmoveis' existe?");

            e.printStackTrace();
        }

        System.out.println("\n========================================\n");
    }
}
