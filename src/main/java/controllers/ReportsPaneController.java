package controllers;

import entity.Book;
import entity.Borrowing;
import entity.RegisterForm;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.Logger;
import services.BookService;
import services.BorrowingService;
import services.UserService;
import util.LoggerUtil;

import java.util.List;

public class ReportsPaneController {

    @FXML
    private Button rep_books;
    @FXML
    private Button rep_forms;
    @FXML
    private Button rep_rating;
    @FXML
    private TextArea rep_report;
    @FXML
    private Button rep_users;

    private UserService userService;
    private BookService bookService;
    private BorrowingService borrowingService;
    private static final Logger logger = LoggerUtil.getLogger();
    private User loggedUser;

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public ReportsPaneController() {
        userService = new UserService();
        bookService = new BookService();
        borrowingService = new BorrowingService();
    }

    public void makeReports(ActionEvent event) {
        StringBuilder report = new StringBuilder();

        if(event.getSource() == rep_forms) {
            List<RegisterForm> forms = userService.getAllRegisterForms();
            for(RegisterForm form : forms) {
                report.append("Date: ").append(form.getDateCreated()).append("\n");
                report.append("Status: ").append(form.getStatus().getStatus()).append("\n");
                report.append("Forms content: \n");
                report.append("ID: ").append(form.getRegFormId()).append("\n");
                report.append("Email: ").append(form.getEmail()).append("\n");
                report.append("Name: ").append(form.getName()).append("\n");
                report.append("Phone number: ").append(form.getPhoneNumber()).append("\n");
                report.append("\n\n");
            }
            logger.info("User {} requested forms report", loggedUser);
        }
        else if(event.getSource() == rep_books) {
            List<Book> books = bookService.getAllBooks();
            for(Book book : books) {
                report.append("ID: ").append(book.getId()).append("\n");
                report.append("Status: ").append(book.getStatus().getStatus()).append("\n");
                report.append("Book information: \n");
                report.append("ISBN: ").append(book.getIsbn()).append("\n");
                report.append("Title: ").append(book.getTitle()).append("\n");
                report.append("Author: ").append(book.getAuthor()).append("\n");
                report.append("Year: ").append(book.getYear()).append("\n");
                report.append("Genre: ").append(book.getGenre()).append("\n");
                report.append("Publisher: ").append(book.getPublisher()).append("\n");
                report.append("Available quantity: ").append(book.getAvailableQuantity()).append("\n");
                report.append("Total quantity: ").append(book.getTotalQuantity()).append("\n");
                report.append("\n\n");
            }
            logger.info("User {} requested books report", loggedUser);
        }
        else if(event.getSource() == rep_users) {
            List<User> users = userService.getAllUsers();
            for(User user : users) {
                List<Borrowing> borrowings = borrowingService.getAllBorrowingsByUser(user.getUserId());
                report.append("ID: ").append(user.getUserId()).append("\n");
                report.append("Date: ").append(user.getApprovalDate()).append("\n");
                report.append("User information: \n");
                report.append("Email: ").append(user.getEmail()).append("\n");
                report.append("Name: ").append(user.getName()).append("\n");
                report.append("Phone number: ").append(user.getPhoneNumber()).append("\n");
                for(Borrowing borrowing : borrowings) {
                    report.append("Id: ").append(borrowing.getBorId()).append("\n");
                    report.append("Date of borrowing").append(borrowing.getBorrowingDate()).append("\n");
                    report.append("Status: ").append(borrowing.getStatus().getStatus()).append("\n");
                    report.append("Book information: \n");
                    report.append("Title: ").append(borrowing.getBook().getTitle()).append(" ");
                    report.append("Author: ").append(borrowing.getBook().getAuthor()).append("\n\n");
                }
                report.append("\n\n");
            }
            logger.info("User {} requested users report", loggedUser);
        }
        else if(event.getSource() == rep_rating) {
            logger.info("User {} requested user rating report", loggedUser);
        }
        rep_report.setText(report.toString());
    }

}
