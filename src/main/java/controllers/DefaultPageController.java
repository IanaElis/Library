package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.BookService;
import services.BorrowingService;
import services.UserService;

public class DefaultPageController {

    @FXML
    private Label stat_books_n;
    @FXML
    private Label stat_overdue_n;
    @FXML
    private Label stat_readers_n;

    private BorrowingService borrowingService;
    private BookService bookService;
    private UserService userService;

    public void setParam(BorrowingService brs, BookService bs, UserService us){
        borrowingService = brs;
        bookService = bs;
        userService = us;
    }

    public void showStatistics(){
        stat_books_n.setText(String.valueOf(bookService.countAllBooks()));
        stat_overdue_n.setText(String.valueOf(borrowingService.countAllOverdue()));
        stat_readers_n.setText(String.valueOf(userService.countAllReaders()));
    }
}
