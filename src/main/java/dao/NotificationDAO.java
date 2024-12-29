package dao;

import entity.Notification;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class NotificationDAO {
    public void saveNotification(Notification notification) {
        Transaction transaction = null;
        if (notification == null) {
            throw new IllegalArgumentException("Notification is null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(notification);
            System.out.println(notification.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteNotification(Notification notification) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(notification);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

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
