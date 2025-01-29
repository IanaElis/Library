package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertMessage {
    private Alert alert;

    public Alert getAlert() {
        return alert;
    }

    public void emptyAlertMessage(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void successMessage(String message, String title) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean confirmationMessage(String message) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get().equals(ButtonType.OK)) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean confirmationMessageYesNo(String message) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().equals(ButtonType.YES)) {
            return true;
        } else if (result.get().equals(ButtonType.NO)) {
            return false;
        }
        return false;
    }
}
