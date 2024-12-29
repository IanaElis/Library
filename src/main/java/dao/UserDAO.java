package dao;

import entity.User;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class UserDAO extends BaseDAO<User> {
/*
    public static void saveUser(User user) {
        Transaction transaction = null;
        if(user == null) throw new IllegalArgumentException("user is null");
        try (Session session = HibernateUtil.getSessionFactory()
                .openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            System.out.println(user.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

        public void updateUser(User user) {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory()
                    .openSession()) {
                transaction = session.beginTransaction();
                session.update(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
            }
        }

        public void deleteUser(User user) {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                User u = session.get(User.class, user.getUserId());
                if(u != null) {
                    session.delete(u);
                }
                else {
                System.out.println("User with ID " + user.getUserId() + " not found.");
            }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
            }
        }
*/
        public User getUserById(int id) {
            try (Session session = HibernateUtil.getSessionFactory()
                    .openSession()) {
                return session.get(User.class, id);
            }
        }

        public List<User> getAllUsers() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery("FROM User", User.class).list();
            }
        }

    public List<User> getAllOperators() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE  role.roleID = :roleId", User.class)
                    .setParameter("roleId", 2)
                    .list();
        }
    }

    public List<User> getAllReaders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE  role.roleID = :roleId", User.class)
                    .setParameter("roleId", 1)
                    .list();
        }
    }

        public User getUserByEmail(String email) {
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty.");
            }
            try (Session session = HibernateUtil.getSessionFactory()
                    .openSession()) {
                return session.createQuery("from User where email = :email", User.class)
                        .setParameter("email", email)
                        .uniqueResult();
            }
        }
}
