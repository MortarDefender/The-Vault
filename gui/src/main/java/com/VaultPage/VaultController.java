package com.VaultPage;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import com.LoginPage.LoginController;
import com.NewAccountPage.AccountGenerator;
import com.AccountsPage.ShowAccountsController;


public class VaultController {
    private Stage window;
    private String userSessionKey;

    public void setWindow(Stage stage) {
        this.window = stage;
    }

    public void setUserSessionKey(String key) { this.userSessionKey = key; }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
        Context.safe.logout(userSessionKey);

        Context.createScene(window, "Login", Context.loginUrl,
                (fxmlLoader) -> {
                    LoginController loginController = fxmlLoader.getController();
                    loginController.setStage(window);
                }
        );
    }

    @FXML
    public void createNewAccount(MouseEvent event) throws IOException {
        // open a new window of create a new account
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Context.createScene(stage, "Create Account", Context.newAccountUrl,
           (fxmlLoader) -> {
                AccountGenerator createAccountController = fxmlLoader.getController();
                createAccountController.setStage(stage);
                createAccountController.setUserSessionKey(userSessionKey);
           }
        );
    }

    @FXML
    public void showAccounts(MouseEvent event) throws IOException {
        // go to show Account
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Context.createScene(stage, "User Accounts", Context.showAccountsUrl,
            (fxmlLoader) -> {
                ShowAccountsController showAccountsController = fxmlLoader.getController();
                showAccountsController.setUserSessionKey(userSessionKey);
            }
        );
    }
}
