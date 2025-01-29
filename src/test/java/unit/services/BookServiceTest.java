package unit.services;

import dao.BookDAO;
import dao.BookStatusDAO;
import entity.Book;
import entity.BookStatus;
import entity.Notification;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import services.BookService;
import services.NotificationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookDAO mockBookDAO;
    @Mock
    private BookStatusDAO mockBookStatusDAO;
    @Mock
    private NotificationService mockNotificationService;
    @Mock
    private Notification mockNotification;

    private BookService bookService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(mockBookDAO,mockBookStatusDAO, mockNotificationService);
    }

    @Test
    public void addBook_newBook() {
        Book mockBook = new Book();
        mockBook.setTitle("Test Book");

        when(mockBookDAO.getBookByTitle("Test Book")).thenReturn(null);

        String result = bookService.addBook(mockBook);

        assertEquals("Book added", result);
        verify(mockBookDAO).saveOrUpdate(mockBook);
    }

    @Test
    public void addBook_existingBook() {
        Book mockBook = new Book();
        mockBook.setTitle("Test Book");

        when(mockBookDAO.getBookByTitle("Test Book")).thenReturn(mockBook);

        String result = bookService.addBook(mockBook);

        assertEquals("Book already exists", result);
        verify(mockBookDAO, never()).saveOrUpdate(mockBook);
    }

    @Test
    public void archiveBook_bookAlreadyArchived() {
        Book mockBook = new Book();
        BookStatus bookStatus = new BookStatus();
        bookStatus.setId(1);
        mockBook.setStatus(bookStatus);

        String result = bookService.archiveBook(mockBook);

        assertEquals("Book is already archived", result);
        verify(mockBookDAO, never()).saveOrUpdate(mockBook);
    }

    @Test
    public void archiveBook_bookTooYoungToArchive() {
        Book mockBook = new Book();
        mockBook.setYear(LocalDate.of(2010, 1, 1));

        BookStatus bookStatus = new BookStatus();
        bookStatus.setId(2);
        mockBook.setStatus(bookStatus);

        String result = bookService.archiveBook(mockBook);

        assertEquals("The book must be 40+ years old to be archived", result);
        verify(mockBookDAO, never()).saveOrUpdate(mockBook);
    }

    @Test
    public void archiveBook_bookEligibleForArchiving() {
        Book mockBook = new Book();
        mockBook.setYear(LocalDate.of(1980, 1, 1));

        BookStatus bookStatus = new BookStatus();
        bookStatus.setId(2);
        mockBook.setStatus(bookStatus);

        when(mockBookStatusDAO.getBookStatus(1)).thenReturn(new BookStatus(1,null));

        String result = bookService.archiveBook(mockBook);

        assertEquals("", result);
        verify(mockBookDAO).saveOrUpdate(mockBook);
    }

    @Test
    public void archiveYoungBook() {
        Book mockBook = new Book();
        BookStatus bookStatus = new BookStatus();
        bookStatus.setId(1);
        mockBook.setStatus(bookStatus);

        bookService.archiveYoungBook(mockBook);

        verify(mockBookDAO).saveOrUpdate(mockBook);
    }

    @Test
    public void updateBook_bookNotNull() {
        Book mockBook = new Book();

        bookService.updateBook(mockBook);

        verify(mockBookDAO).saveOrUpdate(mockBook);
    }

    @Test
    public void updateBook_bookNull() {
        bookService.updateBook(null);

        verify(mockBookDAO, never()).saveOrUpdate(null);
    }

    @Test
    public void deleteBook_existingBook() {
        Book mockBook = new Book();
        mockBook.setAvailableQuantity(5);
        mockBook.setTotalQuantity(10);

        bookService.deleteBook(mockBook);

        assertEquals(4, mockBook.getAvailableQuantity());
        assertEquals(9, mockBook.getTotalQuantity());
        verify(mockBookDAO).saveOrUpdate(mockBook);
    }

    @Test
    public void deleteBook_nullBook() {
        Book mockBook = null;

        bookService.deleteBook(mockBook);

        verify(mockBookDAO, never()).saveOrUpdate(mockBook);
    }

    @Test
    public void needToArchive_booksToBeArchived() {
        Book mockBook1 = new Book();
        mockBook1.setYear(LocalDate.of(1970, 1, 1));
        BookStatus bookStatus1 = new BookStatus();
        bookStatus1.setId(2);
        mockBook1.setStatus(bookStatus1);

        Book mockBook2 = new Book();
        mockBook2.setYear(LocalDate.of(1990, 1, 1));
        BookStatus bookStatus2 = new BookStatus();
        bookStatus2.setId(2);
        mockBook2.setStatus(bookStatus2);

        when(mockBookDAO.getAllBooks()).thenReturn(Arrays.asList(mockBook1, mockBook2));
        when(mockNotificationService.getNotificationById(3)).thenReturn(mockNotification);

        bookService.needToArchive();

        verify(mockNotificationService).notifyAdminsOperators(mockNotification, null);
    }

    @Test
    public void needToArchive_noBooksToBeArchived() {
        Book mockBook1 = new Book();
        mockBook1.setYear(LocalDate.of(1990, 1, 1));
        BookStatus bookStatus1 = new BookStatus();
        bookStatus1.setId(2);
        mockBook1.setStatus(bookStatus1);

        Book mockBook2 = new Book();
        mockBook2.setYear(LocalDate.of(2010, 1, 1));
        BookStatus bookStatus2 = new BookStatus();
        bookStatus2.setId(2);
        mockBook1.setStatus(bookStatus2);

        when(mockBookDAO.getAllBooks()).thenReturn(Arrays.asList(mockBook1, mockBook2));

        bookService.needToArchive();

        verify(mockNotificationService, never()).notifyAdminsOperators(any(), any());
    }

    @Test
    public void booksToArchive_booksToBeArchived() {
        Book mockBook1 = new Book();
        mockBook1.setYear(LocalDate.of(1970, 1, 1));
        BookStatus bookStatus1 = new BookStatus();
        bookStatus1.setId(2);
        mockBook1.setStatus(bookStatus1);

        Book mockBook2 = new Book();
        mockBook2.setYear(LocalDate.of(1990, 1, 1)); // Not old enough
        BookStatus bookStatus2 = new BookStatus();
        bookStatus2.setId(2);
        mockBook1.setStatus(bookStatus2);

        List<Book> books = Arrays.asList(mockBook1, mockBook2);

        when(mockBookDAO.getAllBooks()).thenReturn(books);

        List<Book> result = bookService.booksToArchive();

        assertEquals(1, result.size());
        assertEquals(mockBook1, result.get(0));
    }

}