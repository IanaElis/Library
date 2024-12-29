package controllers;

import dao.BookStatusDAO;
import entity.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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

    private AlertMessage alert = new AlertMessage();
    private BookStatusDAO bookStatusDAO = new BookStatusDAO();
    private BookService bookService = new BookService();

    public AddBookController() {
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
        Book book = null;

        if(isbn == null || title == null || author == null || genre == null
                || year == null || total_q == null || av_q == null || publisher == null) {
            alert.emptyAlertMessage("Please fill in the fields");
        }
        else {
            book = new Book(isbn, title, author, genre, year,
                    bookStatusDAO.getBookStatus(2),
                    Integer.parseInt(total_q), Integer.parseInt(av_q), publisher);

            String s = bookService.addBook(book);
            if (s.contains("added")) alert.successMessage(s, "Success");
            else alert.emptyAlertMessage(s);
        }
    }
}
