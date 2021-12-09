package com.PasswordGenerator.SimplePasswordGenerator;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.util.function.Consumer;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import com.PasswordGenerator.PasswordGenerator;


public class SimplePasswordGeneratorController implements PasswordGenerator {
    @FXML private TextField passwordEditText;
    @FXML private Slider passwordLengthSlider;

    private Stage window;
    private Consumer<String> retrieveFunction;

    public void setStage(Stage stage) { window = stage; }

    public void setRetrieveFunction(Consumer<String> function) { retrieveFunction = function; }

    @FXML
    private void initialize() { generatePassword(); }

    @FXML
    public void submitPassword(ActionEvent actionEvent) { window.close(); }

    @FXML
    public void manualOperation(MouseEvent event) throws IOException {
        PasswordGenerator.initPasswordGenerator(window, retrieveFunction, Context.ManagedPasswordGeneratorUrl);
    }

    @FXML public void onSliderDrag(MouseEvent event) { generatePassword(); }

    @FXML public void onMouseClicked(MouseEvent event) { generatePassword(); }

    @FXML public void onSliderDragReleased(MouseDragEvent event) { generatePassword(); }

    private void generatePassword() {
        String generatedPassword = Context.safe.generatePassword((int) passwordLengthSlider.getValue());

        passwordEditText.setText(generatedPassword);
        if (retrieveFunction != null)
            retrieveFunction.accept(generatedPassword);
    }
}
