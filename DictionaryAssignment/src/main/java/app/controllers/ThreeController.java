package app.controllers;

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
    protected WebEngine webEngine;
    protected ArrayList<String> arrayWords;


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

    public void showPopup(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Notification!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected String showConfirmationPopup(String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmation");
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        dialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

        dialog.showAndWait();

        ButtonType result = dialog.getResult();
        if (result == yesButton) {
            dialog.close();
            showPopup("Delete successfully!");
            return "yes";
        } else {
            dialog.close();
            return "no";
        }
    }
    @FXML
    public void deleteTextSearch(Event event) {
        if (event.getSource() == deleteSearchButton) {
            txtSearch.setText("");
            showListAction("");
        }
    }
}
