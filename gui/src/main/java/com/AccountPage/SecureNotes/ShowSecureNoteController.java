package com.AccountPage.SecureNotes;

import com.Context;
import javafx.fxml.FXML;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.AccountPage.AccountPreview;
import Objects.interfaces.AccountInterDto;
import Objects.interfaces.AccountCategory;


public class ShowSecureNoteController implements AccountPreview {

    @FXML private Label accountNameTitle;
    @FXML private TextArea notesEditText;
    @FXML private TextField titleEditText;

    private String userSessionKey;
    private AccountInterDto currentAccount;


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
    public void deleteAccount(ActionEvent event) {
        this.deleteAccount(userSessionKey, currentAccount);
    }

    @FXML
    public void saveAccountChanges(ActionEvent event) {
        Context.safe.changeUserAccount(userSessionKey,
                new AccountDto(accountNameTitle.getText(), titleEditText.getText(),
                        "", "" , notesEditText.getText(), AccountCategory.SecureNotes));
    }

    private void setAccountVariables() {
        notesEditText.setText(currentAccount.getNotes());
        titleEditText.setText(currentAccount.getUsername());
        accountNameTitle.setText(currentAccount.getAccountName());
    }
}
