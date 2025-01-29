package unit.dao;

import dao.NotificationDAO;
import entity.Notification;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.HibernateUtil;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;;

public class NotificationDAOTest {

    @Mock
    private Session mockSession;

    private NotificationDAO notificationDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        HibernateUtil.setSessionFactory(mock(org.hibernate.SessionFactory.class));
        when(HibernateUtil.getSessionFactory().openSession()).thenReturn(mockSession);
        notificationDAO = new NotificationDAO();
    }

    @Test
    public void getNotificationById_Success() {
        Notification mockNotification = new Notification();
        mockNotification.setNotifID(1);
        mockNotification.setMessage("Test Notification");

        when(mockSession.get(Notification.class, 1)).thenReturn(mockNotification);

        Notification result = notificationDAO.getNotificationById(1);

        assertNotNull(result);
        assertEquals(1, result.getNotifID());
        assertEquals("Test Notification", result.getMessage());
    }

    @Test
    public void getNotificationById_NotFound() {
        when(mockSession.get(Notification.class, 1)).thenReturn(null);

        Notification result = notificationDAO.getNotificationById(1);

        assertNull(result);
    }

}