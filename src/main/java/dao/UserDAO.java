package dao;

import entity.User;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class UserDAO extends BaseDAO<User> {

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

    public List<User> getAllAdmins() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE  role.roleID = :roleId", User.class)
                    .setParameter("roleId", 3)
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
