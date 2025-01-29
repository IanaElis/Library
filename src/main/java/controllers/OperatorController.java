package controllers;

import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.*;

import java.io.IOException;
import java.util.Objects;

public class OperatorController {
    @FXML
    private AnchorPane books_form;
    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private AnchorPane notifications_form;
    @FXML
    private AnchorPane profile_form;
    @FXML
    private AnchorPane reports_form;
    @FXML
    private AnchorPane readers_form;

    @FXML
    private Button books_btn;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button exit_btn;
    @FXML
    private Button notifications_btn;
    @FXML
    private Button profile_btn;
    @FXML
    private Button readers_btn;
    @FXML
    private Button reports_btn;

    @FXML
    private Label username_label;

    private User loggedUser;

    private final BookService bookService = ServicesDAOs.getBookService();
    private final BorrowingService borrowingService = ServicesDAOs.getBorrowingService();
    private final UserService userService = ServicesDAOs.getUserService();
    private final UserRatingService userRatingService = ServicesDAOs.getUserRatingService();
    private final NotificationService notificationService = ServicesDAOs.getNotificationService();

    private BooksPaneController booksPaneController;
    private ReadersPaneController readersPaneController;
    private NotificationsPaneController notificationsPaneController;
    private ProfilePaneController profilePaneController;
    private ReportsPaneController reportsPaneController;
    private DefaultPageController defaultPageController;
    private static final Logger logger = LogManager.getLogger(OperatorController.class);

    public void initialize() throws IOException {
        showDashboard();
        showBooksPane();
        showReadersPane();
        showNotificationsPane();
        showReportsPane();
        showProfilePane();
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        showName();
    }

    public void getDefaultPane(){
        setFormsInvisible();
        dashboard_form.setVisible(true);
        defaultPageController.showStatistics();
    }

    private void showForm(AnchorPane form){
        setFormsInvisible();
        form.setVisible(true);
    }

    public void switchForm(ActionEvent event) {
        if(event.getSource() == dashboard_btn) {
            showForm(dashboard_form);
            defaultPageController.showStatistics();
        }
        else if(event.getSource() == readers_btn) {
            showForm(readers_form);
            readersPaneController.setLoggedUser(loggedUser);
            readersPaneController.showReadersAndBorBooks();
        }
        else if(event.getSource() == books_btn) {
            showForm(books_form);
            booksPaneController.setLoggedUser(loggedUser);
            booksPaneController.showBooks();
        }
        else if(event.getSource() == notifications_btn) {
            showForm(notifications_form);
            notificationsPaneController.showNotifications(loggedUser);
            notificationsPaneController.markNotificationsAsRead(loggedUser.getUserId());
        }
        else if(event.getSource() == reports_btn) {
            showForm(reports_form);
            reportsPaneController.setLoggedUser(loggedUser);
        }
        else if(event.getSource() == profile_btn) {
            showForm(profile_form);
            profilePaneController.setLoggedUser(loggedUser);
        }
        else if(event.getSource() == exit_btn) {
            exit();
            logger.info("User {} logged out", loggedUser.getEmail());
        }
    }

    public void setFormsInvisible(){
        dashboard_form.setVisible(false);
        readers_form.setVisible(false);
        books_form.setVisible(false);
        notifications_form.setVisible(false);
        reports_form.setVisible(false);
        profile_form.setVisible(false);
    }

    public void showName(){
        if(loggedUser != null) username_label.setText("Welcome, " + loggedUser.getName());
        bookService.needToArchive();
    }

    public void showDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
        Parent main= loader.load();
        defaultPageController = loader.getController();
        defaultPageController.setParam(borrowingService, bookService, userService);
        dashboard_form.getChildren().setAll(main);
    }

    public void showBooksPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booksPane.fxml"));
        Parent booksPane= loader.load();
        booksPaneController = loader.getController();
        booksPaneController.setParam(bookService, borrowingService, userService);
        books_form.getChildren().setAll(booksPane);
    }

    public void showReadersPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/readersPane.fxml"));
        Parent readersPane= loader.load();
        readersPaneController = loader.getController();
        readersPaneController.setParam(userService, borrowingService);
        readers_form.getChildren().setAll(readersPane);
    }

    public void showNotificationsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/notificationsPane.fxml"));
        Parent notif = loader.load();
        notificationsPaneController = loader.getController();
        notificationsPaneController.setParam(userService, bookService, notificationService);
        notifications_form.getChildren().setAll(notif);
    }

    public void showReportsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reportsPane.fxml"));
        Parent reportsPane = loader.load();
        reportsPaneController = loader.getController();
        reportsPaneController.setParam(userService, bookService, borrowingService, userRatingService);
        reports_form.getChildren().setAll(reportsPane);
    }

    public void showProfilePane() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profilePane.fxml"));
        Parent profilePane = loader.load();
        profilePaneController = loader.getController();
        profilePaneController.setParam(userService);
        profile_form.getChildren().setAll(profilePane);
    }

    public void exit(){
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("/fxml/login.fxml")));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setMinHeight(350);
            primaryStage.setMinWidth(500);
            primaryStage.setTitle("Library Information System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            logger.error("Error loading login screen", e);
            throw new RuntimeException(e);
        }
    }
}
