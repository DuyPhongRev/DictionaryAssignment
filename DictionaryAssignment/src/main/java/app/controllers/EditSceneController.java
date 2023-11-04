package app.controllers;

import app.helper.GeneratePhonetics;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;
import java.sql.SQLException;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;

public class EditSceneController {
    private ContainerController myController;
    @FXML
    private TextField txtAdd;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtPronunciation;
    @FXML
    private TextField txtDescription;
    @FXML
    private Button addButton;
    @FXML
    private Button genPhonetics;
    public EditSceneController() {

    }

    public void initData(ContainerController containerController) {
        this.myController = containerController;
        txtAdd.setPromptText("Type word...");
        txtType.setPromptText("Type word type...");
        txtPronunciation.setPromptText("Type pronunciation...");
        txtDescription.setPromptText("Type description...");

    }

    public void handleAddButton(ActionEvent event ) throws SQLException {
        if (event.getSource() == addButton) {
            boolean hasContent = (!txtAdd.getText().isEmpty() && !txtDescription.getText().isEmpty());
            if (hasContent) {
                String tempResult = showConfirmationPopup("Are you sure you want to add this word to the dictionary?");
                if (tempResult.equals("no")) {
                    return;
                } else {
                    String pronunciation = txtPronunciation.getText();
                    String word_ = txtAdd.getText();
                    String type = txtType.getText();
                    String description = txtDescription.getText();
                    boolean ret = myController.getDictionaryManagement().getDictMain().addWordToDictionaryDatabase(word_, pronunciation, description, type);
                    if (ret) {
                        showPopup("Successfully!");
                        txtAdd.setText("");
                        txtPronunciation.setText("");
                        txtDescription.setText("");
                        txtType.setText("");
                    } else {
                        showPopup("This word is already in the dictionary!");
                    }
                }
            } else {
                showPopup("Please type the word you want to add!");
            }

        }
    }

    public void handleGenPhoneticsButton(ActionEvent event) throws IOException {
        if (event.getSource() == genPhonetics) {
            String word = txtAdd.getText();
            if (!word.isEmpty()) {
                String phonetics = GeneratePhonetics.getPhonetics(word);
                txtPronunciation.clear();
                txtPronunciation.setText(phonetics);
            } else {
                showPopup("Please type the word first!");
            }
        }
    }
}
