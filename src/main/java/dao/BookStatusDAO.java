package dao;

import entity.BookStatus;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class BookStatusDAO extends BaseDAO<BookStatus> {

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
