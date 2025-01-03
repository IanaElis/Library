package dao;

import entity.Borrowing;
import entity.Notification;
import entity.UserNotification;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class NotificationDAO extends BaseDAO<Notification> {

    public Notification getNotificationById(int notifID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Notification.class, notifID);
        }
    }

    public List<Notification> getAllNotifications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Notification", Notification.class).list();
        }
    }


}
