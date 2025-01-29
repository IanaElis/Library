package unit.dao;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import dao.BookDAO;
import entity.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDAOTest {
    @Mock
    private Session mockSession;
    @Mock
    private Transaction mockTransaction;
    @Mock
    private Query<Book> mockQuery;

    private BookDAO bookDAO;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        HibernateUtil.setSessionFactory(mock(org.hibernate.SessionFactory.class));
        when(HibernateUtil.getSessionFactory().openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        bookDAO = new BookDAO();
    }

    @Test
    public void saveOrUpdate_Success() {
        Book mockBook = new Book();
        mockBook.setId(1);
        mockBook.setTitle("New Book");

        doNothing().when(mockSession).saveOrUpdate(mockBook);

        boolean result = bookDAO.saveOrUpdate(mockBook);

        assertTrue(result);
        verify(mockSession).saveOrUpdate(mockBook);
        verify(mockTransaction).commit();
    }

    @Test
    public void delete_Success() {
        Book mockBook = new Book();
        mockBook.setId(1);
        mockBook.setTitle("To Be Deleted");

        bookDAO.delete(mockBook);

        verify(mockSession).delete(mockBook);
        verify(mockTransaction).commit();
    }

    @Test
    public void getBookById() {
        Book mockBook = new Book();
        mockBook.setId(1);
        mockBook.setTitle("Sample Book");
        when(mockSession.get(Book.class, 1)).thenReturn(mockBook);

        Book result = bookDAO.getBookById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Sample Book", result.getTitle());
    }

    @Test
    public void getBookByTitle() {
        Book mockBook = new Book();
        mockBook.setId(2);
        mockBook.setTitle("Test Book");

        when(mockSession.createQuery("from Book WHERE title =: t", Book.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("t", "Test Book")).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(mockBook);

        Book result = bookDAO.getBookByTitle("Test Book");

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    public void getAllBooks() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Book 2");

        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        mockBooks.add(book2);

        when(mockSession.createQuery("from Book", Book.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockBooks);

        List<Book> result = bookDAO.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
    }

    @Test
    public void countAllBooks() {
        when(mockSession.createQuery("from Book", Book.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new Book(), new Book()));

        int result = bookDAO.countAllBooks();

        assertEquals(2, result);
    }
}