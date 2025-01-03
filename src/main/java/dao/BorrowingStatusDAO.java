package dao;

import entity.BorrowingStatus;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class BorrowingStatusDAO extends BaseDAO<BorrowingStatus> {

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
