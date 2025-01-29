package services;

import dao.BookDAO;
import dao.BookStatusDAO;
import entity.Book;
import entity.BookStatus;
import entity.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO;
    private final BookStatusDAO bookStatusDAO;
    private final NotificationService notificationService;
    private static final Logger logger = LogManager.getLogger(BookService.class);

    public BookService(BookDAO bd, BookStatusDAO bsd, NotificationService ns) {
        bookDAO = bd;
        bookStatusDAO = bsd;
        notificationService = ns;
    }

    public BookStatus getBookStatus(int id) {
        return bookStatusDAO.getBookStatus(id);
    }

    public String addBook(Book book) {
        if(bookDAO.getBookByTitle(book.getTitle()) == null) {
            bookDAO.saveOrUpdate(book);
            return "Book added";
        }
        else return "Book already exists";
    }

    public String archiveBook(Book book) {
        String s;
        if( book!= null) {
            if(book.getStatus().getId() != 1){
                LocalDate date = LocalDate.now();
                if((date.getYear() - book.getYear().getYear()) > 40) {
                    book.setStatus(new BookStatusDAO().getBookStatus(1));
                    bookDAO.saveOrUpdate(book);
                    s = "";
                }
                else{
                    s = "The book must be 40+ years old to be archived";
                    //raise alert and ask if the user wants to proceed
                    //if yes then go to method "archiveYoungBook"
                }
            }
            else s = "Book is already archived";

        }
        else s = "Book is null";
        return s;
    }

    public void archiveYoungBook(Book book) {
        book.setStatus(new BookStatusDAO().getBookStatus(1));
        bookDAO.saveOrUpdate(book);
    }

    public void updateBook(Book book) {
        if(book != null) {
            bookDAO.saveOrUpdate(book);
        }
        else{
            logger.error("The book cannot be updated because it is null");
        }
    }

    public void deleteBook(Book book) {
        if(book != null) {
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            book.setTotalQuantity(book.getTotalQuantity()-1);
            bookDAO.saveOrUpdate(book);
        }
        else{
            logger.error("The book cannot be updated because it is null");
        }
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public void needToArchive() {
        List<Book> books = this.getAllBooks();
        boolean needToArchive = false;
        LocalDate date = LocalDate.now();
        for(Book book : books) {
            if ((date.getYear() - book.getYear().getYear()) > 40 && book.getStatus().getId() == 2) {
                needToArchive = true;
                break;
            }
        }
        if(needToArchive) {
            Notification ntfc = notificationService.getNotificationById(3);
            notificationService.notifyAdminsOperators(ntfc, null);
        }
    }

    public List<Book> booksToArchive() {
        List<Book> books = this.getAllBooks();
        List<Book> toBeArchived = new ArrayList<>();
        LocalDate date = LocalDate.now();
        for(Book book : books) {
            if((date.getYear() - book.getYear().getYear()) > 40 && book.getStatus().getId() == 2){
                toBeArchived.add(book);
            }
        }
        return toBeArchived;
    }

    public int countAllBooks(){
        return bookDAO.countAllBooks();
    }

}
