package dao;

import entity.RegisterStatus;
import org.hibernate.Session;
import util.HibernateUtil;

public class RegisterStatusDAO extends BaseDAO<RegisterStatus> {

    public RegisterStatus getRegisterStatusById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RegisterStatus.class, id);
        }
    }

}
