package unit.dao;

import dao.UserNotificationDAO;
import entity.Notification;
import entity.UserNotification;
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

public class UserNotificationDAOTest {

    @Mock
    private Session mockSession;

    @Mock
    private Query<UserNotification> mockQuery;

    private UserNotificationDAO userNotificationDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        HibernateUtil.setSessionFactory(mock(org.hibernate.SessionFactory.class));
        when(HibernateUtil.getSessionFactory().openSession()).thenReturn(mockSession);
        userNotificationDAO = new UserNotificationDAO();
    }

    @Test
    public void getUserNotificationsByUserId_Success() {
        UserNotification notification1 = new UserNotification();
        Notification n1 = new Notification();
        n1.setMessage("Notification 1");
        notification1.setId(1);
        notification1.setNotification(n1);

        UserNotification notification2 = new UserNotification();
        Notification n2 = new Notification();
        n2.setMessage("Notification 2");
        notification2.setId(2);
        notification2.setNotification(n2);

        when(mockSession.createQuery("from UserNotification where user.userId = :userId", UserNotification.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("userId", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(notification1, notification2));

        List<UserNotification> result = userNotificationDAO.getUserNotificationsByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Notification 1", result.get(0).getNotification().getMessage());
        assertEquals("Notification 2", result.get(1).getNotification().getMessage());
    }

    @Test
    public void getUserNotificationsByUserId_NoNotifications() {
        when(mockSession.createQuery("from UserNotification where user.userId = :userId", UserNotification.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("userId", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList());

        List<UserNotification> result = userNotificationDAO.getUserNotificationsByUserId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getUnreadNotificationCount_Success() {
        UserNotification notification1 = new UserNotification();
        notification1.setId(1);
        notification1.setRead(false);

        UserNotification notification2 = new UserNotification();
        notification1.setId(2);
        notification1.setRead(false);

        when(mockSession.createQuery("FROM UserNotification WHERE user.userId = :id AND isRead = false", UserNotification.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList(notification1, notification2));

        int result = userNotificationDAO.getUnreadNotificationCount(1);

        assertEquals(2, result);
    }

    @Test
    public void getUnreadNotificationCount_NoUnreadNotifications() {
        when(mockSession.createQuery("FROM UserNotification WHERE user.userId = :id AND isRead = false", UserNotification.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("id", 1)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Arrays.asList());

        int result = userNotificationDAO.getUnreadNotificationCount(1);

        assertEquals(0, result);
    }

}