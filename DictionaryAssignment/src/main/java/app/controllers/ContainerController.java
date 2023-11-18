package app.controllers;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import app.dictionary.DictionaryManagement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
    private AnchorPane anchorCategory;
    @FXML
    private Button gameButton;
    @FXML
    private Button addButton;
    @FXML
    private Label title;

    private AnchorPane anchorSearchScene;
    private AnchorPane anchorTranslateScene;
    private AnchorPane anchorFavouriteScene;
    private AnchorPane anchorHistoryScene;
    private AnchorPane anchorEditScene;
    private AnchorPane anchorHomeScene;
    private AnchorPane anchorGameScene;
    private AnchorPane anchorAddScene;

    private EditSceneController editSceneController;
    private AddSceneController addSceneController;
    private FavoriteSceneController favoriteSceneController;
    private GameSceneController gameSceneController;
    private HistorySceneController historySceneController;
    private HomeSceneController homeSceneController;
    private SearchSceneController searchSceneController;
    private TranslateSceneController translateSceneController;
    @FXML
    private VBox menuBox;
    @FXML
    private Button menuButton;
    private Button lastButton;

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getFavouriteButton() {
        return favouriteButton;
    }

    public Button getHistoryButton() {
        return historyButton;
    }

    public Button getHomeButton() {
        return homeButton;
    }

    public Button getTranslateButton() {
        return translateButton;
    }

    public Button getGameButton() {
        return gameButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Label getTitle() {
        return title;
    }

    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();

    public AnchorPane getAnchorHomeScene() {
        return anchorHomeScene;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lastButton = homeButton;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeScene.fxml"));
            anchorHomeScene = fxmlLoader.load();
            homeSceneController = fxmlLoader.getController();
            homeSceneController.initData(this);
            System.out.println("success home scene");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddScene.fxml"));
            anchorAddScene = fxmlLoader.load();
            addSceneController = fxmlLoader.getController();
            addSceneController.initData(this);

            System.out.println("success add scene");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditScene.fxml"));
            anchorEditScene = fxmlLoader.load();
            editSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FavoriteScene.fxml"));
            anchorFavouriteScene = fxmlLoader.load();
            favoriteSceneController = fxmlLoader.getController();
            favoriteSceneController.initData(this);
            favoriteSceneController.initSelectionList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
            anchorGameScene = fxmlLoader.load();
            gameSceneController = fxmlLoader.getController();
            gameSceneController.initData(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HistoryScene.fxml"));
            anchorHistoryScene = fxmlLoader.load();
            historySceneController = fxmlLoader.getController();
            historySceneController.initData(this);
            historySceneController.initSelectionList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SearchScene.fxml"));
            anchorSearchScene = fxmlLoader.load();
            searchSceneController = fxmlLoader.getController();
            searchSceneController.initData(this);
            searchSceneController.initSelectionList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TranslateScene.fxml"));
            anchorTranslateScene = fxmlLoader.load();
            translateSceneController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuBox.setVisible(false);
        pressedButton(homeButton);
        showHomeScene();
    }

    @FXML
    private void handleAction(MouseEvent e) {
        if (e.getSource() == menuButton) {
            if (menuBox.isVisible()) {
                SlideOutLeft slideOutLeft = new SlideOutLeft(menuBox);
                slideOutLeft.play();
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0.7), g->{
                            menuBox.setVisible(false);
                        })
                );
                timeline.play();

            } else {
                menuBox.setVisible(true);
                new SlideInLeft(menuBox).play();
            }
        } else if (e.getSource() == homeButton) {
            title.setText("");
            pressedButton(homeButton);
            showHomeScene();
        } else if (e.getSource() == searchButton) {
            title.setText("Search in Database");
            pressedButton(searchButton);
            showSearchScene();
        } else if (e.getSource() == editButton) {
            title.setText("Edit a word");
            pressedButton(editButton);
            showEditScene();
        } else if (e.getSource() == addButton) {
            title.setText("Add a word");
            pressedButton(addButton);
            showAddScene();
        } else if (e.getSource() == translateButton) {
            title.setText("Online translate");
            pressedButton(translateButton);
            showTranslateScene();
        } else if (e.getSource() == favouriteButton) {
            title.setText("Favourite list");
            pressedButton(favouriteButton);
            favoriteSceneController.reload();
            showFavoriteScene();
        } else if (e.getSource() == historyButton) {
            title.setText("History list");
            pressedButton(historyButton);
            historySceneController.reload();
            showHistoryScene();
        } else {
            title.setText("");
            pressedButton(gameButton);
            showGameScene();
        }
    }

    public void setContentScene(AnchorPane anchorCurrent) {
        this.anchorCategory.getChildren().setAll(anchorCurrent);
    }

    public void showGameScene() {

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

    public void showSearchScene() {

        setContentScene(anchorSearchScene);
    }

    private void showHomeScene() {

        setContentScene(anchorHomeScene);
    }

    public void showAddScene() {
        setContentScene(anchorAddScene);
    }

    public void reset() {

    }

    public DictionaryManagement getDictionaryManagement() {
        return this.dictionaryManagement;
    }

    public void pressedButton(Button currentButton) {
        lastButton.setStyle(null);
        currentButton.setStyle(
                "-fx-background-color: linear-gradient(to right, rgba(76,205,241,0.8), rgba(22,69,189,0.8));" +
                        "-fx-border-radius: 5px 5px 5px 5px;" +
                        "-fx-border-style: hidden hidden solid hidden;" +
                        "-fx-border-radius: 10px"
        );
        lastButton = currentButton;
    }

    @FXML
    public void pressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            System.out.println("OK");
        }
    }
}
