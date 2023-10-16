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
    private Button SrcSound, DesSound;
    @FXML
    private ChoiceBox<LANGUAGE> SrcLang, DesLang;
    @FXML
    private TextArea SrcText, DesText;
    private String currentSrcLang = "vi";
    private String currentDesLang = "vi";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SrcLang.getItems().addAll(LANGUAGE.values());
        DesLang.getItems().addAll(LANGUAGE.values());
    }

    public void getCurrentSrcLang() {
        if (SrcLang.getValue() != null) {
            currentSrcLang = SrcLang.getValue().key;
        }
    }

    public void getCurrentDesLang() {
        if (DesLang.getValue() != null) {
            currentDesLang = DesLang.getValue().key;
        }
    }

    @FXML
    public void handleEvent(Event event) throws IOException, JavaLayerException {
            getCurrentSrcLang();
            getCurrentDesLang();
        if (event.getSource().equals(SrcText) && !SrcText.getText().equals("")) {
            String text = SrcText.getText();
            if (currentDesLang.equals(currentSrcLang)) {
                DesText.setText(text);
            } else {
                DesText.setText(TranslateTextAPIs.translate(text, currentSrcLang, currentDesLang));
            }
        } else if (event.getSource().equals(SrcSound) && !SrcText.getText().equals("")) {
            TranslateVoiceAPIs.getAudio(SrcText.getText(), currentSrcLang);
        } else if (event.getSource().equals(DesSound) && !DesText.getText().equals("")) {
            TranslateVoiceAPIs.getAudio(DesText.getText(), currentDesLang);
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
