package com.LoginPage;

import com.Toast;
import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.PasswordField;
import java.security.InvalidParameterException;

import javafx.event.ActionEvent;
import com.VaultPage.VaultController;
import javafx.scene.control.TextField;


public class LoginController {
    @FXML private TextField usernameEditText;
    @FXML private PasswordField passwordEditText;

    private Stage window;

    public void setStage(Stage stage) { this.window = stage; }

    @FXML
    public void authenticate(ActionEvent actionEvent) {
        try {
            String userSessionKey = Context.safe.login(usernameEditText.getText(), passwordEditText.getText());
            goToLogin(userSessionKey);
        } catch (InvalidParameterException e) {
            Toast.makeText(e.getMessage(), Toast.TimeDuration.SHORT);
        } catch (IOException ignore) { }
    }

    private void goToLogin(String userSessionKey) throws IOException {
        Context.createScene(window, "The Vault", Context.vaultUrl,
            (fxmlLoader) -> {
                VaultController vaultController = fxmlLoader.getController();
                vaultController.setWindow(window);
                vaultController.setUserSessionKey(userSessionKey);
            }
        );
    }

    @FXML
    public void registerAccount(MouseEvent event) throws IOException {
        try {
            Context.safe.register(usernameEditText.getText(), passwordEditText.getText());
            String userSessionKey = Context.safe.login(usernameEditText.getText(), passwordEditText.getText());
            Context.createScene(window, "The Vault", Context.vaultUrl,
                    (fxmlLoader) -> {
                        VaultController vaultController = fxmlLoader.getController();
                        vaultController.setWindow(window);
                        vaultController.setUserSessionKey(userSessionKey);
                    }
            );
        } catch (InvalidParameterException e) {
            Toast.makeText(e.getMessage());
        }
    }
}
