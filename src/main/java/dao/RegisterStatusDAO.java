package dao;

import entity.RegisterForm;
import entity.RegisterStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class RegisterStatusDAO extends BaseDAO<RegisterStatus> {

    public RegisterStatus getRegisterStatusById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RegisterStatus.class, id);
        }
    }

    public List<RegisterStatus> getAllRegisterStatuses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RegisterStatus", RegisterStatus.class).list();
        }
    }
}
