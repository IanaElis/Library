package dao;

import entity.Borrowing;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import java.util.List;

public class BorrowingDAO extends BaseDAO<Borrowing> {
    /*
    public void saveBorrowing(Borrowing borrowing) {
        Transaction transaction = null;
        if (borrowing == null) {
            throw new IllegalArgumentException("Borrowing is null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(borrowing);
            System.out.println(borrowing.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteBorrowing(Borrowing borrowing) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Borrowing b = session.get(Borrowing.class, borrowing.getBorId());
            if (b != null) {
                session.delete(b);
            } else {
                System.out.println("Borrowing with ID " + borrowing.getBorId() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
     */

    public Borrowing getBorrowingById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Borrowing.class, id);
        }
    }

    public List<Borrowing> getAllBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Borrowing", Borrowing.class).list();
        }
    }

    public List<Borrowing> getDamagedBooks(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Borrowing WHERE isDamaged =: d", Borrowing.class)
                    .setParameter("d", true)
                    .list();
        }
    }

    public List<Borrowing> getActualBorrowingsByReader(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Borrowing WHERE user.userId =: id AND status.borStatId =: bId", Borrowing.class)
                    .setParameter("id", userId)
                    .setParameter("bId", 1)
                    .list();
        }
    }

    public List<Borrowing> getAllBorrowingsByReader(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Borrowing WHERE user.userId =: id", Borrowing.class)
                    .setParameter("id", userId)
                    .list();
        }
    }

    public boolean isBookAlreadyBorrowedByUser(int userId, int bookId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                List<Borrowing> b = session.createQuery("from Borrowing WHERE " +
                                "user.userId =: id AND book.id =: bookId ", Borrowing.class)
                        .setParameter("id", userId)
                        .setParameter("bookId", bookId).list();
                System.out.println(b.size());
                return true;
            }
            catch(NoResultException e) {
                return false;
            }

        }
    }
}
