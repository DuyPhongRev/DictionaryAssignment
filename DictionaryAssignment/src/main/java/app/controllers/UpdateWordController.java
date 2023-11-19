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

public class UpdateWordController {
    protected ContainerController myController;
    @FXML
    protected TextField txtType;
    @FXML
    protected TextField txtPronunciation;
    @FXML
    protected TextField txtDescription;
    @FXML
    protected Button genPhonetics;
    @FXML
    protected ProgressIndicator progressIndicator;
    protected String tempPhonetics;
    @FXML
    protected TextField txtWord;
    public UpdateWordController() {

    }

    public void initData(ContainerController containerController) {
        this.myController = containerController;
    }


    public void handleGenPhoneticsButton(ActionEvent event) throws IOException {
        if (event.getSource() == genPhonetics) {
            String word = txtWord.getText();
            if (!word.isEmpty()) {
                var executor = Executors.newSingleThreadExecutor();
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        progressIndicator.setVisible(true);
                        tempPhonetics = GeneratePhonetics.getPhonetics(word);
                        System.out.println(tempPhonetics);

                    } catch (IOException e) {
                        System.err.println("Cannot get pronunciation");
                    }
                }, executor);

                future.thenRun(() -> {
                    try {
                        progressIndicator.setVisible(false);
                        Platform.runLater(() -> {
                            txtPronunciation.clear();
                            txtPronunciation.setText(tempPhonetics);
                        });
                        executor.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Cannot shutdown executor");
                    }
                });
            } else {
                showPopup("Please type the word first!");
            }
        }
    }
}
