package com.AccountsPage;

import com.Context;
import javafx.fxml.FXML;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.AccountPage.AccountPreview;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import Objects.interfaces.AccountInterDto;
import com.NewAccountPage.AccountGenerator;
import com.SearchAccounts.SearchAccountsController;
import javafx.beans.property.ReadOnlyObjectWrapper;


public class ShowAccountsController {
    @FXML private Label addIcon;
    @FXML private TextField searchBar;
    @FXML private TableView<AccountInterDto> accountsTable;
    @FXML private TableColumn<AccountInterDto, String> accountNameColumn;

    private String userSessionKey;


    public void setUserSessionKey(String key) {
        userSessionKey = key;
        accountsTable.setItems(FXCollections.observableArrayList(Context.safe.showAccounts(userSessionKey)));
    }

    @FXML
    private void initialize() {
        accountNameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAccountName()));
        accountNameColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 1));
        accountNameColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");

        accountsTable.setPlaceholder(new Label("No rows to display"));
    }

    @FXML
    public void showAccount(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        AccountInterDto account = accountsTable.getSelectionModel().getSelectedItem();

        Context.createScene(stage, account.getAccountName(), Context.showAccountUrl,
                (fxmlLoader) -> {
                    AccountPreview showAccountController = fxmlLoader.getController();
                    showAccountController.setUserSessionKey(userSessionKey);
                    showAccountController.setUserAccount(account);
                }
        );
    }

    @FXML
    public void addAccount(MouseEvent event) throws IOException {
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
    public void searchAccount(ActionEvent event) throws IOException {
        // open search
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Context.createScene(stage, "Search", Context.searchAccountsUrl,
                (fxmlLoader) -> {
                    SearchAccountsController searchAccountsController = fxmlLoader.getController();
                    searchAccountsController.setUserSessionKey(userSessionKey);
                    searchAccountsController.setSearchText(searchBar.getText());
                }
        );
    }

    @FXML
    public void searchTyping(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            searchAccount(new ActionEvent());
        }
    }
}
