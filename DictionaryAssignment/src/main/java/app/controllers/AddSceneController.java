package app.controllers;

import app.helper.GeneratePhonetics;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;

public class AddSceneController {
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
    @FXML
    private ProgressIndicator progressIndicator;
    public AddSceneController() {

    }

    public void initData(ContainerController containerController) {
        this.myController = containerController;
    }

    public void handleAddButton(ActionEvent event ) throws SQLException {
        if (event.getSource() == addButton) {
            boolean hasContent = (!txtAdd.getText().isEmpty() && !txtDescription.getText().isEmpty());
            if (hasContent) {
                if (!showConfirmationPopup("Are you sure you want to add this word to the dictionary?")) {
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
                AtomicReference<String> phonetics = new AtomicReference<>("");
                // Chạy hàm trên một luồng khác để tránh khựng lại giao diện người dùng
                var executor = Executors.newSingleThreadExecutor();
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        progressIndicator.setVisible(true);
                        phonetics.set(GeneratePhonetics.getPhonetics(word));
                        progressIndicator.setVisible(true);

                    } catch (IOException e) {
                        System.err.println("Cannot get pronunciation");
                    }
                }, executor);

                future.thenRun(() -> {
                    try {
                        progressIndicator.setVisible(false);
                        // dùng platformer do synonymlist thuộc luồng chính
                        Platform.runLater(() -> {
                            txtPronunciation.clear();
                            txtPronunciation.setText(phonetics.get());
                        });
                        executor.shutdown();
                    } catch (Exception e) {
                        System.err.println("Cannot shutdown executor");
//                        showPopup("No pronunciation found");
                    }
                });
            } else {
                showPopup("Please type the word first!");
            }
        }
    }
}
