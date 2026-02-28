package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SceneRouter {
    private SceneRouter() {}

    public static void go(Node anyNodeInScene, String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneRouter.class.getResource("/" + fxml));
            Parent root = loader.load();

            Stage stage = (Stage) anyNodeInScene.getScene().getWindow();
            Scene scene = new Scene(root, 520, 640);
            scene.getStylesheets().add(SceneRouter.class.getResource("/style.css").toExternalForm());

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + fxml, e);
        }
    }
}
