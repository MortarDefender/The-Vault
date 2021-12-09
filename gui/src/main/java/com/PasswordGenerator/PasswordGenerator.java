package com.PasswordGenerator;

import com.Context;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;


public interface PasswordGenerator {
    void setStage(Stage stage);
    void setRetrieveFunction(Consumer<String> retrieveFunction);

    static void initPasswordGenerator(Stage stage, Consumer<String> retrieveFunction, String contextPath) throws IOException {
        Context.createScene(stage, "Password Generator", contextPath,
                (fxmlLoader) -> {
                    PasswordGenerator passwordGeneratorController = fxmlLoader.getController();
                    passwordGeneratorController.setStage(stage);
                    passwordGeneratorController.setRetrieveFunction(retrieveFunction);
                }
        );
    }
}
