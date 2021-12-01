package com.AccountPage;

import java.awt.*;
import com.Context;
import java.awt.datatransfer.Clipboard;
import Objects.interfaces.AccountInterDto;
import javafx.stage.Stage;

import java.awt.datatransfer.StringSelection;
import java.io.IOException;


public interface AccountPreview {
    void setUserSessionKey(String userSessionKey);
    void setUserAccount(AccountInterDto userAccount);

    default void deleteAccount(String userSessionKey, AccountInterDto account) {
        Context.safe.removeAccount(userSessionKey, account.getAccountName());
    }

    default void copyField(String fieldData) {
        StringSelection selection = new StringSelection(fieldData);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    static void initAccountPreview(Stage stage, String userSessionKey, AccountInterDto userAccount, String contextPath) throws IOException {
        Context.createScene(stage, "Show Account", contextPath,
                (fxmlLoader) -> {
                    AccountPreview accountPreview = fxmlLoader.getController();
                    accountPreview.setUserSessionKey(userSessionKey);
                    accountPreview.setUserAccount(userAccount);
                }
        );
    }
}
