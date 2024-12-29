package dao;

import entity.BorrowingStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class BorrowingStatusDAO extends BaseDAO<BorrowingStatus> {
    /*
    public void saveBorrowingStatus(BorrowingStatus borrowingStatus) {
        Transaction transaction = null;
        if (borrowingStatus == null) {
            throw new IllegalArgumentException("BorrowingStatus is null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(borrowingStatus);
            System.out.println(borrowingStatus.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteBorrowingStatus(BorrowingStatus borrowingStatus) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            BorrowingStatus bs = session.get(BorrowingStatus.class, borrowingStatus.getBorStatId());
            if (bs != null) {
                session.delete(bs);
            } else {
                System.out.println("BorrowingStatus with ID " + borrowingStatus.getBorStatId() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
*/

    public BorrowingStatus getBorrowingStatusById(int borStatId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(BorrowingStatus.class, borStatId);
        }
    }

    public List<BorrowingStatus> getAllBorrowingStatuses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from BorrowingStatus", BorrowingStatus.class).list();
        }
    }
}
