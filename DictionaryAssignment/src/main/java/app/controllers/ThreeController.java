package app.controllers;
import app.connections.databaseConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.sql.Connection;
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
    protected WebEngine webEngine;
    protected ArrayList<String> arrayWords;

    public ThreeController() {

    }
    @FXML
    public void handleSearchButton (ActionEvent event) {
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
    public void SelectSearchListItem (MouseEvent event) throws SQLException {
        if (event.getSource() == SearchListView) {
            String searchText = SearchListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
                searchAction(searchText);
                txtSearch.setText(searchText);
                arrayWords.clear();
                arrayWords.add(searchText);
            }
        }
    }

    public void showListAction(String searchText) {
        arrayWords = myController.getDictionaryAction().getStringFoundWord(searchText);

        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
    }

    public void searchAction(String searchText) {
        String meaning = myController.getDictionaryAction().getMyDict().LookUp(searchText);
        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
    }
}
