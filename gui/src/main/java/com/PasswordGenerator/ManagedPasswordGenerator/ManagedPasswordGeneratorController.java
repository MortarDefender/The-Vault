package com.PasswordGenerator.ManagedPasswordGenerator;

import com.Context;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Slider;
import java.util.function.Consumer;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import com.PasswordGenerator.PasswordGenerator;


public class ManagedPasswordGeneratorController implements PasswordGenerator {
    @FXML private CheckBox numericCheckBox;
    @FXML private CheckBox specialCheckBox;
    @FXML private TextField passwordEditText;
    @FXML private Slider passwordLengthSlider;
    @FXML private CheckBox upperCaseLettersCheckBox;
    @FXML private CheckBox lowerCaseLettersCheckBox;

    private Stage window;
    private Consumer<String> retrieveFunction;

    public void setStage(Stage stage) { window = stage; }

    public void setRetrieveFunction(Consumer<String> function) { retrieveFunction = function; }

    @FXML
    private void initialize() { generatePassword(); }

    @FXML
    public void back(ActionEvent actionEvent) { window.close(); }

    @FXML public void onSliderDrag(MouseEvent event) { generatePassword(); }

    @FXML public void onMouseClicked(MouseEvent event) { generatePassword(); }

    @FXML public void createPassword(ActionEvent actionEvent) { generatePassword(); }

    @FXML public void addRemoveUpperLetters(ActionEvent event) { generatePassword(); }

    @FXML public void addRemoveLowerLetters(ActionEvent event) { generatePassword(); }

    @FXML public void addRemoveNumericLetters(ActionEvent event) { generatePassword(); }

    @FXML public void addRemoveSpecialLetters(ActionEvent event) { generatePassword(); }

    @FXML public void onSliderDragReleased(MouseDragEvent event) { generatePassword(); }

    private void generatePassword() {
        String generatedPassword = Context.safe.generatePassword((int) passwordLengthSlider.getValue(), upperCaseLettersCheckBox.isSelected(),
                lowerCaseLettersCheckBox.isSelected(), numericCheckBox.isSelected(), specialCheckBox.isSelected());

        passwordEditText.setText(generatedPassword);
        if (retrieveFunction != null)
            retrieveFunction.accept(generatedPassword);
    }
}
