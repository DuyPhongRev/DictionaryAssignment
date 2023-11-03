package app.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class FavoriteSceneController extends ThreeController {
    public FavoriteSceneController() {
        super();
    }

    public void initSelectionList() {

        arrayWords = myController.getDictionaryManagement().getDictFavourite().getDefault_favourite();
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
                arrayWords = myController.getDictionaryManagement().getDictFavourite().getFavouriteList();
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
    @FXML
    public void SelectSearchListItem (MouseEvent event) {
        if (event.getSource() == SearchListView) {
            String searchText = SearchListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                System.out.println("selected favourite list item");
                searchAction(searchText);
            }
        }
    }

    @Override
    public void showListAction(String searchText) {
        arrayWords = myController.getDictionaryManagement().getDictFavourite().getArrayRelevantWordInFavourite(searchText);

        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
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

    @Override
    public void searchAction(String searchText) {
        String meaning = myController.getDictionaryManagement().getDictFavourite().LookUpInFavourite(searchText);
        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
        webEngine.setUserStyleSheetLocation(getClass().getResource("webview.css").toString());
    }

    public void reload() {
        txtSearch.setText("");
        arrayWords = myController.getDictionaryManagement().getDictFavourite().getFavouriteList();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);
    }
}
