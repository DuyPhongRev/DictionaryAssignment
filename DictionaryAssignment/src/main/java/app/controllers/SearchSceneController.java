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

import java.sql.SQLException;
import java.util.ArrayList;

public class SearchSceneController extends ThreeController {
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
        String meaning = myController.getDictionaryAction().getMyDict().LookUp(searchText);
        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
//        myController.getCheckHistoryAction().addToHistory(searchText);
    }
}
