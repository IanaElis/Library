package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public abstract class BaseDAO<T> implements DAO<T> {

    public boolean saveOrUpdate(T entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Provided object is null");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public void delete(T entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Provided object is null");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}


