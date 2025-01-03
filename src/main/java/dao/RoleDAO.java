package dao;

import entity.Role;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class RoleDAO extends BaseDAO<Role>{

    public Role getRoleById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Role.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Role> getAllRoles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Role", Role.class).list();
        }
    }
}
