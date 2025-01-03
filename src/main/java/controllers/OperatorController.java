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

import java.io.IOException;

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

    private BooksPaneController booksPaneController;
    private ReadersPaneController readersPaneController;
    private NotificationsPaneController notificationsPaneController;
    private ProfilePaneController profilePaneController;
    private ReportsPaneController reportsPaneController;


    public void initialize() throws IOException {
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

    public void switchForm(ActionEvent event) throws IOException {
        if(event.getSource() == dashboard_btn) {
            setFormsInvisible();
            dashboard_form.setVisible(true);
            showDashboard();
        }
        else if(event.getSource() == readers_btn) {
            setFormsInvisible();
            readers_form.setVisible(true);
            readersPaneController.setLoggedUser(loggedUser);
            readersPaneController.showReadersAndBorBooks();
        }
        else if(event.getSource() == books_btn) {
            setFormsInvisible();
            books_form.setVisible(true);
            booksPaneController.setLoggedUser(loggedUser);
            booksPaneController.showBooks();
        }
        else if(event.getSource() == notifications_btn) {
            setFormsInvisible();
            notifications_form.setVisible(true);
            notificationsPaneController.showNotifications(loggedUser.getUserId());
            notificationsPaneController.markNotificationsAsRead(loggedUser.getUserId());
        }
        else if(event.getSource() == reports_btn) {
            setFormsInvisible();
            reports_form.setVisible(true);
            reportsPaneController.setLoggedUser(loggedUser);
        }
        else if(event.getSource() == profile_btn) {
            setFormsInvisible();
            profile_form.setVisible(true);
            profilePaneController.setLoggedUser(loggedUser);
        }
        else if(event.getSource() == exit_btn) {
            exit();
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
    }

    public void showDashboard(){

    }

    public void showBooksPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booksPane.fxml"));
        Parent booksPane= loader.load();
        booksPaneController = loader.getController();
        books_form.getChildren().setAll(booksPane);
    }

    public void showReadersPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/readersPane.fxml"));
        Parent readersPane= loader.load();
        readersPaneController = loader.getController();
        readers_form.getChildren().setAll(readersPane);
    }

    public void showNotificationsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/notificationsPane.fxml"));
        Parent notif = loader.load();
        notificationsPaneController = loader.getController();
        notifications_form.getChildren().setAll(notif);
    }

    public void showReportsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reportsPane.fxml"));
        Parent reportsPane = loader.load();
        reportsPaneController = loader.getController();
        reports_form.getChildren().setAll(reportsPane);
    }

    public void showProfilePane() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profilePane.fxml"));
        Parent profilePane = loader.load();
        profilePaneController = loader.getController();
        profile_form.getChildren().setAll(profilePane);
    }

    public void exit() throws IOException {
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setMinHeight(350);
        primaryStage.setMinWidth(500);
        primaryStage.setTitle("Library Information System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
