package app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Label statusLabel;

    @FXML private HBox emailRow;
    @FXML private HBox passRow;

    private final UserDao userDao = new UserDao();

    private static final Pattern EMAIL_RE =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @FXML
    public void initialize() {
        clearStatus();
        clearErrors();

        emailField.textProperty().addListener((o,a,b) -> clearError(emailRow));
        passwordField.textProperty().addListener((o,a,b) -> clearError(passRow));

        Database.init();
    }

    @FXML
    private void onLogin() {
        clearStatus();
        clearErrors();

        String email = safe(emailField.getText()).trim().toLowerCase();
        String pass  = safe(passwordField.getText());

        boolean ok = true;
        if (email.isBlank() || !EMAIL_RE.matcher(email).matches()) { markError(emailRow); ok = false; }
        if (pass.isBlank()) { markError(passRow); ok = false; }

        if (!ok) {
            setStatusError("Please fix the highlighted fields.");
            return;
        }

        loginBtn.setDisable(true);
        loginBtn.setText("LOGGING IN...");

        try {
            UserDao.UserRecord u = userDao.findByEmail(email);
            if (u == null) {
                markError(emailRow);
                setStatusError("No account found for that email.");
                return;
            }

            if (!Auth.verifyPassword(pass, u.salt(), u.passwordHash())) {
                markError(passRow);
                setStatusError("Incorrect password.");
                return;
            }

            Session.setCurrentUser(u);
            SceneRouter.go(loginBtn, "dashboard.fxml", "Intramural Soccer • Dashboard");

        } catch (SQLException e) {
            setStatusError("Login failed: " + e.getMessage());
        } finally {
            loginBtn.setDisable(false);
            loginBtn.setText("LOG IN");
        }
    }

    @FXML
    private void onGoSignup() {
        SceneRouter.go(loginBtn, "signup.fxml", "Intramural Soccer • Create Account");
    }

    private static String safe(String s) { return s == null ? "" : s; }

    // ---------- UI helpers ----------
    private void setStatusError(String msg) {
        statusLabel.getStyleClass().removeAll("status-success");
        if (!statusLabel.getStyleClass().contains("status-error"))
            statusLabel.getStyleClass().add("status-error");
        statusLabel.setText(msg);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }

    private void clearStatus() {
        statusLabel.setText("");
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
    }

    private void markError(HBox row) {
        if (!row.getStyleClass().contains("field-error"))
            row.getStyleClass().add("field-error");
    }

    private void clearError(HBox row) {
        row.getStyleClass().remove("field-error");
    }

    private void clearErrors() {
        clearError(emailRow);
        clearError(passRow);
    }
}
