package controllers;

import entity.Book;
import entity.RegisterForm;
import entity.User;
import entity.UserNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import services.BookService;
import services.NotificationService;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class NotificationsPaneController {
    @FXML
    private VBox notificationsPane;
    @FXML
    private ScrollPane scrollPane;
    private static final Logger logger = LogManager.getLogger(NotificationsPaneController.class);

    private NotificationService notificationService;
    private BookService bookService;
    private UserService userService;
    private final AlertMessage alert = new AlertMessage();
    List<UserNotification> list = new ArrayList<>();

    public void setParam(UserService us, BookService bs, NotificationService ns){
        userService = us;
        bookService = bs;
        notificationService = ns;
    }

    public void showUnreadNotificationsAlert(int currentUserId) {
        int unreadCount = notificationService.getUnreadNotificationCount(currentUserId);
        if (unreadCount > 0) {
            alert.confirmationMessage("You have" + unreadCount + " unread notifications");
        }
    }

    public void markNotificationsAsRead(int currentUserId) {
        notificationService.markNotificationsAsRead(currentUserId);
    }

    public void showNotifications(User currentUser) {
        notificationsPane.getChildren().clear();
        list = notificationService.getNotificationsByUser(currentUser.getUserId());
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
                String result = userNotification.getAdditionalInfo();

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
                    approveButton.setOnAction(event -> {
                        boolean res = userService.approveReader(userToAdd);
                        if(!res) {
                            alert.emptyAlertMessage("User already denied");
                            logger.warn("User with register form {} has been denied registration", userToAdd.getRegFormId());
                        }
                        else logger.info("User {} created successfully by {} {}",
                                userToAdd.getEmail(), currentUser.getRole().getName(), currentUser.getEmail());
                    });
                    denyButton.setOnAction(event -> {
                        userService.denyReader(userToAdd);
                        if(!userService.denyReader(userToAdd)) {
                            alert.emptyAlertMessage("User already approved");
                            logger.info("User {} has already been approved", userToAdd.getEmail());
                        }
                        else logger.info("User {} denied by {} {}",
                                userToAdd.getEmail(), currentUser.getRole().getName(), currentUser.getEmail());
                    });

                    if(userNotification.isRead()){
                      //  messageLabel.setStyle("-fx-text-fill: #cfc5c5;");
                        if(userToAdd.getStatus().getRegStatId() == 2 ||
                                userToAdd.getStatus().getRegStatId() == 3){
                            infoLabel.setStyle("-fx-text-fill: #cfc5c5; -fx-alignment: center;");
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
            else if(userNotification.getNotification().getNotifID() == 3){
                List<Book> books = bookService.booksToArchive();
                for(Book book : books) {
                    Label bookLabel = new Label("\nISBN: " + book.getIsbn() +
                            "Book ID: " + book.getId());
                    bookLabel.setWrapText(true);
                    contentBox = new VBox(bookLabel);
                    contentBox.setSpacing(30);
                    notificationBox.getChildren().add(contentBox);
                }
            }

            if(userNotification.isRead()){
                messageLabel.setStyle("-fx-text-fill: #cfc5c5;");
            }
            notificationsPane.getChildren().add(notificationBox);
        }
        scrollPane.setContent(notificationsPane);
    }

}
