package app.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable {
    @FXML
    private Button WordleGameButton, FlappyBirdGameButton;
    private ContainerController myController;
    private WordleController wordleController;
    private FlappyBirdController flappyBirdController;
    private AnchorPane anchorFlappyBirdScene;
    private AnchorPane anchorWordleScene;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FlappyBirdScene.fxml"));
            anchorFlappyBirdScene = fxmlLoader.load();
            flappyBirdController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WordleScene.fxml"));
            anchorWordleScene = fxmlLoader.load();
            wordleController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initData(ContainerController containerController) {
        this.myController = containerController;
        wordleController.initData(this.myController);
    }

    @FXML
    public void showWordleGame(Event event) {
        if (event.getSource() == WordleGameButton) {
            this.myController.setContentScene(anchorWordleScene);
            anchorWordleScene.requestFocus();
        }
    }

    @FXML
    public void showFlappyBirdGame(Event event) {
        if (event.getSource() == FlappyBirdGameButton) {
            this.myController.setContentScene(anchorFlappyBirdScene);
            anchorFlappyBirdScene.requestFocus();
        }
    }
}