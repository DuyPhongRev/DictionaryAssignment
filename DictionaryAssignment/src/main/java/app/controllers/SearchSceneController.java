package app.controllers;

import app.connections.TranslateVoiceAPIs;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;

public class SearchSceneController extends ThreeController {
    @FXML
    private Button favoriteButton;
    public SearchSceneController() {
        super();
    }
    ArrayList<String> arrayWordsDefault = new ArrayList<>();
    @Override
    @FXML
    public void SelectSearchListItem (MouseEvent event) throws SQLException {
        if (event.getSource() == SearchListView) {
            String searchText = SearchListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                searchAction(searchText);
                txtSearch.setText(searchText);
                ArrayList<String> tmp = new ArrayList<>();
                tmp.add(searchText);
                SearchListView.setItems(FXCollections.observableArrayList(tmp));
            }
        }
    }

    @Override
    public void handleChangeInputSearch(KeyEvent event) {
        if (event.getSource() == txtSearch) {
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                showListAction(searchText);
            } else {
                SearchListView.setItems(FXCollections.observableArrayList(arrayWordsDefault));
            }
        }
    }

    @Override
    public void showListAction(String searchText) {
        arrayWords = myController.getDictionaryManagement().getDictMain().getArrayRelevantWord(searchText);

        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
    }

    public void initSelectionList() {
        arrayWords = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);
        arrayWordsDefault = arrayWords;
    }
    public void initData(ContainerController containerController) {
        this.myController = containerController;
        txtSearch.setPromptText("Search word...");

        txtSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String searchText = txtSearch.getText();
                if (!searchText.isEmpty()) {
                    try {
                        searchAction(searchText);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
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

    public void handleDeleteButton(ActionEvent event) throws SQLException {
        if (event.getSource() == deleteButton) {
            boolean hasContent = webView.getEngine().getDocument() != null;
            if (hasContent) {
                if (showConfirmationPopup("Are you sure you want to delete this word from the database system?")) {
                    myController.getDictionaryManagement().deleteAct(currentLoadWord);
                    showPopup("Successfully!");
                    reload();
                    webEngine = webView.getEngine();
                    webEngine.loadContent("This word has been deleted from the database system!");
                }
            } else {
                showPopup("Please Select the word first");
            }

        }
    }
    @Override
    public void searchAction(String searchText) throws SQLException {
        String meaning = myController.getDictionaryManagement().searchAct(searchText);
        currentLoadWord = searchText;
        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
        webEngine.setUserStyleSheetLocation(getClass().getResource("webview.css").toString());
    }

    @Override
    public void deleteTextSearch(Event event) {
        if (event.getSource() == deleteSearchButton) {
            txtSearch.setText("");
            arrayWords = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
            SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
            SearchListView.getItems().setAll(arrayWords);
        }
    }
    public void reload() {
        txtSearch.setText("");
        arrayWords = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);
   }
}
