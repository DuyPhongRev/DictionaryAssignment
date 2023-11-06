package app.controllers;

import app.connections.TranslateTextAPIs;
import app.connections.TranslateVoiceAPIs;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javazoom.jl.decoder.JavaLayerException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslateSceneController implements Initializable {
    @FXML
    private Button SrcSoundButton, DesSoundButton, tranButton, textRecognizeButton;
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
        SrcLangChoiceBox.setValue(LANGUAGE.ENGLISH);
        DesLangChoiceBox.setValue(LANGUAGE.VIETNAMESE);
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
    public void handleEvent(Event event) throws IOException, JavaLayerException, TesseractException {
        if (event.getSource().equals(SrcTextArea)) {
            String text = SrcTextArea.getText();
            if (currentDesLang.equals(currentSrcLang)) {
                DesTextArea.setText(text);
            } else {
                DesTextArea.setText(TranslateTextAPIs.translate(text, currentSrcLang, currentDesLang));
            }
        }
    }

    @FXML
    public void handleTranslateButton(Event event) throws IOException {
        if (event.getSource().equals(tranButton)) {
            getCurrentDesLang();
            getCurrentSrcLang();
            String text = SrcTextArea.getText();
            DesTextArea.setText(TranslateTextAPIs.translate(text, currentSrcLang, currentDesLang));
        }
    }

    @FXML
    public void handleTextRecognition(Event event) throws TesseractException, IOException {
        if (event.getSource().equals(textRecognizeButton)) {
            recognizeText();
        }
    }

    @FXML
    public void handleVoiceButton(Event event) throws IOException, JavaLayerException {
        if (event.getSource().equals(SrcSoundButton)) {
            if (SrcTextArea.getText().equals("")) {
                PopUp.showPopup("Source text is bank!");
            } else {
                TranslateVoiceAPIs.getAudio(SrcTextArea.getText(), currentSrcLang);
            }
        } else if (event.getSource().equals(DesSoundButton) && !DesTextArea.getText().equals("")) {
            if (SrcTextArea.getText().equals("")) {
                PopUp.showPopup("Source text is bank!");
            } else {
                TranslateVoiceAPIs.getAudio(DesTextArea.getText(), currentDesLang);
            }
        }
    }

    public void recognizeText() throws TesseractException, IOException {
        FileChooser fileChooser = new FileChooser();
        File respond = fileChooser.showOpenDialog(null);
        if (respond != null) {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("DictionaryAssignment\\src\\main\\resources\\app\\tessdata");
            String text = tesseract.doOCR(respond).replace("\n", " ");
            SrcTextArea.setText(text);
            SrcLangChoiceBox.setValue(LANGUAGE.AUTO);
            getCurrentDesLang();
            getCurrentSrcLang();
            DesTextArea.setText(TranslateTextAPIs.translate(text, currentSrcLang, currentDesLang));
        }
    }

    public enum LANGUAGE {
        ENGLISH("en"),
        CHINESE("zh"),
        FRENCH("fr"),
        VIETNAMESE("vi"),
        AUTO("auto");

        private final String key;

        LANGUAGE(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
