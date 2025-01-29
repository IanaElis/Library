package unit.dao;

import dao.UserDAO;
import entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.HibernateUtil;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UserDAOTest {

    @Mock
    private Session mockSession;

    @Mock
    private Query<User> mockQuery;

    private UserDAO userDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        HibernateUtil.setSessionFactory(mock(org.hibernate.SessionFactory.class));
        when(HibernateUtil.getSessionFactory().openSession()).thenReturn(mockSession);
        userDAO = new UserDAO();
    }

    @Test
    public void getUserById_Success() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("user1@example.com");

        when(mockSession.get(User.class, 1)).thenReturn(mockUser);

        User result = userDAO.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("user1@example.com", result.getEmail());
    }

    @Test
    public void getUserById_NotFound() {
        when(mockSession.get(User.class, 1)).thenReturn(null);

        User result = userDAO.getUserById(1);

        assertNull(result);
    }

    @Test
    public void getAllUsers() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setUserId(2);
        user2.setEmail("user2@example.com");

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(mockSession.createQuery("FROM User", User.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockUsers);

        List<User> result = userDAO.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }

    @Test
    public void getAllOperators() {
        User operator = new User();
        operator.setUserId(2);
        operator.setEmail("operator@example.com");

        List<User> mockUsers = Arrays.asList(operator);

        when(mockSession.createQuery("FROM User WHERE  role.roleID = :roleId", User.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("roleId", 2)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockUsers);

        List<User> result = userDAO.getAllOperators();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("operator@example.com", result.get(0).getEmail());
    }

    @Test
    public void getAllAdmins() {
        User admin = new User();
        admin.setUserId(3);
        admin.setEmail("admin@example.com");

        List<User> mockUsers = Arrays.asList(admin);

        when(mockSession.createQuery("FROM User WHERE  role.roleID = :roleId", User.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("roleId", 3)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockUsers);

        List<User> result = userDAO.getAllAdmins();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin@example.com", result.get(0).getEmail());
    }

    @Test
    public void getAllReaders() {
        User reader = new User();
        reader.setUserId(1);
        reader.setEmail("reader@example.com");

        List<User> mockUsers = Arrays.asList(reader);

        when(mockSession.createQuery("FROM User WHERE  role.roleID = :roleId", User.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("roleId", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockUsers);

        List<User> result = userDAO.getAllReaders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("reader@example.com", result.get(0).getEmail());
    }

    @Test
    public void countAllReaders() {
        when(mockSession.createQuery("FROM User WHERE  role.roleID = 1", User.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(new User(), new User()));

        int result = userDAO.countAllReaders();

        assertEquals(2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserByEmail_EmptyEmail() {
        userDAO.getUserByEmail("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserByEmail_NullEmail() {
        userDAO.getUserByEmail(null);
    }

    @Test
    public void getUserByEmail_Success() {
        User mockUser = new User();
        mockUser.setEmail("user@example.com");
        mockUser.setUserId(1);

        when(mockSession.createQuery("from User where email = :email", User.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("email", "user@example.com")).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(mockUser);

        User result = userDAO.getUserByEmail("user@example.com");

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    public void getUserByEmail_NotFound() {
        when(mockSession.createQuery("from User where email = :email", User.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("email", "user@example.com")).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(null);

        User result = userDAO.getUserByEmail("user@example.com");

        assertNull(result);
    }

}