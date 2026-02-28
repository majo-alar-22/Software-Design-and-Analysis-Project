package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        var u = Session.getCurrentUser();
        if (u != null) {
            welcomeLabel.setText("Welcome, " + u.name() + "!");
        } else {
            welcomeLabel.setText("Welcome!");
        }
    }

    @FXML
    private void onLogout(javafx.event.ActionEvent e) {
        Session.clear();
        Button btn = (Button) e.getSource();
        SceneRouter.go(btn, "login.fxml", "Intramural Soccer • Log In");
    }
}
