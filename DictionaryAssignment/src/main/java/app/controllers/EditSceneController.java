package app.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;


public class EditSceneController extends UpdateWordController {
    @FXML
    private TextField txtOld;
    @FXML
    private Button editButton;
    public EditSceneController() {
        super();
    }

    @FXML
    public void handleEditButton(ActionEvent event) throws SQLException {
        if (event.getSource() == editButton) {
            boolean hasContent = (!txtOld.getText().isEmpty());
            if (hasContent) {
                if (showConfirmationPopup("Are you sure you want to edit this word in the dictionary?")) {
                    // check if the word is in the dictionary
                    boolean checkValid  = myController.getDictionaryManagement().getDictMain().checkContains(txtOld.getText());
                    if (!checkValid) {
                        showPopup("Cannot find the Old Word in the dictionary!");
                        return;
                    }

                    String oldWord = txtOld.getText();
                    String newWord = txtWord.getText();
                    String pronunciation = txtPronunciation.getText();
                    String type = txtType.getText();
                    String description = txtDescription.getText();
                    myController.getDictionaryManagement().editAct(oldWord, newWord, type, pronunciation, description);

                    showPopup("Successfully!");
                    txtWord.setText("");
                    txtPronunciation.setText("");
                    txtDescription.setText("");
                    txtType.setText("");
                    txtOld.setText("");
                }
            } else {
                showPopup("Please type the word you want to edit!");
            }
        }
    }

}
