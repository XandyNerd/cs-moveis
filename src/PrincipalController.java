import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void abrirClientes() {
        carregarTela("/fxml/cliente.fxml");
    }

    @FXML
    public void abrirProdutos() {
        System.out.println("Produtos");
    }

    @FXML
    public void abrirVendas() {
        System.out.println("Vendas");
    }

    @FXML
    public void sair() {
        System.exit(0);
    }

    private void carregarTela(String fxml) {
        try {
            System.out.println("\n🔄 Carregando tela: " + fxml);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // Limpa o conteúdo anterior
            contentPane.getChildren().clear();

            // Adiciona a nova tela
            contentPane.getChildren().add(root);

            // Configura as âncoras para preencher todo o espaço
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

            System.out.println("✅ Tela carregada com sucesso: " + fxml);

        } catch (IOException e) {
            System.err.println("❌ ERRO ao carregar FXML: " + fxml);
            System.err.println("   Detalhes do erro:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ ERRO INESPERADO ao carregar tela: " + fxml);
            e.printStackTrace();
        }
    }
}
