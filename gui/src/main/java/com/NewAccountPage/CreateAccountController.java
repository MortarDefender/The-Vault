package com.NewAccountPage;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.stage.Modality;
import Objects.dto.AccountDto;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import com.PasswordGenerator.PasswordGenerator;


public class CreateAccountController implements AccountGenerator {
    @FXML private TextField linkEditText;
    @FXML private TextField notesEditText;
    @FXML private TextField usernameEditText;
    @FXML private TextField accountNameEditText;
    @FXML private PasswordField passwordEditText;

    private Stage window;
    private String userSessionKey;

    @FXML
    private void initialize() {
        // property
        // passwordEditText.
        // passwordEditText.onInputMethodTextChangedProperty().bind();
    }

    public void setStage(Stage stage) { this.window = stage; }

    public void setUserSessionKey(String key) { this.userSessionKey = key; }

    @FXML
    public void generate(ActionEvent actionEvent) throws IOException {
        // create a new generate password window
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Context.createScene(stage, "Password Generator", Context.SimplePasswordGeneratorUrl, //  ManagedPasswordGeneratorUrl
                (fxmlLoader) -> {
                    PasswordGenerator passwordGeneratorController = fxmlLoader.getController();
                    passwordGeneratorController.setStage(stage);
                    passwordGeneratorController.setRetrieveFunction(passwordEditText::setText);
                }
        );
    }

    @FXML
    public void create(ActionEvent actionEvent) {
        Context.safe.addAccountToUser(userSessionKey,
                new AccountDto(accountNameEditText.getText(), usernameEditText.getText(),
                               passwordEditText.getText(), linkEditText.getText(), notesEditText.getText()));
        resetFields();
    }

    private void resetFields() {
        linkEditText.setText("");
        notesEditText.setText("");
        usernameEditText.setText("");
        passwordEditText.setText("");
        accountNameEditText.setText("");
    }
}
