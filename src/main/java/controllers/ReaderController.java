package controllers;

import entity.Book;
import entity.Borrowing;
import entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class ReaderController {
    @FXML
    private AnchorPane books_form;
    @FXML
    private AnchorPane borrowedBooks_form;
    @FXML
    private AnchorPane notifications_form;
    @FXML
    private AnchorPane profile_form;

    @FXML
    private Button books_btn;
    @FXML
    private Button exit_btn;
    @FXML
    private Button notifications_btn;
    @FXML
    private Button profile_btn;
    @FXML
    private Button borrowedBooks_btn;

    @FXML
    private TableView<Book> books_table;
    @FXML
    private TableColumn<Book, String> genre;
    @FXML
    private TableColumn<Book, String> isbn;
    @FXML
    private TableColumn<Book, String> author;
    @FXML
    private TableColumn<Book, String > publisher;
    @FXML
    private TableColumn<Book, String> status;
    @FXML
    private TableColumn<Book, String> title;
    @FXML
    private TableColumn<Book, LocalDate> year;

    @FXML
    private TableColumn<Borrowing, String> borBooks_author;
    @FXML
    private TableColumn<Borrowing, LocalDate> borBooks_dateOfBor;
    @FXML
    private TableColumn<Borrowing, LocalDate> borBooks_dateOfRet;
    @FXML
    private TableColumn<Borrowing, String> borBooks_isbn;
    @FXML
    private TableView<Borrowing> borBooks_table;
    @FXML
    private TableColumn<Borrowing, String> borBooks_title;

    @FXML
    private Label username_label;

    private User loggedUser;

    private final BookService bookService = ServicesDAOs.getBookService();
    private final BorrowingService borrowingService = ServicesDAOs.getBorrowingService();
    private final UserService userService = ServicesDAOs.getUserService();
    private final NotificationService notificationService = ServicesDAOs.getNotificationService();

    private NotificationsPaneController notificationsPaneController;
    private static final Logger logger = LogManager.getLogger(ReaderController.class);

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        showName();
    }

    public void getDefaultPane(){
        setFormsInvisible();
        books_form.setVisible(true);
        showBooks();
    }

    public void initialize() throws IOException {
        showNotificationsPane();
    }

    public void switchForm(ActionEvent event){
        if(event.getSource() == borrowedBooks_btn) {
            setFormsInvisible();
            borrowedBooks_form.setVisible(true);
            showBorrowedBooks();
        }
        else if(event.getSource() == books_btn) {
            setFormsInvisible();
            books_form.setVisible(true);
            showBooks();
        }
        else if(event.getSource() == notifications_btn) {
            setFormsInvisible();
            notifications_form.setVisible(true);
            notificationsPaneController.showNotifications(loggedUser);
            notificationsPaneController.markNotificationsAsRead(loggedUser.getUserId());
        }
        else if(event.getSource() == profile_btn) {
            setFormsInvisible();
            profile_form.setVisible(true);
        }
        else if(event.getSource() == exit_btn) {
            exit();
            logger.info("User {} logged out", loggedUser.getEmail());
        }
    }

    public void setFormsInvisible(){
        borrowedBooks_form.setVisible(false);
        books_form.setVisible(false);
        notifications_form.setVisible(false);
        profile_form.setVisible(false);
    }

    public void showName(){
        if(loggedUser != null) username_label.setText("Welcome, " + loggedUser.getName());
        notificationsPaneController.showUnreadNotificationsAlert(loggedUser.getUserId());
        booksOverdue(loggedUser);
    }

    public void showBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList(bookService.getAllBooks());
        books_table.setItems(books);

        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        status.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().getStatus()));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    }

    public void showBorrowedBooks(){
        ObservableList<Borrowing> borrowings = FXCollections.
                observableArrayList(borrowingService.getAllBorrowingsByUser(loggedUser.getUserId()));
        borBooks_table.setItems(borrowings);

        borBooks_isbn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getIsbn()));
        borBooks_title.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        borBooks_author.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));
        borBooks_dateOfBor.setCellValueFactory(new PropertyValueFactory<>("borrowingDate"));
        borBooks_dateOfRet.setCellValueFactory(new PropertyValueFactory<>("actualReturnDate"));

        borBooks_table.setRowFactory(tv -> {
            TableRow<Borrowing> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    if (newItem.getStatus().getBorStatId() == 3) {
                        row.setStyle("-fx-background-color: #de5555;");
                        row.setOpacity(0.8);
                    } else {
                        row.setStyle("");
                    }
                }
            });
            return row;
        });
    }

    public void booksOverdue(User user){
        borrowingService.checkForOverdue(user);
    }

    public void showNotificationsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/notificationsPane.fxml"));
        Parent notif = loader.load();
        notificationsPaneController = loader.getController();
        notificationsPaneController.setParam(userService,bookService,notificationService);
        notifications_form.getChildren().setAll(notif);

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
