package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView logoImage;

    // Nome de usuário e senha hardcoded para autenticação
    private final String correctUsername = "admin";
    private final String correctPassword = "1234";

    public void initialize() {
        // Configurações de inicialização, se necessário
    }

    @FXML
    public void handleLogin() {
        // Obter os valores inseridos pelo usuário
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Autenticação hardcoded
        if (username.equals(correctUsername) && password.equals(correctPassword)) {
            // Autenticação bem-sucedida, navega para outra página
            loadDashboard();
        } else {
            // Autenticação falhou
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    // Método para exibir alertas
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para carregar o dashboard (ou outra página)
    private void loadDashboard() {
        try {
            // Carrega o novo layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/Tabelas.fxml"));
            Parent dashboardRoot = loader.load();

            // Obtém o stage atual a partir de qualquer componente (por exemplo, usernameField)
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Define a nova cena (Dashboard) no stage
            stage.setScene(new Scene(dashboardRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the dashboard.");
        }
    }
}
