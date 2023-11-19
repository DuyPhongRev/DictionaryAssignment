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
    private Button learningMoreButton, getStartButton, leaveButton;
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
        this.learningMoreWebView.setLayoutX(0);
        this.learningMoreWebView.setLayoutY(0);
        this.learningMoreWebView.setMinWidth(1000);
        this.learningMoreWebView.setMinHeight(750);
    }

    public void handleLearningMoreButton(MouseEvent event) {
        homeAnchorPane.getChildren().add(learningMoreWebView);
        this.myController.setMenuButtonInvisible();
        leaveButton.toFront();
        leaveButton.setVisible(true);
    }

    public void handleGetStartButton(MouseEvent event) {
        myController.getTitle().setText("Search in Database");
        myController.pressedButton(myController.getSearchButton());
        myController.showSearchScene();
    }

    public void handleLeaveButton(MouseEvent event) {
        leaveButton.setVisible(false);
        this.myController.setMenuButtonVisible();
        homeAnchorPane.getChildren().remove(learningMoreWebView);
    }
}
