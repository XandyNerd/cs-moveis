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
            // carregar tela principal
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/Principal.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("CS Móveis - Sistema");
            stage.setScene(scene);
            stage.show();

            // fechar tela de login
            Stage loginStage = (Stage) txtUsuario.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}