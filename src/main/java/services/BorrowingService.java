package services;

import dao.*;
import entity.Book;
import entity.Borrowing;
import entity.Notification;
import entity.User;

import java.time.LocalDate;
import java.util.List;

public class BorrowingService {
    private UserDAO userDAO;
    private BookDAO bookDAO;
    private BorrowingDAO borrowingDAO;
    private BorrowingStatusDAO borrowingStatusDAO;
    private BookService bookService;
    private NotificationService notificationService;
    private NotificationDAO notificationDAO;

    public BorrowingService() {
        userDAO = new UserDAO();
        bookDAO = new BookDAO();
        borrowingDAO = new BorrowingDAO();
        borrowingStatusDAO = new BorrowingStatusDAO();
        bookService = new BookService();
        notificationService = new NotificationService();
        notificationDAO = new NotificationDAO();
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
        String s;
        if(borrowing != null){
            Book book = bookDAO.getBookById(borrowing.getBook().getId());
            borrowing.setStatus(borrowingStatusDAO.getBorrowingStatusById(2));
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            if(borrowing.isDamaged()){
                book.setAvailableQuantity(book.getAvailableQuantity() - 1);
                borrowing.setDamaged(true);
                bookService.deleteBook(book);
                s = "Damaged book was written off";

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

    public void checkForOverdue(User user){
        List<Borrowing> borrowingList = borrowingDAO.getAllBorrowingsByReader(user.getUserId());
        for(Borrowing b : borrowingList){
            if(b.getExpectedReturnDate().isBefore(LocalDate.now())){
                if(b.getStatus().getBorStatId() != 3){
                    b.setStatus(borrowingStatusDAO.getBorrowingStatusById(3));
                    borrowingDAO.saveOrUpdate(b);
                }
            }
        }
        int number = borrowingDAO.numberBorrowingsOverdue(user);
        if(number != 0) {
            Notification ntfc = new Notification("You have "
                    + number + " borrowed books that you haven't returned on time",
                    LocalDate.now(), false);
            notificationDAO.saveOrUpdate(ntfc);
            notificationService.notifySpecificReader(ntfc, user);
        }
    }
}
