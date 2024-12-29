package dao;

import entity.BookStatus;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class BookStatusDAO extends BaseDAO<BookStatus> {
    /*
    public void saveBookStatus(BookStatus bookStatus) {
        Transaction transaction = null;
        if (bookStatus == null) {
            throw new IllegalArgumentException("BookStatus is null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookStatus);
            System.out.println(bookStatus.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteBookStatus(BookStatus bookStatus) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            BookStatus bs = session.get(BookStatus.class, bookStatus.getId());
            if (bs != null) {
                session.delete(bs);
            } else {
                System.out.println("BookStatus with ID " + bookStatus.getId() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
*/
    public BookStatus getBookStatus(int statusId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(BookStatus.class, statusId);
        }
    }

    public List<BookStatus> getAllBookStatuses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from BookStatus", BookStatus.class).list();
        }
    }
}
