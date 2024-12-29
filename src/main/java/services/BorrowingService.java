package services;

import dao.BookDAO;
import dao.BorrowingDAO;
import dao.BorrowingStatusDAO;
import dao.UserDAO;
import entity.Book;
import entity.Borrowing;
import entity.BorrowingStatus;
import entity.User;

import java.time.LocalDate;
import java.util.List;

public class BorrowingService {
    private UserDAO userDAO;
    private BookDAO bookDAO;
    private BorrowingDAO borrowingDAO;
    private BorrowingStatusDAO borrowingStatusDAO;
    private BookService bookService;

    public BorrowingService() {
        userDAO = new UserDAO();
        bookDAO = new BookDAO();
        borrowingDAO = new BorrowingDAO();
        borrowingStatusDAO = new BorrowingStatusDAO();
        bookService = new BookService();
    }

    public String issueBook(int bookId, int userId) {
        Book dbBookCheck = bookDAO.getBookById(bookId);
        User dbUserCheck = userDAO.getUserById(userId);
        if(!borrowingDAO.isBookAlreadyBorrowedByUser(userId, bookId)) {
            LocalDate now = LocalDate.now();
            if (dbBookCheck != null) {
                if (dbBookCheck.getAvailableQuantity() > 1) {
                    if (dbUserCheck != null) {
                        Borrowing borrowing = new Borrowing(borrowingStatusDAO.
                                getBorrowingStatusById(1),
                                userDAO.getUserById(userId), bookDAO.getBookById(bookId),
                                now, now.plusDays(30), null, false);
                        borrowingDAO.saveOrUpdate(borrowing);
                        dbBookCheck.setAvailableQuantity(dbBookCheck.getAvailableQuantity() - 1);
                        bookDAO.saveOrUpdate(dbBookCheck);
                        return "Book issued to " + dbUserCheck.getName();
                    }
                    return "User not found ";
                }
                return "Book is not available";
            }
            return "Book not found";
        }
        return "This book has already been borrowed and not returned yet";
    }

    public String returnBook(Borrowing borrowing) {
        String s = null;
        if(borrowing != null){
            Book book = bookDAO.getBookById(borrowing.getBook().getId());
            borrowing.setStatus(borrowingStatusDAO.getBorrowingStatusById(2));
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            if(borrowing.isDamaged()){
                book.setAvailableQuantity(book.getAvailableQuantity() - 1);
                borrowing.setDamaged(true);
                bookService.deleteBook(book);
                //is damaged to Borrowing, not BOOK
                s = "Damaged book was written off";

            }
            else if(borrowing.getExpectedReturnDate().isBefore(LocalDate.now())) {
                //borrowing.getBook().getStatus().getId() == 3
                borrowing.setStatus(borrowingStatusDAO.getBorrowingStatusById(3));
                s = "Book is overdue";
                //ranking value decrease
            }
            else s = "Book returned successfully";

            borrowing.setActualReturnDate(LocalDate.now());
            borrowingDAO.saveOrUpdate(borrowing);
            bookDAO.saveOrUpdate(book);
            return s;
        }
        else return "Borrowing not found";
    }

    public List<Borrowing> getActualBorrowingsByUser(int userId) {
        return borrowingDAO.getActualBorrowingsByReader(userId);
    }


    public List<Borrowing> getAllBorrowingsByUser(int userId) {
        return borrowingDAO.getAllBorrowingsByReader(userId);
    }
}
