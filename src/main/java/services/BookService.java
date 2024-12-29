package services;

import dao.BookDAO;
import dao.BookStatusDAO;
import dao.BorrowingDAO;
import entity.Book;

import java.time.LocalDate;
import java.util.List;

public class BookService {
    private BookDAO bookDAO;
    private BorrowingDAO borrowingDAO;

    public BookService() {
        bookDAO = new BookDAO();
        borrowingDAO = new BorrowingDAO();
    }

    public String addBook(Book book) {
        if(bookDAO.getBookByTitle(book.getTitle()) == null) {
            bookDAO.saveOrUpdate(book);
            return "Book added";
        }
        else return "Book already exists";

    }

    public String archiveBook(Book book) {
        String s = null;
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
                    //rise alert and ask if the user wants to proceed
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
            System.out.println("Book is null");
        }
    }

    public void deleteBook(Book book) {
        if(book != null) {
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            book.setTotalQuantity(book.getTotalQuantity()-1);
            bookDAO.saveOrUpdate(book);
        }
        else{
            System.out.println("Book is null");
        }
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

}
