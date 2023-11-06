package app.controllers;

import app.connections.TranslateVoiceAPIs;
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
    protected ArrayList<String> arrayWords;
    protected String currentLoadWord = "";

    public ThreeController() {

    }
    @FXML
    public void handleSearchButton (ActionEvent event) throws SQLException {
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
                TranslateVoiceAPIs.getAudio(txtSearch.getText(), "en");
            }
        }
    }

    @FXML
    public void SelectSearchListItem (MouseEvent event) throws SQLException {
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

    public void searchAction(String searchText) throws SQLException {}

    @FXML
    public void deleteTextSearch(Event event) {
        if (event.getSource() == deleteSearchButton) {
            txtSearch.setText("");
        }
    }
}
