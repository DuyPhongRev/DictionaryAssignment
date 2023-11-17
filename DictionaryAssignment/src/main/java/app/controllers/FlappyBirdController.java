package app.controllers;

import app.App;
import app.connections.TranslateTextAPIs;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FlappyBirdController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView bird;
    @FXML
    private Button leaveButton;
    @FXML
    private Label correctCloud, wrongCloud, meaningLabel;
    @FXML
    private ImageView layer1Background, layer2Background, layer3Background, layer4Background, layer5Background;
    @FXML
    private ImageView layer1Background1, layer2Background1, layer3Background1, layer4Background1, layer5Background1;
    @FXML
    private ImageView startImage, gameOverImage, firstScoreImage, secondScoreImage, correctCloudImage, wrongCloudImage;
    private AnimationTimer gameLoop;
    private double yDelta = 0.05;
    private double time = 0;
    private int jumpHeight = 50;
    private final int WIDTH = 1333;
    private int score = 0;
    private boolean checkPoint = true;
    private boolean isGameStarted = false;
    private ContainerController myController;
    private ArrayList<String> correctWordList = new ArrayList<>();
    private ArrayList<String> wrongWordList = new ArrayList<>();

    private ArrayList<String> meaningWordList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> updateBackground()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        gameLoop.start();
    }

    public void initData(ContainerController containerController) throws IOException {
        this.myController = containerController;
        int countWords = 0;
        ArrayList<String> sourceList = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
        for (String s : sourceList) {
            if (s.length() >= 3 && s.length() <= 8 && !s.contains(" ") && !s.contains("-") && countWords <= 10 && Math.random() < 0.2) {
                countWords++;
                if (Math.random() < 0.5) {
                    correctWordList.add(s.toUpperCase());
                    meaningWordList.add(TranslateTextAPIs.translate(s, "en", "vi").toUpperCase());
                } else {
                    wrongWordList.add(s.toUpperCase());
                }
            }
        }
    }


    private void update() throws IOException {
        if (isGameStarted) {
            time ++;
            moveBird(yDelta * time);
            moveCloud();
            if (correctCloud.getBoundsInParent().intersects(bird.getBoundsInParent())) {
                chooseRightAnswer();
            }
            if (correctCloud.getLayoutX() < -200) {
                generateCloud();
            }
            if(isBirdDead()){
                gameOverImage.setVisible(true);
                correctCloud.setLayoutX(-WIDTH);
                wrongCloud.setLayoutX(-WIDTH);
                correctCloudImage.setLayoutX(-WIDTH);
                wrongCloudImage.setLayoutX(-WIDTH);
                gameLoop.stop();
            }
        }
    }

    @FXML
    public void pressed(KeyEvent event) throws IOException {
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

    @FXML
    public void clicked(MouseEvent event) throws IOException {
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

    public void startGame() {
        startImage.setVisible(false);
        generateCloud();
        firstScoreImage.setImage(getImage(0));
        secondScoreImage.setImage(getImage(0));
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
        correctCloud.setVisible(false);
        correctCloudImage.setVisible(false);
        if (checkPoint) {
            checkPoint = false;
            score++;
            firstScoreImage.setImage(getImage(score % 10));
            if(score > 9) {
                secondScoreImage.setImage(getImage((score - score % 10) / 10));
            }
        }
    }

    public Image getImage(int number) {
        String path = "num" + number + ".png";
        Image image = new Image(FlappyBirdController.class.getResourceAsStream(path));
        return image;
    }

    private void generateCloud() {
        checkPoint = true;
        correctCloud.setVisible(true);
        correctCloudImage.setVisible(true);
        correctCloud.setLayoutX(WIDTH);
        wrongCloud.setLayoutX(WIDTH);

        correctCloud.setText(correctWordList.get(0));
        wrongCloud.setText(wrongWordList.get(0));
        meaningLabel.setText(meaningWordList.get(0));
        wrongWordList.remove(0);
        correctWordList.remove(0);
        meaningWordList.remove(0);

        if (Math.ceil(Math.random() * 100) % 2 == 0) {
            wrongCloud.setLayoutY(225);
            correctCloud.setLayoutY(425);
            wrongCloudImage.setLayoutY(210);
            correctCloudImage.setLayoutY(410);
        } else {
            wrongCloud.setLayoutY(425);
            correctCloud.setLayoutY(225);
            wrongCloudImage.setLayoutY(410);
            correctCloudImage.setLayoutY(210);
        }
    }

    private void moveCloud() {
        correctCloud.setLayoutX(correctCloud.getLayoutX() - 0.8);
        wrongCloud.setLayoutX(wrongCloud.getLayoutX() - 0.8);
        correctCloudImage.setLayoutX(correctCloud.getLayoutX() - 0.8);
        wrongCloudImage.setLayoutX(wrongCloud.getLayoutX() - 0.8);
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
        if (correctCloud.getLayoutX() + 140 < bird.getLayoutX() && checkPoint) {
            return true;
        }
        return birdY >= anchorPane.getHeight();
    }

    private void reset() throws IOException {
        isGameStarted = false;
        startImage.setVisible(true);
        gameOverImage.setVisible(false);
        score = 0;
        firstScoreImage.setImage(getImage(0));
        secondScoreImage.setImage(getImage(0));
        generateCloud();
        bird.setLayoutY(200);
        bird.setRotate(0);
        time = 0;
    }

    private void updateBackground() {
        updateBackgroundPosition(layer1Background, 0.14);
        updateBackgroundPosition(layer2Background, 0.12);
        updateBackgroundPosition(layer3Background, 0.16);
        updateBackgroundPosition(layer4Background, 0.4);
        updateBackgroundPosition(layer5Background, 0.1);
        updateBackgroundPosition(layer1Background1, 0.14);
        updateBackgroundPosition(layer2Background1, 0.12);
        updateBackgroundPosition(layer3Background1, 0.16);
        updateBackgroundPosition(layer4Background1, 0.4);
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

    public void handleLeaveButton(MouseEvent event) {
        if (event.getSource() == leaveButton) {
            myController.showGameScene();
        }
    }
}
