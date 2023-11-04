package app.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class PopUp {
    public static void showPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static String showConfirmationPopup(String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmation");
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        dialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

        dialog.showAndWait();

        ButtonType result = dialog.getResult();
        if (result == yesButton) {
            dialog.close();
            return "yes";
        } else {
            dialog.close();
            return "no";
        }
    }
}
