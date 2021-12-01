package com.NewAccountPage.DefaultAccount;

import com.Context;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import com.NewAccountPage.AccountGenerator;
import com.PasswordGenerator.PasswordGenerator;
import javafx.beans.property.SimpleBooleanProperty;


import java.io.IOException;


public class CreateDefaultAccountController implements AccountGenerator {

    @FXML private TextArea notesEditText;
    @FXML private TextField linkEditText;
    @FXML private TextField usernameEditText;
    @FXML private TextField passwordUnMasked;
    @FXML private PasswordField passwordMasked;
    @FXML private TextField accountNameEditText;
    @FXML private FontAwesomeIconView visibilityAwesomeIcon;

    private Stage window;
    private String userSessionKey;
    private SimpleBooleanProperty visibilityProperty;


    @Override
    public void setStage(Stage stage) { this.window = stage; }

    @Override
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    @FXML
    public void initialize() {
        //this.visibilityProperty = new SimpleBooleanProperty(visibilityAwesomeIcon.getGlyphName().equals(FontAwesomeIcon.EYE.name()));
        this.passwordUnMasked.visibleProperty().bind(Bindings.not(this.visibilityProperty));
        this.passwordMasked.visibleProperty().bind(this.visibilityProperty);

        this.passwordUnMasked.textProperty().bindBidirectional(this.passwordMasked.textProperty());
    }

    @FXML
    public void cancelAccount(ActionEvent event) { closeWindow(); }

    @FXML
    public void createNewAccount(ActionEvent event) {
        Context.safe.addAccountToUser(userSessionKey, new AccountDto(accountNameEditText.getText(), usernameEditText.getText(),
                passwordMasked.getText(), linkEditText.getText(), notesEditText.getText()));
        closeWindow();
    }

    private void closeWindow() { window.close(); }

    @FXML
    public void generatePassword(MouseEvent event) throws IOException {
        PasswordGenerator.initPasswordGenerator(new Stage(), this.passwordMasked::setText, Context.SimplePasswordGeneratorUrl);
    }

    @FXML
    public void maskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(visibilityProperty, visibilityAwesomeIcon);
    }
}
