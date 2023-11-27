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

import java.awt.*;
import java.io.IOException;
import java.net.URL;
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
    private double time = 0;
    private int score = 0;
    private boolean isCanFly = true;
    private boolean isPassedCheckPoint = false;
    private boolean isGameStarted = false;
    private ContainerController myController;
    private ArrayList<String> correctWordList = new ArrayList<>();
    private ArrayList<String> wrongWordList = new ArrayList<>();
    private ArrayList<String> meaningWordList = new ArrayList<>();
    private final int BACKGROUND_WIDTH = 1333;
    ArrayList<String> sourceList = new ArrayList<>();
    private final double BACKGROUND_LAYER1_SPEED = 0.14;
    private final double BACKGROUND_LAYER2_SPEED = 0.12;
    private final double BACKGROUND_LAYER3_SPEED = 0.16;
    private final double BACKGROUND_LAYER4_SPEED = 0.4;
    private final double BACKGROUND_LAYER5_SPEED = 0.1;
    private final double BIRD_JUMP_HEIGHT = 60;
    private final double BIRD_ROTATE = 0.012;
    private final double BIRD_DROP = 0.05;
    private final double BIRD_POSITION_Y = 200;
    private final int CLOUD_POSITION1 = 390;
    private final int CLOUD_POSITION2 = 190;
    private final double CLOUD_SPEED = 1.8;
    private final int CLOUD_REGENERATE_POSITION = -200;
    private final int NUMBER_OF_QUESTIONS = 20;

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
        this.sourceList = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
        loadQuestions();
    }


    private void update() throws IOException {
        if (isGameStarted) {
            time ++;
            moveBird(BIRD_DROP * time);
            moveCloud();
            if (correctCloud.getBoundsInParent().intersects(bird.getBoundsInParent())) {
                chooseRightAnswer();
            }
            if (correctCloud.getLayoutX() < CLOUD_REGENERATE_POSITION) {
                generateCloud();
            }
            if(isDead()){
                gameOverImage.setVisible(true);
                correctCloud.setLayoutX(-BACKGROUND_WIDTH);
                wrongCloud.setLayoutX(-BACKGROUND_WIDTH);
                correctCloudImage.setLayoutX(-BACKGROUND_WIDTH);
                wrongCloudImage.setLayoutX(-BACKGROUND_WIDTH);
                gameLoop.stop();
            }
        }
    }

    @FXML
    public void pressed(KeyEvent event) throws IOException
    {
        if (event.getCode() == KeyCode.SPACE) {
            if (isDead()) {
                gameLoop.start();
                reset();
            } else if (isGameStarted) {
                if (isCanFly) {
                    fly();
                    isCanFly = false;
                }
            } else {
                isGameStarted = true;
                startGame();
            }
        }
    }

    @FXML
    void released(KeyEvent event) {
        isCanFly = true;
    }

    @FXML
    public void clicked(MouseEvent event) throws IOException {
        if (isDead()) {
            gameLoop.start();
            reset();
        } else if (isGameStarted) {
            fly();
        } else if (!isGameStarted) {
            isGameStarted = true;
            startGame();
        }
    }

    public void loadQuestions() throws IOException {
        String input = "";
        for (String word : sourceList) {
            if (word.length() >= 3 && word.length() <= 8 && !word.contains(" ") && !word.contains(".") && !word.contains("-") && Math.random() < 0.025) {
                input += word + ", ";
            }
        }
        String[] rawList = input.split(",");
        String translatedInput = TranslateTextAPIs.translate(input, "en", "vi");
        String[] translatedList = translatedInput.split(",");
        for (int i = 0; i < rawList.length && i < translatedList.length; i++) {
            System.out.print(rawList[i].trim().toUpperCase() + "   ");
            System.out.println(translatedList[i].trim().toUpperCase());
        }
        for (int i = 0; i < rawList.length && i < translatedList.length; i++) {
            if (rawList[i].trim().toUpperCase() != translatedList[i].trim().toUpperCase() && Math.random() < 0.75) {
                correctWordList.add(rawList[i].trim().toUpperCase());
                meaningWordList.add(translatedList[i].trim().toUpperCase());
            } else {
                wrongWordList.add(rawList[i].trim().toUpperCase());
            }
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
        if(bird.getLayoutY() <= BIRD_JUMP_HEIGHT){
            moveBird(-bird.getLayoutY());
            time = 0;
            return;
        }
        moveBird(-BIRD_JUMP_HEIGHT);
        time = 0;
    }

    private void chooseRightAnswer() {
        correctCloud.setVisible(false);
        correctCloudImage.setVisible(false);
        if (!isPassedCheckPoint) {
            isPassedCheckPoint = true;
            score++;
            firstScoreImage.setImage(getImage(score % 10));
            if(score > 9) {
                secondScoreImage.setImage(getImage(score / 10));
            }
        }
    }

    public Image getImage(int number) {
        String path = "num" + number + ".png";
        Image image = new Image(FlappyBirdController.class.getResourceAsStream(path));
        return image;
    }

    private void generateCloud() {
        isPassedCheckPoint = false;
        correctCloud.setVisible(true);
        correctCloudImage.setVisible(true);
        correctCloud.setLayoutX(BACKGROUND_WIDTH);
        wrongCloud.setLayoutX(BACKGROUND_WIDTH);

        correctCloud.setText(correctWordList.get(0));
        wrongCloud.setText(wrongWordList.get(0));
        meaningLabel.setText(meaningWordList.get(0));
        wrongWordList.remove(0);
        correctWordList.remove(0);
        meaningWordList.remove(0);

        if (Math.ceil(Math.random() * 100) % 2 == 0) {
            wrongCloud.setLayoutY(CLOUD_POSITION1);
            correctCloud.setLayoutY(CLOUD_POSITION2);
            wrongCloudImage.setLayoutY(CLOUD_POSITION1);
            correctCloudImage.setLayoutY(CLOUD_POSITION2);
        } else {
            wrongCloud.setLayoutY(CLOUD_POSITION2);
            correctCloud.setLayoutY(CLOUD_POSITION1);
            wrongCloudImage.setLayoutY(CLOUD_POSITION2);
            correctCloudImage.setLayoutY(CLOUD_POSITION1);
        }
    }

    private void moveCloud() {
        correctCloud.setLayoutX(correctCloud.getLayoutX() - CLOUD_SPEED);
        wrongCloud.setLayoutX(wrongCloud.getLayoutX() - CLOUD_SPEED);
        correctCloudImage.setLayoutX(correctCloud.getLayoutX() - CLOUD_SPEED);
        wrongCloudImage.setLayoutX(wrongCloud.getLayoutX() - CLOUD_SPEED);
    }

    private void moveBird(double positionChange){
        bird.setRotate(bird.getRotate() + BIRD_ROTATE * time);
        bird.setLayoutY(bird.getLayoutY() + positionChange);
    }

    private boolean isDead(){
        double birdY = bird.getLayoutY();
        if (wrongCloud.getBoundsInParent().intersects(bird.getBoundsInParent()) && !isPassedCheckPoint) {
            return true;
        }
        if (correctCloud.getLayoutX() + 140 < bird.getLayoutX() && !isPassedCheckPoint) {
            return true;
        }
        return birdY >= anchorPane.getHeight();
    }

    private void reset() throws IOException {
        if (this.sourceList.size() < 10) {
            loadQuestions();
        }
        isGameStarted = false;
        startImage.setVisible(true);
        gameOverImage.setVisible(false);
        score = 0;
        time = 0;
        firstScoreImage.setImage(getImage(0));
        secondScoreImage.setImage(getImage(0));
        correctCloud.setLayoutX(BACKGROUND_WIDTH);
        wrongCloud.setLayoutX(BACKGROUND_WIDTH);
        meaningLabel.setText("");
        bird.setLayoutY(BIRD_POSITION_Y);
        bird.setRotate(0);
    }

    private void updateBackground() {
        updateBackgroundPosition(layer1Background, BACKGROUND_LAYER1_SPEED);
        updateBackgroundPosition(layer2Background, BACKGROUND_LAYER2_SPEED);
        updateBackgroundPosition(layer3Background, BACKGROUND_LAYER3_SPEED);
        updateBackgroundPosition(layer4Background, BACKGROUND_LAYER4_SPEED);
        updateBackgroundPosition(layer5Background, BACKGROUND_LAYER5_SPEED);
        updateBackgroundPosition(layer1Background1, BACKGROUND_LAYER1_SPEED);
        updateBackgroundPosition(layer2Background1, BACKGROUND_LAYER2_SPEED);
        updateBackgroundPosition(layer3Background1, BACKGROUND_LAYER3_SPEED);
        updateBackgroundPosition(layer4Background1, BACKGROUND_LAYER4_SPEED);
        updateBackgroundPosition(layer5Background1, BACKGROUND_LAYER5_SPEED);
    }

    private void updateBackgroundPosition(ImageView imageView, double speed) {
        double currentX = imageView.getLayoutX();
        double newX = currentX - speed;
        if (newX + BACKGROUND_WIDTH <= 0) {
            newX += BACKGROUND_WIDTH * 2;
        }
        imageView.setLayoutX(newX);
    }

    public void handleLeaveButton(MouseEvent event) {
        if (event.getSource() == leaveButton) {
            myController.showGameScene();
        }
    }
}
