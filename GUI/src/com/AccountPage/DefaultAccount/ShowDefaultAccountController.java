package com.AccountPage.DefaultAccount;

import com.Context;
import javafx.fxml.FXML;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import com.AccountPage.AccountPreview;
import Objects.interfaces.AccountCategory;
import javafx.scene.control.PasswordField;
import Objects.interfaces.AccountInterDto;
import javafx.beans.property.SimpleBooleanProperty;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;


public class ShowDefaultAccountController implements AccountPreview {

    @FXML private Label accountNameTitle;
    @FXML private TextArea notesEditText;
    @FXML private TextField linkEditText;
    @FXML private TextField usernameEditText;
    @FXML private TextField passwordUnMasked;
    @FXML private PasswordField passwordMasked;
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
        this.passwordUnMasked.visibleProperty().bind(Bindings.not(this.visibilityProperty));
        this.passwordMasked.visibleProperty().bind(this.visibilityProperty);

        this.passwordUnMasked.textProperty().bindBidirectional(this.passwordMasked.textProperty());
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
        Context.safe.changeUserAccount(userSessionKey, new AccountDto(accountNameTitle.getText(), usernameEditText.getText(),
                                       passwordMasked.getText(), linkEditText.getText(), notesEditText.getText(), AccountCategory.Default));
    }

    @FXML
    public void openLink(MouseEvent event) { Context.openUrl(linkEditText.getText()); }

    @FXML
    public void copyPasswordToClipboard(MouseEvent event) { this.copyField(passwordMasked.getText()); }

    @FXML
    public void copyUsernameToClipboard(MouseEvent event) { this.copyField(usernameEditText.getText()); }

    private void setAccountVariables() {
        linkEditText.setText(currentAccount.getLink());
        notesEditText.setText(currentAccount.getNotes());
        passwordMasked.setText(currentAccount.getPassword());
        passwordUnMasked.setText(currentAccount.getPassword());
        usernameEditText.setText(currentAccount.getUsername());
        accountNameTitle.setText(currentAccount.getAccountName());
    }
}
