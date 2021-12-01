package com.NewAccountPage;

import com.Context;
import javafx.stage.Stage;

import java.io.IOException;

public interface AccountGenerator {
    void setStage(Stage stage);
    void setUserSessionKey(String userSessionKey);

    static void initAccountGenerator(Stage stage, String userSessionKey, String contextPath) throws IOException {
        Context.createScene(stage, "Create Account", contextPath,
                (fxmlLoader) -> {
                    AccountGenerator createAccountController = fxmlLoader.getController();
                    createAccountController.setStage(stage);
                    createAccountController.setUserSessionKey(userSessionKey);
                }
        );
    }
}
