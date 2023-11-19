package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HomeSceneController {
    @FXML
    private Button learningMoreButton, getStartButton;
    @FXML
    private Label escapeLabel;
    private WebView learningMoreWebView = new WebView();
    private ContainerController myController = null;
    private AnchorPane homeAnchorPane = null;
    private WebEngine webEngine = null;

    public void initData(ContainerController myController) {
        if (this.myController == null) {
            this.myController = myController;
        }
        if (homeAnchorPane == null) {
            this.homeAnchorPane = myController.getAnchorHomeScene();
        }
        this.webEngine = learningMoreWebView.getEngine();
        this.webEngine.load("https://github.com/DuyPhongRev/DictionaryAssignment");
        this.learningMoreWebView.setLayoutX(100);
        this.learningMoreWebView.setLayoutY(100);
        this.learningMoreWebView.setMinWidth(800);
        this.learningMoreWebView.setMinHeight(500);
    }

    public void handleLearningMoreButton(MouseEvent event) {
        homeAnchorPane.getChildren().add(learningMoreWebView);
        escapeLabel.setVisible(true);
    }

    public void handleGetStartButton(MouseEvent event) {
        myController.getTitle().setText("Search in Database");
        myController.pressedButton(myController.getSearchButton());
        myController.showSearchScene();
    }

    public void handleReturnHome(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            escapeLabel.setVisible(false);
            homeAnchorPane.getChildren().remove(learningMoreWebView);
        }
    }
}
