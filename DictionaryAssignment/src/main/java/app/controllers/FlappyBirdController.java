package app.controllers;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class FlappyBirdController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    ImageView bird;
    @FXML
    private Label rightCloud, wrongCloud, scoreLabel, meaningLabel;
    @FXML
    private ImageView layer1Background, layer2Background, layer3Background, layer4Background, layer5Background;
    @FXML
    private ImageView layer1Background1, layer2Background1, layer3Background1, layer4Background1, layer5Background1;
    @FXML
    private ImageView startImage, gameOverImage;
    private AnimationTimer gameLoop;
    private double yDelta = 0.05;
    private double time = 0;
    private int jumpHeight = 50;
    private final int WIDTH = 1333;
    private String rightWord = "CANDY";
    private String wrongWord = "CAKE";
    private String meaning = "Kẹo";
    private int score = 0;
    private boolean checkPoint = true;
    private boolean isGameStarted = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> updateBackground()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        gameLoop.start();
    }


    private void update() {
        if (isGameStarted) {
            time ++;
            moveBird(yDelta * time);
            moveCloud();
            if (rightCloud.getBoundsInParent().intersects(bird.getBoundsInParent())) {
                chooseRightAnswer();
            }
            if (rightCloud.getLayoutX() < -200) {
                generateCloud();
            }
            if(isBirdDead()){
                gameOverImage.setVisible(true);
                rightCloud.setLayoutX(-WIDTH);
                wrongCloud.setLayoutX(-WIDTH);
                gameLoop.stop();
            }
        }
    }

    @FXML
    public void pressed(KeyEvent event) {
        if(event.getCode() == KeyCode.SPACE){
            if (isBirdDead()) {
                gameLoop.start();
                reset();
            } else if (isGameStarted) {
                fly();
            } else {
                isGameStarted = true;
                startGame();
            }
        }
    }

    public void startGame() {
        startImage.setVisible(false);
        generateCloud();
        meaningLabel.setText(meaning);
        scoreLabel.setText(String.valueOf(score));
    }

    private void fly() {
        bird.setRotate(325);
        if(bird.getLayoutY() <= jumpHeight){
            moveBird(-bird.getLayoutY());
            time = 0;
            return;
        }
        moveBird(-jumpHeight);
        time = 0;
    }

    private void chooseRightAnswer() {
        rightCloud.setVisible(false);
        if (checkPoint) {
            checkPoint = false;
            score++;
            scoreLabel.setText(String.valueOf(score));
        }
    }

    private void generateCloud() {
        checkPoint = true;
        meaningLabel.setText(meaning);
        rightCloud.setVisible(true);
        rightCloud.setLayoutX(WIDTH);
        rightCloud.setText(rightWord);
        wrongCloud.setLayoutX(WIDTH);
        wrongCloud.setText(wrongWord);
        if (Math.ceil(Math.random() * 100) % 2 == 0) {
            wrongCloud.setLayoutY(110);
            rightCloud.setLayoutY(260);
        } else {
            wrongCloud.setLayoutY(260);
            rightCloud.setLayoutY(110);
        }
    }

    private void moveCloud() {
        rightCloud.setLayoutX(rightCloud.getLayoutX() - 0.8);
        wrongCloud.setLayoutX(wrongCloud.getLayoutX() - 0.8);
    }

    private void moveBird(double positionChange){
        bird.setRotate(bird.getRotate() + 0.012 * time);
        bird.setLayoutY(bird.getLayoutY() + positionChange);
    }

    private boolean isBirdDead(){
        double birdY = bird.getLayoutY();
        if (wrongCloud.getBoundsInParent().intersects(bird.getBoundsInParent()) && checkPoint) {
            return true;
        }
        if (rightCloud.getLayoutX() + 140 < bird.getLayoutX() && checkPoint) {
            return true;
        }
        return birdY >= anchorPane.getHeight();
    }

    private void reset(){
        isGameStarted = false;
        startImage.setVisible(true);
        gameOverImage.setVisible(false);
        score = 0;
        scoreLabel.setText(String.valueOf(score));
        generateCloud();
        bird.setLayoutY(200);
        bird.setRotate(0);
        time = 0;
    }

    private void updateBackground() {
        updateBackgroundPosition(layer1Background, 0.14);
        updateBackgroundPosition(layer2Background, 0.12);
        updateBackgroundPosition(layer3Background, 0.4);
        updateBackgroundPosition(layer4Background, 0.16);
        updateBackgroundPosition(layer5Background, 0.1);
        updateBackgroundPosition(layer1Background1, 0.14);
        updateBackgroundPosition(layer2Background1, 0.12);
        updateBackgroundPosition(layer3Background1, 0.4);
        updateBackgroundPosition(layer4Background1, 0.16);
        updateBackgroundPosition(layer5Background1, 0.1);
    }

    private void updateBackgroundPosition(ImageView imageView, double speed) {
        double currentX = imageView.getLayoutX();
        double newX = currentX - speed;
        if (newX + WIDTH <= 0) {
            newX += WIDTH * 2;
        }
        imageView.setLayoutX(newX);
    }
}
