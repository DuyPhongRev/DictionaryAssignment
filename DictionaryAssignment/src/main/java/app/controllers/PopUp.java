package app.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import javax.swing.text.html.ImageView;

public class PopUp {
    public static void showPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String cssPath = PopUp.class.getResource("popUp.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssPath);
        alert.setTitle("Notification!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Boolean showConfirmationPopup(String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmation");
        String cssPath = PopUp.class.getResource("popUp.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssPath);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        dialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

        dialog.showAndWait();

        ButtonType result = dialog.getResult();
        dialog.close();
        if (result == yesButton) {
            return true;
        } else {
            return false;
        }
    }
}
