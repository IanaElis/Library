package dao;

import entity.Notification;
import org.hibernate.Session;
import util.HibernateUtil;

public class NotificationDAO extends BaseDAO<Notification> {

    public Notification getNotificationById(int notifID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Notification.class, notifID);
        }
    }

}
