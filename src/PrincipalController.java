import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class PrincipalController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void abrirClientes() {
        System.out.println("Clientes");
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
}
