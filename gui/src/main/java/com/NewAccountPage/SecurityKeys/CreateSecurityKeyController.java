package com.NewAccountPage.SecurityKeys;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import Objects.interfaces.AccountCategory;
import javafx.scene.control.PasswordField;
import com.NewAccountPage.AccountGenerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class CreateSecurityKeyController implements AccountGenerator {

    @FXML private TextArea notesEditText;
    @FXML private TextField linkEditText;
    @FXML private TextField publicKeyUnMasked;
    @FXML private TextField privateKeyUnMasked;
    @FXML private TextField accountNameEditText;
    @FXML private PasswordField publicKeyMasked;
    @FXML private PasswordField privateKeyMasked;
    @FXML private FontAwesomeIconView publicVisibilityAwesomeIcon;
    @FXML private FontAwesomeIconView privateVisibilityAwesomeIcon;

    private Stage window;
    private String userSessionKey;
    private BooleanProperty publicVisibilityProperty;
    private BooleanProperty privateVisibilityProperty;


    @Override
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    @Override
    public void setStage(Stage stage) { window = stage; }

    @FXML
    public void initialize() {
        publicVisibilityProperty = new SimpleBooleanProperty(publicVisibilityAwesomeIcon.getGlyphName().equals(FontAwesomeIcon.EYE.name()));
        privateVisibilityProperty = new SimpleBooleanProperty(privateVisibilityAwesomeIcon.getGlyphName().equals(FontAwesomeIcon.EYE.name()));

        setVisibility(publicVisibilityProperty, publicKeyMasked, publicKeyUnMasked);
        setVisibility(privateVisibilityProperty, privateKeyMasked, privateKeyUnMasked);
    }

    private void setVisibility(BooleanProperty visibilityProperty, PasswordField keyMasked, TextField keyUnMasked) {
        keyUnMasked.visibleProperty().bind(Bindings.not(visibilityProperty));
        keyMasked.visibleProperty().bind(visibilityProperty);

        keyUnMasked.textProperty().bindBidirectional(keyMasked.textProperty());
    }

    @FXML
    public void cancelAccount(ActionEvent event) {
        closeWindow();
    }

    @FXML
    public void createNewAccount(ActionEvent event) {
        Context.safe.addAccountToUser(userSessionKey, new AccountDto(accountNameEditText.getText(), publicKeyMasked.getText(),
                privateKeyMasked.getText(), linkEditText.getText(), notesEditText.getText(), AccountCategory.SecurityKeys));
        closeWindow();
    }

    private void closeWindow() { window.close(); }

    @FXML
    public void publicMaskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(publicVisibilityProperty, publicVisibilityAwesomeIcon);
    }

    @FXML
    public void privateMaskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(privateVisibilityProperty, privateVisibilityAwesomeIcon);
    }
}
