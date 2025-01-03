package dao;

import entity.RegisterForm;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class RegisterFormDAO extends BaseDAO<RegisterForm> {

    public RegisterForm getRegisterFormById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RegisterForm.class, id);
        }
    }

    public List<RegisterForm> getAllRegisterForms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RegisterForm", RegisterForm.class).list();
        }
    }

    public List<RegisterForm> getRegisterFormsByStatus(int statusId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RegisterForm where status.id = :statusId", RegisterForm.class)
                    .setParameter("statusId", statusId)
                    .list();
        }
    }

    public RegisterForm getRegisterFormsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RegisterForm where email =: e", RegisterForm.class)
                    .setParameter("e", email)
                    .uniqueResult();
        }
    }

    public RegisterForm getRegisterFormsByEmailPass(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RegisterForm where email =: e AND password =: p", RegisterForm.class)
                    .setParameter("e", email)
                    .setParameter("p", password)
                    .uniqueResult();
        }
    }
}
