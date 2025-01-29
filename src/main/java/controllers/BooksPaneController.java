package controllers;

import entity.Book;
import entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BookService;
import services.BorrowingService;
import services.UserService;

import java.io.IOException;
import java.time.LocalDate;

public class BooksPaneController {

    @FXML
    private TableView<Book> books_table;
    @FXML
    private TableColumn<Book, String> genre;
    @FXML
    private TableColumn<Book, String> isbn;
    @FXML
    private TableColumn<Book, String> author;
    @FXML
    private TableColumn<Book, Integer> av_q;
    @FXML
    private TableColumn<Book, String > publisher;
    @FXML
    private TableColumn<Book, String> status;
    @FXML
    private TableColumn<Book, String> title;
    @FXML
    private TableColumn<Book, Integer> total_q;
    @FXML
    private TableColumn<Book, LocalDate> year;

    @FXML
    private Button archiveBook_btn;
    @FXML
    private Button delete_btn;
    @FXML
    private Button issueBook_btn;

    private BookService bookService;
    private BorrowingService borrowingService;
    private UserService userService;
    private final AlertMessage alert = new AlertMessage();
    private ObservableList<Book> books;
    private static final Logger logger = LogManager.getLogger(BooksPaneController.class);
    private User loggedUser;

    public void setParam(BookService bs, BorrowingService brs, UserService us){
        bookService = bs;
        borrowingService = brs;
        userService = us;
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public void initialize() {
        books_table.setEditable(true);

        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        status.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().getStatus()));
        total_q.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        av_q.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        total_q.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        total_q.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setTotalQuantity(event.getNewValue());
        });

        av_q.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        av_q.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setAvailableQuantity(event.getNewValue());
        });


        books_table.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    if ((LocalDate.now().getYear() - newItem.getYear().getYear()) > 40 && newItem.getStatus().getId() == 2) {
                        row.setStyle("-fx-background-color: #fa9734;");
                        row.setOpacity(0.8);
                    } else {
                        row.setStyle("");
                    }
                }
            });
            return row;
        });

        books = FXCollections.observableArrayList();
        books_table.setItems(books);
    }

    public void showBooks(){
        books.setAll(bookService.getAllBooks());
    }

    public void updateBook() {
        for (Book book : books_table.getItems()) {
            bookService.updateBook(book);
            logger.warn("User {} updated the book \"{}\"(ISBN: {})", loggedUser.getEmail(), book.getTitle(),book.getIsbn());
        }
        alert.successMessage("Book updated", "Congrats!");

    }

    public void archiveBook(ActionEvent event) {
        if(event.getSource() == archiveBook_btn){
            if(books_table.getSelectionModel().getSelectedItems() != null) {
                Book book = books_table.getSelectionModel().getSelectedItem();
                checkProcess(book);
                books_table.refresh();
            }
            else alert.emptyAlertMessage("Please select a book to archive");
        }
    }

    public void checkProcess(Book book){
        String s = bookService.archiveBook(book);
        if(!s.isEmpty()){
            if(s.equals("The book must be 40+ years old to be archived")){
                if(alert.confirmationMessage("The book must be 40+ years old to be archived. " +
                        "Do you want to archive this book?")){
                    bookService.archiveYoungBook(book);
                    alert.successMessage("The book is archived", "Success");
                    logger.warn("User {} archived a book \"{}\"(ISBN: {})", loggedUser.getEmail(), book.getTitle(),book.getIsbn());
                }
            }
            else alert.emptyAlertMessage(s);
        }
        else {
            alert.successMessage("The book is archived", "Success");
            logger.warn("User {} archived the book \"{}\"(ISBN: {})", loggedUser.getEmail(), book.getTitle(),book.getIsbn());
        }
    }

    public void deleteBook(ActionEvent event) {
        if(event.getSource() == delete_btn){
            if(books_table.getSelectionModel().getSelectedItem() != null){
                Book book = books_table.getSelectionModel().getSelectedItem();
                bookService.deleteBook(book);
                logger.warn("User {} deleted the book's copy \"{}\"(ISBN: {})", loggedUser.getEmail(), book.getTitle(),book.getIsbn());
                books_table.refresh();
            }
            else alert.emptyAlertMessage("Please select a book to delete");
        }
    }

    public void issueBook(ActionEvent event) throws IOException {
        if(event.getSource() == issueBook_btn){
            if(books_table.getSelectionModel().getSelectedItem() != null){
                Book book = books_table.getSelectionModel().getSelectedItem();
                String s;
                int bookStatus = book.getStatus().getId();
                Pair<Integer,Integer> info = openIssueBookDialog();
                if(info != null) {
                    if (info.getKey() != 0) {
                        if (info.getValue() == bookStatus || (bookStatus == 2 && info.getKey() == 1)) {
                            s = borrowingService.issueBook(book.getId(), info.getKey());
                            if (s.contains("Book issued to ")) {
                                alert.successMessage(s, "Success");
                                User user = userService.getUserById(info.getKey());
                                logger.info("User {} issued the book  \"{}\"(ISBN: {}) to user {} ",
                                        loggedUser.getEmail(), book.getTitle(), book.getIsbn(), user.getEmail());
                            } else
                                alert.emptyAlertMessage(s);
                        } else
                            alert.emptyAlertMessage("The book is archived and cannot be taken out");
                    } else
                        alert.emptyAlertMessage("No value entered or dialog was cancelled");
                }
            }
            else alert.emptyAlertMessage("Please select a book to issue");
        }
    }

    public Pair<Integer,Integer> openIssueBookDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/issueBook.fxml"));
        Parent root = loader.load();
        IssueBookController issueBookController = loader.getController();
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        return issueBookController.getInfoToIssue();
    }

    public void addBook() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addBook.fxml"));
        Parent root = loader.load();
        AddBookController controller = loader.getController();
        controller.setParam(bookService);
        controller.setLoggedUser(loggedUser);

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();
    }
}
