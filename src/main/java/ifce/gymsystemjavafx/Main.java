package ifce.gymsystemjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o arquivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/ifce/gymsystemjavafx/LoginSceen.fxml"));

        // Cria a cena com o layout FXML
        Scene scene = new Scene(root);

        // Define o título da aplicação
        primaryStage.setTitle("Gym Login System");

        // Configura a janela para ser maximizada (ocupa toda a tela, sem barra de título)
        primaryStage.setMaximized(true);

        // Configura a cena na janela
        primaryStage.setScene(scene);

        // Exibe a janela
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
