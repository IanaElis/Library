package dao;

import entity.UserNotification;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class UserNotificationDAO extends BaseDAO<UserNotification> {

    public List<UserNotification> getUserNotificationsByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from UserNotification where " +
                                    "user.userId = :userId", UserNotification.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public int getUnreadNotificationCount(int userId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserNotification WHERE user.userId = :id AND isRead = false", UserNotification.class)
                    .setParameter("id", userId)
                    .list().size();
        }
    }
}
