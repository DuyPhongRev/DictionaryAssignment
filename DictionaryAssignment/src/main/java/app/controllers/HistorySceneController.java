package app.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class HistorySceneController extends ThreeController {
    public HistorySceneController() {
        super();

    }

    public void initSelectionList() {

        arrayWords = myController.getDictionaryManagement().getDicHistory().getDefault_history();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);
    }
    @Override
    @FXML
    public void handleChangeInputSearch(KeyEvent event) {
        if (event.getSource() == txtSearch) {
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                showListAction(searchText);
            } else {
                arrayWords = myController.getDictionaryManagement().getDicHistory().getDefault_history();
                SearchListView.getItems().setAll(arrayWords);
            }
        }
    }
    @FXML
    @Override
    public void handleSearchButton (ActionEvent event) {
        if (event.getSource() == searchButton) {
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                searchAction(searchText);
            }
        }
    }

    @Override
    public void searchAction(String searchText) {
        String meaning = myController.getDictionaryManagement().getDicHistory().LookUpInHist(searchText);
        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
    }

    @Override
    public void showListAction(String searchText) {
        arrayWords = myController.getDictionaryManagement().getDicHistory().getArrayRelevantWordInHist(searchText);

        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
    }

    @Override
    @FXML
    public void SelectSearchListItem (MouseEvent event) {
        if (event.getSource() == SearchListView) {
            String searchText = SearchListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                searchAction(searchText);
            }
        }
    }

    public void initData(ContainerController containerController) {
        this.myController = containerController;
        txtSearch.setPromptText("Search word...");
        txtSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String searchText = txtSearch.getText();
                if (!searchText.isEmpty()) {
                    searchAction(searchText);
                }
            }
        });
    }

    public void reload() {
        txtSearch.setText("");
        arrayWords = myController.getDictionaryManagement().getDicHistory().getHistoryList();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);

    }
}
