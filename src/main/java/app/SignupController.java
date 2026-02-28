package app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class SignupController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Button createAccountBtn;
    @FXML private Label statusLabel;

    @FXML private HBox nameRow;
    @FXML private HBox emailRow;
    @FXML private HBox passRow;
    @FXML private HBox confirmRow;

    private final UserDao userDao = new UserDao();

    private static final Pattern EMAIL_RE =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @FXML
    public void initialize() {
        clearStatus();
        clearErrors();

        nameField.textProperty().addListener((o,a,b) -> clearError(nameRow));
        emailField.textProperty().addListener((o,a,b) -> clearError(emailRow));
        passwordField.textProperty().addListener((o,a,b) -> clearError(passRow));
        confirmPasswordField.textProperty().addListener((o,a,b) -> clearError(confirmRow));

        Database.init();
    }

    @FXML
    private void onCreateAccount() {
        clearStatus();
        clearErrors();

        String name = safe(nameField.getText()).trim();
        String email = safe(emailField.getText()).trim().toLowerCase();
        String pass = safe(passwordField.getText());
        String confirm = safe(confirmPasswordField.getText());

        boolean ok = true;
        if (name.isBlank()) { markError(nameRow); ok = false; }
        if (email.isBlank() || !EMAIL_RE.matcher(email).matches()) { markError(emailRow); ok = false; }
        if (pass.length() < 8) { markError(passRow); ok = false; }
        if (!pass.equals(confirm)) { markError(confirmRow); ok = false; }

        if (!ok) {
            setStatusError("Please fix the highlighted fields.");
            return;
        }

        createAccountBtn.setDisable(true);
        createAccountBtn.setText("CREATING...");

        try {
            if (userDao.emailExists(email)) {
                markError(emailRow);
                setStatusError("That email is already registered.");
                return;
            }

            userDao.createUser(name, email, pass);

            setStatusSuccess("Account created! Please log in.");

            // go to login screen after success
            SceneRouter.go(createAccountBtn, "login.fxml", "Intramural Soccer • Log In");

        } catch (SQLException e) {
            setStatusError("Signup failed: " + e.getMessage());
        } finally {
            createAccountBtn.setDisable(false);
            createAccountBtn.setText("CREATE ACCOUNT");
        }
    }

    @FXML
    private void onGoLogin() {
        SceneRouter.go(createAccountBtn, "login.fxml", "Intramural Soccer • Log In");
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

    private void setStatusSuccess(String msg) {
        statusLabel.getStyleClass().removeAll("status-error");
        if (!statusLabel.getStyleClass().contains("status-success"))
            statusLabel.getStyleClass().add("status-success");
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
        clearError(nameRow);
        clearError(emailRow);
        clearError(passRow);
        clearError(confirmRow);
    }
}
