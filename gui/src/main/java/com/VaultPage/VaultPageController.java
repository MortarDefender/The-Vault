package com.VaultPage;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import com.AccountPage.AccountPreview;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import com.LoginPage.LoginPageController;
import Objects.interfaces.AccountCategory;
import Objects.interfaces.AccountInterDto;
import com.NewAccountPage.AccountGenerator;
import com.SearchAccounts.SearchAccountsController;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.io.IOException;


public class VaultPageController {

    private Stage window;
    private String userSessionKey;
    private AccountCategory currentCategory;
    private double minWidth, prefWidth, maxWidth;

    @FXML private VBox sideMenu;
    @FXML private Label paymentLabel;
    @FXML private Label usernameTitle;
    @FXML private Label secureNotesLabel;
    @FXML private Label allAccountsLabel;
    @FXML private Label workAccountsLabel;
    @FXML private Label securityKeysLabel;
    @FXML private Label schoolAccountsLabel;
    @FXML private Label vacationAccountsLabel;
    @FXML private TableView<AccountInterDto> accountsTable;
    // TODO: FIX DEPENDENCY
    //@FXML private com.gluonhq.charm.glisten.control.TextField searchBar;
    @FXML private TableColumn<AccountInterDto, String> accountNameColumn;

    public void setUsername(String username) { usernameTitle.setText(username); }

    public void setStage(Stage stage) { this.window = stage; }

    public void setUserSessionKey(String key) {
        userSessionKey = key;
        accountsTable.setItems(FXCollections.observableArrayList(Context.safe.showAccountsOfCategory(userSessionKey, currentCategory)));
    }

    public static void initVaultPage(Stage stage, String userSessionKey, String username) throws IOException {
        Context.createScene(stage, "The Vault", Context.vaultUrl,
                (fxmlLoader) -> {
                    VaultPageController vaultController = fxmlLoader.getController();
                    vaultController.setStage(stage);
                    vaultController.setUsername(username);
                    vaultController.setUserSessionKey(userSessionKey);
                }
        );
    }

    @FXML
    private void initialize() {
        currentCategory = AccountCategory.Default;

        accountNameColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAccountName()));
        accountNameColumn.prefWidthProperty().bind(Context.bindStageWidth(accountsTable, 1));
        accountNameColumn.setStyle( "-fx-alignment: CENTER;-fx-font-size: 13px;");

        accountsTable.setPlaceholder(new Label("No rows to display"));

        minWidth = sideMenu.getMinWidth();
        prefWidth = sideMenu.getPrefWidth();
        maxWidth = sideMenu.getMaxWidth();
    }

    @FXML
    public void addNewAccount(MouseEvent event) throws IOException {
        String fxmlPath;
//        Stage stage = new Stage();

        switch (currentCategory) {
            case Payment:
                fxmlPath = Context.newPaymentAccountUrl;
                break;
            case SecureNotes:
                fxmlPath = Context.newSecureNoteUrl;
                break;
            case SecurityKeys:
                fxmlPath = Context.newSecurityKeysUrl;
                break;
            default:
                fxmlPath = Context.newDefaultAccountUrl;
                break;
        }


        AccountGenerator.initAccountGenerator(new Stage(), userSessionKey, fxmlPath);

//        Context.createScene(stage, "Create New Account", fxmlPath,
//                (fxmlLoader) -> {
//                    AccountGenerator accountGenerator = fxmlLoader.getController();
//                    accountGenerator.setStage(stage);
//                    accountGenerator.setUserSessionKey(userSessionKey);
//                }
//        );
    }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
        Context.safe.logout(userSessionKey);
        LoginPageController.initLoginPage(window);
    }

    @FXML
    public void search(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        // TODO: FIX DEPENDENCY
        //SearchAccountsController.initSearchPage(stage, userSessionKey, searchBar.getText());
    }

    @FXML
    public void searchTyping(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            search(null);
        }
    }

    @FXML
    public void showAccount(MouseEvent event) throws IOException {
        String fxmlPath;
        AccountInterDto account = accountsTable.getSelectionModel().getSelectedItem();

        switch (currentCategory) {
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

        AccountPreview.initAccountPreview(new Stage(), userSessionKey, account, fxmlPath);
    }

    @FXML
    public void showAllAccounts(MouseEvent event) {
        setChosenTitle(allAccountsLabel);
        setCategory(AccountCategory.All, "accounts");
        accountsTable.setItems(FXCollections.observableArrayList(Context.safe.showAccounts(userSessionKey)));
    }

    @FXML
    public void showPaymentAccounts(MouseEvent event) {
        setChosenTitle(paymentLabel);
        setCategory(AccountCategory.Payment, "payment accounts");
    }

    @FXML
    public void showSecureNotes(MouseEvent event) {
        setChosenTitle(secureNotesLabel);
        setCategory(AccountCategory.SecureNotes, "secure notes");
    }

    @FXML
    public void showWorkAccounts(MouseEvent event) {
        setChosenTitle(workAccountsLabel);
        setCategory(AccountCategory.Work, "work accounts");
    }

    @FXML
    public void showSecurityKeyAccounts(MouseEvent event) {
        setChosenTitle(securityKeysLabel);
        setCategory(AccountCategory.SecurityKeys, "security keys accounts");
    }

    @FXML
    public void showSchoolAccounts(MouseEvent event) {
        setChosenTitle(schoolAccountsLabel);
        setCategory(AccountCategory.School, "School accounts");
    }

    @FXML
    public void showVacationAccounts(MouseEvent event) {
        setChosenTitle(vacationAccountsLabel);
        setCategory(AccountCategory.Vacation, "Vacation accounts");
    }

    @FXML
    public void toggleSideMenu(MouseEvent event) {
        if (sideMenu.isVisible()) {
            closeSideMenu();
        } else {
            openSideMenu();
        }
        sideMenu.setVisible(!sideMenu.isVisible());
    }

    private void openSideMenu() {
        sideMenu.setMinWidth(minWidth);
        sideMenu.setPrefWidth(prefWidth);
        sideMenu.setMaxWidth(maxWidth);
    }

    private void closeSideMenu() {
        sideMenu.setMinWidth(0);
        sideMenu.setPrefWidth(0);
        sideMenu.setMaxWidth(0);
    }

    private void setChosenTitle(Label currentLabel) {
        Label prevLabel;

        switch (currentCategory) {
            case Work:
                prevLabel = workAccountsLabel;
                break;
            case School:
                prevLabel = schoolAccountsLabel;
                break;
            case Vacation:
                prevLabel = vacationAccountsLabel;
                break;
            case Payment:
                prevLabel = paymentLabel;
                break;
            case SecureNotes:
                prevLabel = secureNotesLabel;
                break;
            case SecurityKeys:
                prevLabel = securityKeysLabel;
                break;
            default:
                prevLabel = allAccountsLabel;
        }

        prevLabel.setStyle("");
        currentLabel.setStyle("-fx-background-color: rgba(0, 0, 0, .3);");
    }

    private void setCategory(AccountCategory category, String accountTitle) {
        this.currentCategory = category;
        accountsTable.setPlaceholder(new Label(String.format("There are no %s to display", accountTitle)));
        accountsTable.setItems(FXCollections.observableArrayList(Context.safe.showAccountsOfCategory(userSessionKey, currentCategory)));
    }
}
