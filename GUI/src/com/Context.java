package com;

import java.awt.*;
import java.net.URL;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import Objects.interfaces.Safe;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import Objects.dto.PasswordStrength;
import javafx.scene.control.TableView;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;


public class Context {

    @FunctionalInterface
    public interface ControllerInitialize {
        void Invoke(FXMLLoader fxmlLoader);
    }


    public static Safe safe = new Vault();
    public static final String iconUrl = "/com/VaultPage/safe.png";
    public static final String loginUrl = "/com/LoginPage/loginPage.fxml";
    public static final String vaultUrl = "/com/VaultPage/vaultPage.fxml";

    public static final String showAccountUrl = "/com/AccountPage/showAccount.fxml";
    public static final String showAccountsUrl = "/com/AccountsPage/showAccounts.fxml";
    public static final String showSecureNoteUrl = "/com/AccountPage/SecureNotes/SecureNote.fxml";
    public static final String showSecurityKeysUrl = "/com/AccountPage/SecurityKeys/SecurityKeys.fxml";
    public static final String showDefaultAccountUrl = "/com/AccountPage/DefaultAccount/DefaultAccount.fxml";
    public static final String showPaymentAccountUrl = "/com/AccountPage/PaymentAccount/PaymentAccount.fxml";

    public static final String newAccountUrl = "/com/NewAccountPage/newAccount.fxml";
    public static final String newSecureNoteUrl = "/com/NewAccountPage/SecureNotes/SecureNote.fxml";
    public static final String newSecurityKeysUrl = "/com/NewAccountPage/SecurityKeys/SecurityKeys.fxml";
    public static final String newDefaultAccountUrl = "/com/NewAccountPage/DefaultAccount/DefaultAccount.fxml";
    public static final String newPaymentAccountUrl = "/com/NewAccountPage/PaymentAccount/PaymentAccount.fxml";

    public static final String searchAccountsUrl = "/com/SearchAccounts/searchAccounts.fxml";

    public static final String ManagedPasswordGeneratorUrl = "/com/PasswordGenerator/ManagedPasswordGenerator/passwordGenerator.fxml";
    public static final String SimplePasswordGeneratorUrl = "/com/PasswordGenerator/SimplePasswordGenerator/SimplePasswordGenerator.fxml";

    public static Color getColor(PasswordStrength strength) {
        switch (strength) {
            case Bad:
                return Color.RED;
            case Good:
                return Color.YELLOW;
            case VeryGood:
                return Color.GREEN;
            default:
                return Color.BLACK;
        }
    }

    public static void createScene(Stage window, String title, String contextPathUrl, ControllerInitialize controllerInitialize) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL mainUrl = Context.class.getResource(contextPathUrl);
        fxmlLoader.setLocation(mainUrl);
        Parent root = fxmlLoader.load(mainUrl.openStream());
        Scene scene = new Scene(root);

        controllerInitialize.Invoke(fxmlLoader);

        window.setTitle(title);
        window.getIcons().add(new Image(Context.iconUrl));
        window.setScene(scene);
        window.show();
    }

    public static DoubleBinding bindStageWidth(TableView<?> table, int size) { return bindStageWidth(table, size, 100); }
    public static DoubleBinding bindStageWidth(TableView<?> table, int size, double maxSize) {
        return (new DoubleBinding() {
            {
                bind(table.widthProperty());
            }
            @Override
            protected double computeValue() {
                double value = table.widthProperty().getValue();
                return Math.max((value / size), maxSize);
            }
        });
    }

    public static void maskUnmaskPassword(BooleanProperty visibilityProperty, FontAwesomeIconView visibilityAwesomeIcon) {
        visibilityProperty.setValue(!visibilityProperty.getValue());

        if (visibilityProperty.getValue()) {
            visibilityAwesomeIcon.setGlyphName(FontAwesomeIcon.EYE.name());
        } else {
            visibilityAwesomeIcon.setGlyphName(FontAwesomeIcon.EYE_SLASH.name());
        }
    }

    public static void openUrl(String baseUrl) {
        try {
            if (!baseUrl.startsWith("https://"))
                baseUrl = "https://" + baseUrl;

            Desktop.getDesktop().browse(new URL(baseUrl).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
