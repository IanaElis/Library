package controllers;

import entity.Book;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BookService;

import java.time.LocalDate;


public class AddBookController {
    @FXML
    private TextField author;
    @FXML
    private TextField av_q;
    @FXML
    private ComboBox<String> genre;
    @FXML
    private TextField isbn;
    @FXML
    private TextField title;
    @FXML
    private TextField total_q;
    @FXML
    private DatePicker year;
    @FXML
    private TextField publisher;

    private final AlertMessage alert = new AlertMessage();
    private BookService bookService;
    private static final Logger logger = LogManager.getLogger(AddBookController.class);
    private User loggedUser;

    public void setParam(BookService bs){
        bookService = bs;
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Science fiction", "Magic Realism", "Option 3");
        genre.setItems(options);
    }

    public void addBookForm(){
        String isbn = this.isbn.getText();
        String title = this.title.getText();
        String author = this.author.getText();
        String genre = this.genre.getValue();
        LocalDate year = this.year.getValue();
        String total_q = this.total_q.getText();
        String av_q = this.av_q.getText();
        String publisher = this.publisher.getText();

        if(isbn == null || title == null || author == null || genre == null
                || year == null || total_q == null || av_q == null || publisher == null) {
            alert.emptyAlertMessage("Please fill in the fields");
        }
        else {
            int parsedTotalQ = 0;
            int parsedAvQ = 0;
            try {
                parsedTotalQ = Integer.parseInt(total_q);
                parsedAvQ = Integer.parseInt(av_q);
            } catch (NumberFormatException e) {
                alert.emptyAlertMessage("Invalid number format");
            }

            Book book = new Book(isbn, title, author, genre, year,
                    bookService.getBookStatus(2),
                    parsedTotalQ, parsedAvQ, publisher);

            String s = bookService.addBook(book);
            if (s.contains("added")) {
                alert.successMessage(s, "Success");
                logger.info("User {} added a new book \"{}\"(ISBN: {})",
                        loggedUser.getEmail(), book.getTitle(), book.getIsbn());
            }
            else {
                alert.emptyAlertMessage(s);
            }
        }
    }
}
