package com.LoginPage;

import com.Toast;
import com.Context;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextField;
import com.VaultPage.VaultPageController;
import javafx.scene.control.PasswordField;
import javafx.beans.property.BooleanProperty;


import java.io.IOException;
import java.security.InvalidParameterException;


public class LoginPageController {
    @FXML private TextField usernameEditText;
    @FXML private TextField passwordUnMasked;
    @FXML private PasswordField passwordMasked;
    @FXML private FontAwesomeIconView visibilityAwesomeIcon;

    private Stage window;
    private BooleanProperty visibilityProperty;


    public void setStage(Stage stage) { this.window = stage; }

    public static void initLoginPage(Stage stage) throws IOException {
        Context.createScene(stage, "Login", Context.loginUrl,
                (fxmlLoader) -> {
                    LoginPageController loginController = fxmlLoader.getController();
                    loginController.setStage(stage);
                }
        );
    }

    @FXML
    public void initialize() {

        this.visibilityProperty = new SimpleBooleanProperty(visibilityAwesomeIcon.getGlyphName().equals(FontAwesomeIcon.EYE.name()));
        this.passwordUnMasked.visibleProperty().bind(Bindings.not(this.visibilityProperty));
        this.passwordMasked.visibleProperty().bind(this.visibilityProperty);

        this.passwordUnMasked.textProperty().bindBidirectional(this.passwordMasked.textProperty());
    }

    @FXML
    public void maskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(visibilityProperty, visibilityAwesomeIcon);
    }

    @FXML
    public void loginTyping(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            authenticate(null);
        }
    }

    @FXML
    public void authenticate(ActionEvent actionEvent) {
        try {
            String userSessionKey = Context.safe.login(usernameEditText.getText(), passwordMasked.getText());
            userLogin(userSessionKey);
        } catch (InvalidParameterException e) {
            Toast.makeText(e.getMessage());
        } catch (IOException e2) {
            System.out.println(e2.getMessage());

        }
    }

    @FXML
    public void registerAccount(MouseEvent event) throws IOException {
        try {
            Context.safe.register(usernameEditText.getText(), passwordMasked.getText());
            String userSessionKey = Context.safe.login(usernameEditText.getText(), passwordMasked.getText());
            userLogin(userSessionKey);
        } catch (InvalidParameterException e) {
            Toast.makeText(e.getMessage());
        }
    }

    public void userLogin(String userSessionKey) throws IOException {
        VaultPageController.initVaultPage(window, userSessionKey, usernameEditText.getText());
    }
}
