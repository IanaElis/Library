package unit.dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import dao.BorrowingDAO;
import entity.Borrowing;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BorrowingDAOTest {


    @Mock
    private Session mockSession;
    @Mock
    private Transaction mockTransaction;
    @Mock
    private Query<Borrowing> mockQuery;

    private BorrowingDAO borrowingDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        HibernateUtil.setSessionFactory(mock(org.hibernate.SessionFactory.class));
        when(HibernateUtil.getSessionFactory().openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        borrowingDAO = new BorrowingDAO();
    }

    @Test
    public void countAllOverdue_Success() {
        when(mockSession.createQuery("FROM Borrowing WHERE status.id = 3", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new Borrowing(), new Borrowing()));

        int result = borrowingDAO.countAllOverdue();

        assertEquals(2, result);
    }

    @Test
    public void getAllBorrowingsByReader_Success() {
        Borrowing borrowing = new Borrowing();

        when(mockSession.createQuery("from Borrowing WHERE user.userId =: id", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(borrowing));

        List<Borrowing> result = borrowingDAO.getAllBorrowingsByReader(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void getOverdueBorrowingsByReader_Success() {
        Borrowing borrowing = new Borrowing();

        when(mockSession.createQuery("from Borrowing WHERE user.userId =: id AND expectedReturnDate <= CURRENT_DATE", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(borrowing));

        List<Borrowing> result = borrowingDAO.getOverdueBorrowingsByReader(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void isBookAlreadyBorrowedByUser_Success() {
        Borrowing borrowing = new Borrowing();

        when(mockSession.createQuery("from Borrowing WHERE user.userId =: id AND book.id =: bookId AND book.status.id != 2", Borrowing.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.setParameter("bookId", 2)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(borrowing));

        boolean result = borrowingDAO.isBookAlreadyBorrowedByUser(1, 2);

        assertTrue(result);
    }

    @Test
    public void isBookAlreadyBorrowedByUser_NotBorrowed() {
        when(mockSession.createQuery("from Borrowing WHERE user.userId =: id AND book.id =: bookId AND book.status.id != 2", Borrowing.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.setParameter("bookId", 2)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of());

        boolean result = borrowingDAO.isBookAlreadyBorrowedByUser(1, 2);

        assertFalse(result);
    }

    @Test
    public void numberBorrowingsOverdue_Success() {
        User user = new User();
        user.setUserId(1);

        when(mockSession.createQuery("FROM Borrowing WHERE user.id =: id AND status.id = 3", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new Borrowing(), new Borrowing()));

        int result = borrowingDAO.numberBorrowingsOverdue(user);

        assertEquals(2, result);
    }

    @Test
    public void getBorrowedBooksCountByUser_Success() {
        when(mockSession.createQuery("FROM Borrowing WHERE user.id =: id AND status.id != 3", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new Borrowing(), new Borrowing()));

        int result = borrowingDAO.getBorrowedBooksCountByUser(1);

        assertEquals(2, result);
    }

    @Test
    public void getLateReturnCountByUser_Success() {
        when(mockSession.createQuery("FROM Borrowing WHERE user.id =: id AND status.id = 3", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new Borrowing()));

        int result = borrowingDAO.getLateReturnCountByUser(1);

        assertEquals(1, result);
    }

    @Test
    public void getDamagedBooksCountByUser_Success() {
        when(mockSession.createQuery("FROM Borrowing WHERE user.id =: id AND isDamaged = true", Borrowing.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new Borrowing()));

        int result = borrowingDAO.getDamagedBooksCountByUser(1);

        assertEquals(1, result);
    }
}