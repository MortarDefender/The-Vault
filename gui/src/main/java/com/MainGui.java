package com;

import javafx.stage.Stage;
import javafx.application.Application;
import com.LoginPage.LoginPageController;

public class MainGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginPageController.initLoginPage(primaryStage);
    }

    public static void main(String[] args) { launch(args); }
}
