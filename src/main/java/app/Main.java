package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(loader.load(), 520, 640);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("Intramural Soccer • Log In");
        stage.setScene(scene);
        stage.show();

        // Ensure DB schema exists
        Database.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
