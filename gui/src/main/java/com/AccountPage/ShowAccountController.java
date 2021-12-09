package com.AccountPage;

import Objects.dto.AccountDto;
import com.Context;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Objects.interfaces.AccountInterDto;

public class ShowAccountController implements AccountPreview {

    @FXML private Label accountLabel;
    @FXML private TextField linkEditText;
    @FXML private TextField notesEditText;
    @FXML private TextField usernameEditText;
    @FXML private TextField passwordEditText;

    private String userSessionKey;
    private AccountInterDto currentAccount;


    @Override
    public void setUserSessionKey(String key) { userSessionKey = key; }

    @Override
    public void setUserAccount(AccountInterDto account) {
        currentAccount = account;
        linkEditText.setText(account.getLink());
        notesEditText.setText(account.getNotes());
        accountLabel.setText(account.getAccountName());
        usernameEditText.setText(account.getUsername());
        passwordEditText.setText(account.getPassword());
    }

    @FXML
    public void deleteAccount(ActionEvent actionEvent) {
        Context.safe.removeAccount(userSessionKey, currentAccount.getAccountName());
    }

    @FXML
    public void changeAccount(ActionEvent actionEvent) {
        Context.safe.changeUserAccount(userSessionKey, new AccountDto(accountLabel.getText(), usernameEditText.getText(),
                                       passwordEditText.getText(), linkEditText.getText(), notesEditText.getText()));
    }
}
