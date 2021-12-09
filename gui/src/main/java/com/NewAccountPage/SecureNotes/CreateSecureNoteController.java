package com.NewAccountPage.SecureNotes;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import Objects.interfaces.AccountCategory;
import com.NewAccountPage.AccountGenerator;


public class CreateSecureNoteController implements AccountGenerator {

    @FXML private TextArea notesEditText;
    @FXML private TextField titleEditText;
    @FXML private TextField accountNameEditText;

    private Stage window;
    private String userSessionKey;


    @Override
    public void setStage(Stage stage) { this.window = stage; }

    @Override
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    @FXML
    public void cancelAccount(ActionEvent event) {
        closeWindow();
    }

    @FXML
    public void createNewAccount(ActionEvent event) {
        Context.safe.addAccountToUser(userSessionKey, new AccountDto(accountNameEditText.getText(), titleEditText.getText(),
                "", "", notesEditText.getText(), AccountCategory.SecureNotes));
        closeWindow();
    }

    private void closeWindow() { window.close(); }
}
