package com.SearchAccounts;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.AccountPage.AccountPreview;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import Objects.interfaces.AccountInterDto;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.List;
import java.io.IOException;


public class SearchAccountsController {
    @FXML private TableView<AccountInterDto> accountsTable;
    @FXML private TableColumn<AccountInterDto, String> nameTableColumn;
    @FXML private TableColumn<AccountInterDto, String> linkTableColumn;
    @FXML private TableColumn<AccountInterDto, String> notesTableColumn;
    @FXML private TableColumn<AccountInterDto, String> usernameTableColumn;
    @FXML private TableColumn<AccountInterDto, String> passwordTableColumn;

    private String userSessionKey;

    public void setUserSessionKey(String key) { userSessionKey = key; }

    public void setSearchText(String textToSearch) {
        List<AccountInterDto> searchResult = Context.safe.searchInAccounts(userSessionKey, textToSearch);

        accountsTable.setItems(FXCollections.observableArrayList(searchResult));
    }

    public static void initSearchPage(Stage stage, String userSessionKey, String textToSearch) throws IOException {
        Context.createScene(stage, "Search", Context.searchAccountsUrl,
                (fxmlLoader) -> {
                    SearchAccountsController searchAccountsController = fxmlLoader.getController();
                    searchAccountsController.setUserSessionKey(userSessionKey);
                    searchAccountsController.setSearchText(textToSearch);
                }
        );
    }

    @FXML
    public void initialize() {
        linkTableColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getLink()));
        notesTableColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNotes()));
        nameTableColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAccountName()));
        usernameTableColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getUsername()));
        passwordTableColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPassword()));

        linkTableColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 5));
        nameTableColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 5));
        notesTableColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 5));
        usernameTableColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 5));
        passwordTableColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 5));

        linkTableColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");
        nameTableColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");
        notesTableColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");
        usernameTableColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");
        passwordTableColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");

        accountsTable.setPlaceholder(new Label("No rows to display"));
    }

    @FXML
    public void showAccount(MouseEvent event) throws IOException {
        String fxmlPath;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        AccountInterDto account = accountsTable.getSelectionModel().getSelectedItem();

        switch (account.getCategory()) {
            case Payment:
                fxmlPath = Context.showPaymentAccountUrl;
                break;
            case SecureNotes:
                fxmlPath = Context.showSecureNoteUrl;
                break;
            case SecurityKeys:
                fxmlPath = Context.showSecurityKeysUrl;
                break;
            default:
                fxmlPath = Context.showDefaultAccountUrl;
                break;
        }

        AccountPreview.initAccountPreview(stage, userSessionKey, account, fxmlPath);
    }
}
