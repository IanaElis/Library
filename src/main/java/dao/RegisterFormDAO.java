package dao;

import entity.RegisterForm;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class RegisterFormDAO extends BaseDAO<RegisterForm> {
 /*   public boolean saveRegisterForm(RegisterForm registerForm) {
        Transaction transaction = null;
        if(registerForm == null) throw new IllegalArgumentException("registerForm is null");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(registerForm);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public void updateRegisterForm(RegisterForm registerForm) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(registerForm);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRegisterForm(RegisterForm registerForm) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            RegisterForm form = session.get(RegisterForm.class, registerForm.getRegFormId());
            if (form != null) {
                session.delete(form);
            } else {
                System.out.println("Register Form with ID " + registerForm.getRegFormId() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }
    }
*/
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

    public RegisterForm getRegisterFormsByEmailPass(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RegisterForm where email =: e AND password =: p", RegisterForm.class)
                    .setParameter("e", email)
                    .setParameter("p", password)
                    .getSingleResult();
        }
    }
}
