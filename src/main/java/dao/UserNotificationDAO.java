package dao;

import entity.UserNotification;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class UserNotificationDAO {
    public void saveUserNotification(UserNotification userNotification) {
        Transaction transaction = null;
        if (userNotification == null) {
            throw new IllegalArgumentException("UserNotification is null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(userNotification);
            System.out.println(userNotification.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteUserNotification(UserNotification userNotification) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            UserNotification un = session.get(UserNotification.class,
                    userNotification.getId());
            if (un != null) {
                session.delete(un);
            } else {
                System.out.println("UserNotification with ID " +
                        userNotification.getId() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<UserNotification> getAllUserNotifications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from UserNotification",
                    UserNotification.class).list();
        }
    }

    public List<UserNotification> getUserNotificationsByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from UserNotification where " +
                                    "user.userId = :userId", UserNotification.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}
