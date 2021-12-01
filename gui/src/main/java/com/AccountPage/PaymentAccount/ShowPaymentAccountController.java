package com.AccountPage.PaymentAccount;

import com.Context;
import javafx.fxml.FXML;
import java.time.LocalDate;
import Objects.dto.AccountDto;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.beans.binding.Bindings;
import javafx.scene.input.MouseEvent;
import com.AccountPage.AccountPreview;
import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;
import javafx.beans.property.SimpleBooleanProperty;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;


public class ShowPaymentAccountController implements AccountPreview {

    @FXML private Label accountNameTitle;
    @FXML private TextField codeUnMasked;
    @FXML private TextArea notesEditText;
    @FXML private PasswordField codeMasked;
    @FXML private TextField cardNumberEditText;
    @FXML private DatePicker cardExpirationDate;
    @FXML private FontAwesomeIconView visibilityAwesomeIcon;

    private String userSessionKey;
    private AccountInterDto currentAccount;
    private SimpleBooleanProperty visibilityProperty;


    @Override
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    @Override
    public void setUserAccount(AccountInterDto userAccount) {
        this.currentAccount = userAccount;
        setAccountVariables();
    }

    @FXML
    public void initialize() {
        this.visibilityProperty = new SimpleBooleanProperty(visibilityAwesomeIcon.getGlyphName().equals(FontAwesomeIcon.EYE.name()));
        this.codeUnMasked.visibleProperty().bind(Bindings.not(this.visibilityProperty));
        this.codeMasked.visibleProperty().bind(this.visibilityProperty);

        this.codeUnMasked.textProperty().bindBidirectional(this.codeMasked.textProperty());
    }

    @FXML
    public void deleteAccount(ActionEvent event) {
        this.deleteAccount(userSessionKey, currentAccount);
    }

    @FXML
    public void maskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(visibilityProperty, visibilityAwesomeIcon);
    }

    @FXML
    public void saveAccountChanges(ActionEvent event) {
        Context.safe.changeUserAccount(userSessionKey, new AccountDto(accountNameTitle.getText(), cardNumberEditText.getText(),
                                       codeMasked.getText(), getDate(), notesEditText.getText(), AccountCategory.Payment));
    }

    @FXML
    public void copyCodeToClipboard(MouseEvent event) { this.copyField(codeMasked.getText()); }

    @FXML
    public void copyCardNumberToClipboard(MouseEvent event) { this.copyField(cardNumberEditText.getText()); }

    private String getDate() {
        return Long.toString(cardExpirationDate.getValue().toEpochDay());
    }

    private void setDate(String accountDate) {
        cardExpirationDate.setValue(LocalDate.ofEpochDay(Long.parseLong(accountDate)));
    }

    private void setAccountVariables() {
        setDate(currentAccount.getLink());
        notesEditText.setText(currentAccount.getNotes());
        codeMasked.setText(currentAccount.getPassword());
        codeUnMasked.setText(currentAccount.getPassword());
        cardNumberEditText.setText(currentAccount.getUsername());
        accountNameTitle.setText(currentAccount.getAccountName());
    }
}
