package app.controllers;

import app.connections.TranslateVoiceAPIs;
import app.helper.GetSynonyms;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static app.controllers.PopUp.showPopup;

public class ThreeController {
    protected ContainerController myController;
    @FXML
    protected TextField txtSearch;
    @FXML
    protected ListView<String> SearchListView;
    @FXML
    protected Button searchButton;
    @FXML
    protected WebView webView;
    @FXML
    protected Button deleteButton;
    @FXML
    protected Button deleteSearchButton;
    @FXML
    protected Button soundButton;
    protected WebEngine webEngine;
    protected ArrayList<String> arrayWords = new ArrayList<>();
    protected ArrayList<String> arraySynonyms = new ArrayList<>();
    protected String currentLoadWord = "";
    @FXML
    protected ListView<String> SynonymListView = new ListView<>();
    @FXML
    protected Button favoriteButton;

    public ThreeController() {

    }
    @FXML
    public void handleSearchButton (ActionEvent event) throws SQLException, IOException {
        if (event.getSource() == searchButton) {
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                searchAction(searchText);
            }
        }
    }

    @FXML
    public void handleChangeInputSearch(KeyEvent event) {
        if (event.getSource() == txtSearch) {
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                showListAction(searchText);
            } else {
                arrayWords.clear();
                SearchListView.getItems().setAll(arrayWords);
            }
        }
    }

    @FXML
    public void handleVoice(Event event) throws IOException, JavaLayerException {
        if (event.getSource() == soundButton) {
            if (txtSearch.getText().isEmpty()) {
                PopUp.showPopup("Search text is blank!");
            } else {
//                TranslateVoiceAPIs.getAudio(txtSearch.getText(), "en");
                var executor = Executors.newSingleThreadExecutor();
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        TranslateVoiceAPIs.getAudio(txtSearch.getText(), "en");
                    } catch (IOException | JavaLayerException e) {
                        System.err.println("Cannot get audio");
                    }
                }, executor);
                future.thenRun(() -> {
                    try {
                        executor.shutdown();
                    } catch (Exception e) {
                        System.err.println("Cannot shutdown executor");
                    }
                });

            }
        }
    }

    @FXML
    public void SelectSearchListItem (MouseEvent event) throws SQLException, IOException {
        if (event.getSource() == SearchListView) {
            String searchText = SearchListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                searchAction(searchText);
                txtSearch.setText(searchText);
                arrayWords.clear();
                arrayWords.add(searchText);
                SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
            }
        }
    }

    public void showListAction(String searchText) {
        arrayWords = myController.getDictionaryManagement().getDictMain().getArrayRelevantWord(searchText);
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
    }

    public void searchAction(String searchText) throws SQLException, IOException {}

    @FXML
    public void deleteTextSearch(Event event) {
        if (event.getSource() == deleteSearchButton) {
            txtSearch.setText("");
        }

        webEngine = webView.getEngine();
        webEngine.loadContent("");
    }

    @FXML
    public void handleFavouriteButton(ActionEvent event) throws SQLException {
        if (event.getSource() == favoriteButton) {
            boolean hasContent = webView.getEngine().getDocument() != null;
            if (hasContent) {
                boolean checkContains = myController.getDictionaryManagement().getDictFavourite().getFavouriteList().contains(currentLoadWord);
                if (!checkContains) {
                    myController.getDictionaryManagement().addToFavourite(currentLoadWord);
                    showPopup("Added to favorite!");
                } else {
                    showPopup("This word is already in favorite!");
                }
            } else {
                showPopup("Please search a word first!");
            }
        }
    }
}
