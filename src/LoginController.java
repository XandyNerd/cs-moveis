import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class LoginController {
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Label lblErro;

    @FXML
    public void entrar() {
        try {
            // Carrega a tela principal
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Principal.fxml"));

            Scene scene = new Scene(loader.load());

            // Reutiliza o Stage atual (janela de login)
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle("CS Móveis - Sistema");
            stage.setScene(scene);

            // Define tamanho mínimo
            stage.setMinWidth(1024);
            stage.setMinHeight(768);

            // Maximiza a janela
            stage.setMaximized(true);

            // Mantém a janela visível (já está aberta)
            // stage.show(); // Não precisa, já está visível

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao abrir tela principal: " + e.getMessage());
        }
    }
}