package app.controllers;

import app.actions.CheckHistoryAction;
import app.actions.DictionaryAction;
import app.dictionary.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Dictionary;
import java.util.ResourceBundle;

public class ContainerController implements Initializable {
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button favouriteButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button translateButton;
    @FXML
    private AnchorPane anchorCategory;
    @FXML
    private Button gameButton;

    private AnchorPane anchorSearchScene;
    private AnchorPane anchorTranslateScene;
    private AnchorPane anchorFavouriteScene;
    private AnchorPane anchorHistoryScene;
    private AnchorPane anchorEditScene;
    private AnchorPane anchorHomeScene;
    private AnchorPane anchorGameScene;

    private EditSceneController editSceneController;
    private FavoriteSceneController favoriteSceneController;
    private GameSceneController gameSceneController;
    private HistorySceneController historySceneController;
    private HomeSceneController homeSceneController;
    private SearchSceneController searchSceneController;
    private TranslateSceneController translateSceneController;

    private Button lastButton;
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lastButton = homeButton;
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("HomeScene.fxml"));
            anchorHomeScene = fxmlLoader.load();
            homeSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("EditScene.fxml"));
            anchorEditScene = fxmlLoader.load();
            editSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("FavoriteScene.fxml"));
            anchorFavouriteScene = fxmlLoader.load();
            favoriteSceneController = fxmlLoader.getController();
            favoriteSceneController.initData(this);
            favoriteSceneController.initSelectionList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("GameScene.fxml"));
            anchorGameScene = fxmlLoader.load();
            gameSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("HistoryScene.fxml"));
            anchorHistoryScene = fxmlLoader.load();
            historySceneController = fxmlLoader.getController();
            historySceneController.initData(this);
            historySceneController.initSelectionList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("SearchScene.fxml"));
            anchorSearchScene = fxmlLoader.load();
            searchSceneController = fxmlLoader.getController();
            searchSceneController.initData(this);
            searchSceneController.initSelectionList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("TranslateScene.fxml"));
            anchorTranslateScene = fxmlLoader.load();
            translateSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        showHomeScene();
    }

    @FXML
    private void handleAction(ActionEvent e) {
        if (e.getSource() == homeButton) {
            pressedButton(homeButton);
            showHomeScene();
        } else if (e.getSource() == searchButton) {
            pressedButton(searchButton);
            showSearchScene();
        } else if (e.getSource() == editButton) {
            pressedButton(editButton);
            showEditScene();
        } else if (e.getSource() == translateButton) {
            pressedButton(translateButton);
            showTranslateScene();
        } else if (e.getSource() == favouriteButton) {
            pressedButton(favouriteButton);
            favoriteSceneController.reload();
            showFavoriteScene();
        } else if (e.getSource() == historyButton) {
            pressedButton(historyButton);
            historySceneController.reload();
            showHistoryScene();
        } else {
            pressedButton(gameButton);
            showGameScene();
        }
    }

    private void setContentScene(AnchorPane anchorCurrent) {
        this.anchorCategory.getChildren().setAll(anchorCurrent);
    }

    private void showGameScene() {

        setContentScene(anchorGameScene);
    }

    private void showHistoryScene() {

        setContentScene(anchorHistoryScene);
    }

    private void showFavoriteScene() {

        setContentScene(anchorFavouriteScene);
    }

    private void showTranslateScene() {

        setContentScene(anchorTranslateScene);
    }

    private void showEditScene() {

        setContentScene(anchorEditScene);
    }

    private void showSearchScene() {

        setContentScene(anchorSearchScene);
    }

    private void showHomeScene() {

        setContentScene(anchorHomeScene);
    }

    public void reset() {

    }

    public DictionaryManagement getDictionaryManagement() {
        return this.dictionaryManagement;
    }

    public void pressedButton(Button currentButton) {
        lastButton.setStyle(null);
        currentButton.setStyle(
                "-fx-background-color: #1d386c;" +
                        "-fx-border-radius: 5px 5px 5px 5px;" +
                        "-fx-border-style: hidden hidden solid hidden;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-color: #FEC400;"
        );
        lastButton = currentButton;
    }
}
