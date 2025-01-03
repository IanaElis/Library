package controllers;

import entity.RegisterForm;
import entity.User;
import entity.UserNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.Logger;
import services.NotificationService;
import services.UserService;
import util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationsPaneController {
    @FXML
    private VBox notificationsPane;

    private final NotificationService notificationService = new NotificationService();
    private UserService userService = new UserService();
    private AlertMessage alert = new AlertMessage();
    List<UserNotification> list = new ArrayList<>();

    public void showUnreadNotificationsAlert(int currentUserId) {
        int unreadCount = notificationService.getUnreadNotificationCount(currentUserId);
        if (unreadCount > 0) {
            alert.confirmationMessage("You have" + unreadCount + " unread notifications");
        }
    }

    public void markNotificationsAsRead(int currentUserId) {
        notificationService.markNotificationsAsRead(currentUserId);
    }

    public void showNotifications(int currentUserId) {
        notificationsPane.getChildren().clear();
        list = notificationService.getNotificationsByUser(currentUserId);
        for(UserNotification userNotification : list) {
            VBox notificationBox = new VBox();
            notificationBox.setSpacing(10);
            notificationBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-padding: 10;");

            Label messageLabel = new Label(userNotification.getNotification().getMessage());
            messageLabel.setWrapText(true);

            VBox contentBox = new VBox(messageLabel);
            contentBox.setSpacing(5);

            notificationBox.getChildren().add(contentBox);

            if (userNotification.getNotification().getNotifID() == 1) {
                String email = userNotification.getNotification().getAdditionalInfo();
                String result = this.getEmail(email);

                if(result != null) {
                    RegisterForm userToAdd = userService.getRegFormByEmail(result);

                    Label infoLabel = new Label("Name: " + userToAdd.getName() +
                            "\nEmail: " + userToAdd.getEmail() +
                            "\nPhone number: " + userToAdd.getPhoneNumber() +
                            "\nRegistration date: " + userToAdd.getDateCreated());
                    infoLabel.setWrapText(true);
                    contentBox = new VBox(infoLabel);
                    contentBox.setSpacing(30);


                    Button approveButton = new Button("Approve");
                    Button denyButton = new Button("Deny");
                    approveButton.setOnAction(event -> userService.approveReader(userToAdd));
                    denyButton.setOnAction(event -> {
                        userService.denyReader(userToAdd);
                        if(!userService.denyReader(userToAdd))
                            alert.emptyAlertMessage("User already approved");
                    });

                    if(userNotification.isRead()){
                        messageLabel.setStyle("-fx-text-fill: #cfc5c5;");
                        infoLabel.setStyle("-fx-text-fill: #cfc5c5; -fx-alignment: center;");
                        if(userToAdd.getStatus().getRegStatId() == 2){
                            approveButton.setDisable(true);
                            denyButton.setDisable(true);
                        }
                    }

                    HBox buttonBox = new HBox(10, approveButton, denyButton);
                    buttonBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

                    notificationBox.getChildren().add(contentBox);
                    notificationBox.getChildren().add(buttonBox);
                }
            }
            notificationsPane.getChildren().add(notificationBox);
        }
    }

    public String getEmail(String input) {
        String keyword = "email: ";
        int startIndex = input.indexOf(keyword);
        if (startIndex != -1) {
            startIndex += keyword.length();
            int endIndex = input.indexOf(' ', startIndex);
            if (endIndex == -1) {
                endIndex = input.length();
            }
            return input.substring(startIndex, endIndex);
        }
        return null;
    }
}
