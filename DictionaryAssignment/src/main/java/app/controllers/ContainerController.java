package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
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
    private Label categoryLabel;
    @FXML
    private AnchorPane anchorCategory;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("SearchScene.fxml"));
            anchorSearchScene = fxmlLoader.load();
            searchSceneController = fxmlLoader.getController();
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
    }

    @FXML
    private void handleAction(ActionEvent e) {
        if (e.getSource() == homeButton) {
            showHomeScene();
        } else if (e.getSource() == searchButton) {
            showSearchScene();
        } else if (e.getSource() == editButton) {
            showEditScene();
        } else if (e.getSource() == translateButton) {
            showTranslateScene();
        } else if (e.getSource() == favouriteButton) {
            showFavoriteScene();
        } else if (e.getSource() == historyButton) {
            showHistoryScene();
        } else {
            showGameScene();
        }
    }

    private void setContentScene(AnchorPane anchorCurrent) {
        this.anchorCategory.getChildren().setAll(anchorCurrent);
    }

    private void showGameScene() {
        categoryLabel.setText("GAME");
        setContentScene(anchorGameScene);
    }

    private void showHistoryScene() {
        categoryLabel.setText("HISTORY");
        setContentScene(anchorHistoryScene);
    }

    private void showFavoriteScene() {
        categoryLabel.setText("FAVORITE");
        setContentScene(anchorFavouriteScene);
    }

    private void showTranslateScene() {
        categoryLabel.setText("TRANSLATE");
        setContentScene(anchorTranslateScene);
    }

    private void showEditScene() {
        categoryLabel.setText("EDIT");
        setContentScene(anchorEditScene);
    }

    private void showSearchScene() {
        categoryLabel.setText("SEARCH");
        setContentScene(anchorSearchScene);
    }

    private void showHomeScene() {
        categoryLabel.setText("HOME");
        setContentScene(anchorHomeScene);
    }
}
