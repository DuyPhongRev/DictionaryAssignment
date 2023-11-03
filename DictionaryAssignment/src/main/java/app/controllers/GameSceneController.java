package app.controllers;

import app.connections.databaseConnection;
import app.dictionary.Dictionary;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GameSceneController {
    @FXML
    private GridPane answerGridPane, keyboardGridPane;
    @FXML
    private Button resetButton, giveUpButton, instructionButton;
    @FXML
    private Button qButton, wButton, eButton, rButton, tButton, yButton, uButton, iButton, oButton, pButton;
    @FXML
    private Button aButton, sButton, dButton, fButton, gButton, hButton, jButton, kButton, lButton;
    @FXML
    private Button zButton, xButton, cButton, vButton, bButton, nButton, mButton, enterButton, deleteButton;
//    private databaseConnection dictionary = new databaseConnection();
    private String hiddenWord = "CANDY";
    private int currentIndex = 0;
    private int currentAttempt = 1;
    private final int size = 5;

    public void setText(String text) {
        if (currentIndex < size * currentAttempt) {
            Label label = (Label) answerGridPane.getChildren().get(currentIndex);
            label.setText(text);
            label.setStyle(
                    "-fx-border-color: #A7ADC0"
            );
            currentIndex++;
        }
    }

    public void deleteText() {
        if (currentIndex > (currentAttempt - 1) * size) {
            currentIndex--;
            Label label = (Label) answerGridPane.getChildren().get(currentIndex);
            label.setText("");
            label.setStyle(null);
        }
    }

    public void instruct() {
        showAlert("INSTRUCTION", """
                You have to guess the hidden word in 6 tries and the color of the letters changes to show how close you are.
                GRAY is in the target word at all.
                YELLOW is in the word but in the wrong spot.
                GREEN is in the word and in the correct spot.
                Got it!?""");
    }

    public void highLightLastWord(String s, int status) {
        for (int i = 0; i < 26; i++) {
            if (keyboardGridPane.getChildren().get(i) != null) {
                Button button = (Button) keyboardGridPane.getChildren().get(i);
                if (button.getText().equals(s)) {
                    if (status == 0) {
                        button.setStyle(
                                "-fx-text-fill: #FFFFFF;"
                                        + "-fx-background-color: #79B851;"
                        );
                    } else if (status == 1) {
                        if (!button.getStyle().contains("#79B851")) {
                            button.setStyle(
                                    "-fx-text-fill: #FFFFFF;"
                                            + "-fx-background-color: #F3C237;"
                            );
                        }
                    } else {
                        if (!button.getStyle().contains("#79B851") && !button.getStyle().contains("F3C237")) {
                            button.setStyle(
                                    "-fx-text-fill: #FFFFFF;"
                                            + "-fx-background-color: #A4AEC4;"
                            );
                        }
                    }
                }
            }
        }
    }

    public void confirmText() {
        boolean endGame = true;
        if (currentIndex == currentAttempt * size) {
            int size = answerGridPane.getColumnCount();
            for (int i = size * (currentAttempt - 1); i < size * currentAttempt; i++) {
                Label label = (Label) answerGridPane.getChildren().get(i);
                if (hiddenWord.charAt(i - size * (currentAttempt - 1)) == label.getText().charAt(0)) {
                    label.setStyle(
                            "-fx-text-fill: #FFFFFF;"
                                    + "-fx-background-color: #79B851;"
                                    + "-fx-border-width: 0px"
                    );
                    highLightLastWord(label.getText(), 0);
                } else if (hiddenWord.indexOf(label.getText().charAt(0)) != -1) {
                    endGame = false;
                    label.setStyle(
                            "-fx-text-fill: #FFFFFF;"
                                    + "-fx-background-color: #F3C237;"
                                    + "-fx-border-width: 0px"
                    );

                    highLightLastWord(label.getText(), 1);
                } else {
                    endGame = false;
                    label.setStyle(
                            "-fx-text-fill: #FFFFFF;"
                                    + "-fx-background-color: #A4AEC4;"
                                    + "-fx-border-width: 0px"
                    );
                    highLightLastWord(label.getText(), 2);
                }
            }
            currentAttempt++;
            if (endGame) {
                showAlert("WORDLE", "GOOD JOBS!!! YOU'RE WINNER");
                reset();
            }
            if (currentAttempt == 7) {
                giveUp();
            }
        }
    }

    public void reset() {
        for (int i = 0; i < 26; i++) {
            Button button = (Button) keyboardGridPane.getChildren().get(i);
            button.setStyle(null);
        }
        for (int i = 0; i < this.currentIndex; i++) {
            Label label = (Label) answerGridPane.getChildren().get(i);
            label.setText("");
            label.setStyle(null);
        }
        this.currentIndex = 0;
        this.currentAttempt = 1;
//        this.hiddenWord = dictionary.gameQueryWord();
    }

    public void giveUp() {
        showAlert("WORDLE", """
                Ha :| Chicken, you're loser.
                Hiddien Word is\s""" + hiddenWord);
        reset();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleEvent(Event event) {
        if (event.getSource().equals(resetButton)) {
            reset();
        } else if (event.getSource().equals(instructionButton)) {
            instruct();
        } else if (event.getSource().equals(giveUpButton)) {
            giveUp();
        } else if (event.getSource().equals(qButton)) {
            setText(qButton.getText());
        } else if (event.getSource().equals(wButton)) {
            setText(wButton.getText());
        } else if (event.getSource().equals(eButton)) {
            setText(eButton.getText());
        } else if (event.getSource().equals(rButton)) {
            setText(rButton.getText());
        } else if (event.getSource().equals(tButton)) {
            setText(tButton.getText());
        } else if (event.getSource().equals(yButton)) {
            setText(yButton.getText());
        } else if (event.getSource().equals(uButton)) {
            setText(uButton.getText());
        } else if (event.getSource().equals(iButton)) {
            setText(iButton.getText());
        } else if (event.getSource().equals(oButton)) {
            setText(oButton.getText());
        } else if (event.getSource().equals(pButton)) {
            setText(pButton.getText());
        } else if (event.getSource().equals(aButton)) {
            setText(aButton.getText());
        } else if (event.getSource().equals(sButton)) {
            setText(sButton.getText());
        } else if (event.getSource().equals(dButton)) {
            setText(dButton.getText());
        } else if (event.getSource().equals(fButton)) {
            setText(fButton.getText());
        } else if (event.getSource().equals(gButton)) {
            setText(gButton.getText());
        } else if (event.getSource().equals(hButton)) {
            setText(hButton.getText());
        } else if (event.getSource().equals(jButton)) {
            setText(jButton.getText());
        } else if (event.getSource().equals(kButton)) {
            setText(kButton.getText());
        } else if (event.getSource().equals(lButton)) {
            setText(lButton.getText());
        } else if (event.getSource().equals(zButton)) {
            setText(zButton.getText());
        } else if (event.getSource().equals(xButton)) {
            setText(xButton.getText());
        } else if (event.getSource().equals(cButton)) {
            setText(cButton.getText());
        } else if (event.getSource().equals(vButton)) {
            setText(vButton.getText());
        } else if (event.getSource().equals(bButton)) {
            setText(bButton.getText());
        } else if (event.getSource().equals(nButton)) {
            setText(nButton.getText());
        } else if (event.getSource().equals(mButton)) {
            setText(mButton.getText());
        } else if (event.getSource().equals(deleteButton)) {
            deleteText();
        } else if (event.getSource().equals(enterButton)) {
            confirmText();
        }
    }
}