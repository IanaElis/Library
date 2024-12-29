package dao;

import entity.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class RoleDAO {
    public void saveRole(Role role) {
        Transaction transaction = null;
        if(role == null ) throw new IllegalArgumentException("Role cannot be null");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateRole(Role role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRole(Role role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Role r = session.get(Role.class, role.getRoleID());
            if(r != null) {
                session.delete(r);
            }
            else {
            System.out.println("Role with ID " + role.getRoleID() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Role getRoleById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            return session.get(Role.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
    }

    public List<Role> getAllRoles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Role", Role.class).list();
        }
    }
}
