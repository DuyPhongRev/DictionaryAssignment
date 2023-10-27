package app.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.controlsfx.control.action.Action;
import java.sql.SQLException;
import java.util.ArrayList;

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
                System.out.println("hello");
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
                String searchText = txtSearch.getText();
                boolean checkContains = myController.getDictionaryManagement().getDictFavourite().getFavouriteList().contains(searchText);
                if (!checkContains) {
                    myController.getDictionaryManagement().addToFavourite(searchText);
                    showPopup("Added to favorite!");
                } else {
                    showPopup("This word is already in favorite!");
                }
            } else {
                showPopup("Please search a word first!");
            }
        }
    }
    @Override
    public void searchAction(String searchText) throws SQLException {
        String meaning = myController.getDictionaryManagement().searchAct(searchText);
        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
    }
}
