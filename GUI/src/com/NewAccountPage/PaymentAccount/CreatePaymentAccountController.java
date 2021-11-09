package com.NewAccountPage.PaymentAccount;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import Objects.interfaces.AccountCategory;
import com.NewAccountPage.AccountGenerator;
import javafx.beans.property.SimpleBooleanProperty;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;


public class CreatePaymentAccountController  implements AccountGenerator {

    @FXML private TextField codeUnMasked;
    @FXML private TextArea notesEditText;
    @FXML private PasswordField codeMasked;
    @FXML private TextField cardNumberEditText;
    @FXML private TextField accountNameEditText;
    @FXML private DatePicker cardExpirationDate;
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
        this.visibilityProperty = new SimpleBooleanProperty(visibilityAwesomeIcon.getGlyphName().equals(FontAwesomeIcon.EYE.name()));
        this.codeUnMasked.visibleProperty().bind(Bindings.not(this.visibilityProperty));
        this.codeMasked.visibleProperty().bind(this.visibilityProperty);

        this.codeUnMasked.textProperty().bindBidirectional(this.codeMasked.textProperty());
    }

    @FXML
    public void cancelAccount(ActionEvent event) {
        closeWindow();
    }

    @FXML
    public void createNewAccount(ActionEvent event) {
        Context.safe.addAccountToUser(userSessionKey, new AccountDto(accountNameEditText.getText(), cardNumberEditText.getText(),
                codeMasked.getText(), getDate(), notesEditText.getText(), AccountCategory.Payment));
        closeWindow();
    }

    private void closeWindow() { window.close(); }

    private String getDate() {
        return Long.toString(cardExpirationDate.getValue().toEpochDay());
    }

    @FXML
    public void maskUnmaskPassword(MouseEvent event) {
        Context.maskUnmaskPassword(visibilityProperty, visibilityAwesomeIcon);
    }
}
