package app.controllers;

import app.helper.FileService;
import app.helper.ImageRequestServer;

import javafx.application.Platform;
import javafx.scene.control.*;

import app.connections.TranslateTextAPIs;
import app.connections.TranslateVoiceAPIs;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javazoom.jl.decoder.JavaLayerException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class TranslateSceneController implements Initializable {
    @FXML
    private Button SrcSoundButton, DesSoundButton, tranButton, textRecognizeButton;
    @FXML
    private ChoiceBox<LANGUAGE> SrcLangChoiceBox, DesLangChoiceBox;
    @FXML
    private TextArea SrcTextArea, DesTextArea;
    private String currentSrcLang = "en";
    private String currentDesLang = "vi";

    @FXML
    private Text waitText;

    public ProgressIndicator progressIndicator;

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
//            recognizeText();
            processImage();
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

    public void processImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image orignalImage = new Image(selectedFile.toURI().toString());
                // return the working directory
                String workingDir = System.getProperty("user.dir");
                // save the image to the working directory
                String destinationPath = workingDir + "/input.png";
                FileService.saveImageToFile(selectedFile, destinationPath);

                ImageView ClientImageView = new ImageView(orignalImage);
                ClientImageView.setFitWidth(400);
                ClientImageView.setFitHeight(400);
                ClientImageView.setPreserveRatio(true);
                ClientImageView.setSmooth(true);

                StackPane dialogContent = new StackPane(ClientImageView);
                ButtonType sendButton = new ButtonType("OK");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Preview your image");
                dialog.getDialogPane().setContent(dialogContent);
                dialog.getDialogPane().getButtonTypes().add(sendButton);
                dialog.initOwner(app.App.AppStage);
                dialog.showAndWait();
                progressIndicator.setVisible(true);
                waitText.setVisible(true);

                var executor = Executors.newSingleThreadExecutor();
                CompletableFuture future = CompletableFuture.runAsync(() -> {
                    try {
                        ImageRequestServer.sendToServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, executor);
                future.thenRun(() -> {
                    try {
                        Platform.runLater(() -> {
                            File image = new File("result.png");
                            Image resultImage = null;
                            try {
                                resultImage = new Image(image.toURI().toURL().toString());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            ImageView resultImageView = new ImageView(resultImage);
                            resultImageView.setFitWidth(400);
                            resultImageView.setFitHeight(400);
                            resultImageView.setPreserveRatio(true);
                            resultImageView.setSmooth(true);

                            HBox hbox = new HBox(ClientImageView, resultImageView);

                            hbox.setSpacing(20);
                            Dialog<ButtonType> resultDialog = new Dialog<>();
                            resultDialog.setTitle("Result");
                            resultDialog.initOwner(app.App.AppStage);
                            resultDialog.getDialogPane().setContent(hbox);
                            resultDialog.getDialogPane().getButtonTypes().add(sendButton);

                            waitText.setVisible(false);
                            progressIndicator.setVisible(false);
                            resultDialog.showAndWait();
//                            hbox.setAlignment(Pos.CENTER);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
//                e.printStackTrace();
            }

        }
    }
}
