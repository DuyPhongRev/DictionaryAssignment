package app.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;

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
        currentLoadWord = searchText;

        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
        webEngine.setUserStyleSheetLocation(getClass().getResource("webview.css").toString());

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

    public void handleDeleteButton(ActionEvent event) throws SQLException {
        if (event.getSource() == deleteButton) {
            boolean hasContent = webView.getEngine().getDocument() != null;
            if (hasContent) {
                String tempResult = showConfirmationPopup("Are you sure you want to delete this word from the history list?");
                if (tempResult.equals("no")) {
                    return;
                } else {
                    showPopup("Successfully!");
                    myController.getDictionaryManagement().getDicHistory().deleteWordFromHistoryDatabase(currentLoadWord);
                    reload();
                    webEngine = webView.getEngine();
                    webEngine.loadContent("This word has been deleted from the history!");
                }
            } else {
                showPopup("Please Select the word first");
            }

        }
    }

    public void reload() {
        txtSearch.setText("");
        arrayWords = myController.getDictionaryManagement().getDicHistory().getHistoryList();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);

    }
}
