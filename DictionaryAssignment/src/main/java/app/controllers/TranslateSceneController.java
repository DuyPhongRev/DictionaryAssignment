package app.controllers;

import app.connections.TranslateTextAPIs;
import app.connections.TranslateVoiceAPIs;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslateSceneController implements Initializable {
    @FXML
    private Button SrcSoundButton, DesSoundButton;
    @FXML
    private ChoiceBox<LANGUAGE> SrcLangChoiceBox, DesLangChoiceBox;
    @FXML
    private TextArea SrcTextArea, DesTextArea;
    private String currentSrcLang = "en";
    private String currentDesLang = "vi";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SrcLangChoiceBox.getItems().addAll(LANGUAGE.values());
        DesLangChoiceBox.getItems().addAll(LANGUAGE.values());
        SrcTextArea.setWrapText(true);
        DesTextArea.setWrapText(true);
    }

    public void getCurrentSrcLang() {
        if (SrcLangChoiceBox.getValue() != null) {
            currentSrcLang = SrcLangChoiceBox.getValue().getKey();
        }
    }

    public void getCurrentDesLang() {
        if (DesLangChoiceBox.getValue() != null) {
            currentDesLang = DesLangChoiceBox.getValue().getKey();
        }
    }

    @FXML
    public void handleEvent(Event event) throws IOException, JavaLayerException {
        if (event.getSource().equals(SrcTextArea)) {
            String text = SrcTextArea.getText();
            if (currentDesLang.equals(currentSrcLang)) {
                DesTextArea.setText(text);
            } else {
                DesTextArea.setText(TranslateTextAPIs.translate(text, currentSrcLang, currentDesLang));
            }
        } else if (event.getSource().equals(SrcSoundButton) && !SrcTextArea.getText().equals("")) {
            TranslateVoiceAPIs.getAudio(SrcTextArea.getText(), currentSrcLang);
        } else if (event.getSource().equals(DesSoundButton) && !DesTextArea.getText().equals("")) {
            TranslateVoiceAPIs.getAudio(DesTextArea.getText(), currentDesLang);
        }
    }

    public enum LANGUAGE {
        ENGLISH("en"),
        CHINESE("zh"),
        FRENCH("fr"),
        VIETNAMESE("vi");

        private final String key;

        LANGUAGE(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
