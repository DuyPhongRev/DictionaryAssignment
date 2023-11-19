package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.SQLException;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;

public class AddSceneController extends UpdateWordController{
    @FXML
    private Button addButton;

    public AddSceneController() {
        super();
    }

    public void handleAddButton(ActionEvent event ) throws SQLException {
        if (event.getSource() == addButton) {
            boolean hasContent = (!txtWord.getText().isEmpty() && !txtDescription.getText().isEmpty());
            if (hasContent) {
                if (showConfirmationPopup("Are you sure you want to add this word to the dictionary?")) {
                    String pronunciation = txtPronunciation.getText();
                    String word_ = txtWord.getText();
                    String type = txtType.getText();
                    String description = txtDescription.getText();
                    boolean ret = myController.getDictionaryManagement().getDictMain().addWordToDictionaryDatabase(word_, pronunciation, description, type);
                    if (ret) {
                        showPopup("Successfully!");
                        txtWord.setText("");
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

}
