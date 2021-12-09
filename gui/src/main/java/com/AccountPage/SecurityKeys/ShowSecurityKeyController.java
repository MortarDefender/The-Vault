package com.AccountPage.SecurityKeys;

import com.Context;
import javafx.fxml.FXML;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.AccountPage.AccountPreview;
import javafx.scene.control.PasswordField;
import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;


public class ShowSecurityKeyController  implements AccountPreview {

    @FXML private Label accountNameTitle;
    @FXML private TextArea notesEditText;
    @FXML private TextField linkEditText;
    @FXML private TextField publicKeyUnMasked;
    @FXML private TextField privateKeyUnMasked;
    @FXML private PasswordField publicKeyMasked;
    @FXML private PasswordField privateKeyMasked;
    @FXML private FontAwesomeIconView publicVisibilityAwesomeIcon;
    @FXML private FontAwesomeIconView privateVisibilityAwesomeIcon;

    private String userSessionKey;
    private AccountInterDto currentAccount;
    private BooleanProperty publicVisibilityProperty;
    private BooleanProperty privateVisibilityProperty;


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
    public void deleteAccount(ActionEvent event) {
        this.deleteAccount(userSessionKey, currentAccount);
    }

    @FXML
    public void publicMaskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(publicVisibilityProperty, publicVisibilityAwesomeIcon);
    }

    @FXML
    public void privateMaskUnmaskPassword(MouseEvent event) {

        Context.maskUnmaskPassword(privateVisibilityProperty, privateVisibilityAwesomeIcon);
    }

    @FXML
    public void openLink(MouseEvent event) { Context.openUrl(linkEditText.getText()); }

    @FXML
    public void saveAccountChanges(ActionEvent event) {
        Context.safe.changeUserAccount(userSessionKey, new AccountDto(accountNameTitle.getText(), publicKeyMasked.getText(),
                privateKeyMasked.getText(), linkEditText.getText(), notesEditText.getText(), AccountCategory.SecurityKeys));
    }

    @FXML
    public void copyPublicKeyToClipboard(MouseEvent event) { this.copyField(publicKeyMasked.getText()); }

    @FXML
    public void copyPrivateKeyToClipboard(MouseEvent event) { this.copyField(privateKeyMasked.getText()); }

    private void setAccountVariables() {
        linkEditText.setText(currentAccount.getLink());
        notesEditText.setText(currentAccount.getNotes());
        publicKeyMasked.setText(currentAccount.getPassword());
        publicKeyMasked.setText(currentAccount.getPassword());
        privateKeyMasked.setText(currentAccount.getUsername());
        privateKeyUnMasked.setText(currentAccount.getUsername());
        accountNameTitle.setText(currentAccount.getAccountName());
    }
}
