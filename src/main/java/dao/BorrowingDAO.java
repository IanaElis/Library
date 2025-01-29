package dao;

import entity.Borrowing;
import entity.User;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class BorrowingDAO extends BaseDAO<Borrowing> {

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

    public int countAllOverdue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Borrowing WHERE status.id = 3", Borrowing.class).list().size();
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

    public List<Borrowing> getOverdueBorrowingsByReader(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Borrowing WHERE user.userId =: id AND expectedReturnDate <= CURRENT_DATE", Borrowing.class)
                    .setParameter("id", userId)
                    .list();
        }
    }

    public boolean isBookAlreadyBorrowedByUser(int userId, int bookId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        //    try {
                List<Borrowing> b = session.createQuery("from Borrowing WHERE " +
                                "user.userId =: id AND book.id =: bookId AND book.status.id != 2", Borrowing.class)
                        .setParameter("id", userId)
                        .setParameter("bookId", bookId).list();
                if(b == null || b.isEmpty())
                    return false;
                else return true;
        }
    }

    public int numberBorrowingsOverdue(User user){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Borrowing> b = session.createQuery("FROM Borrowing WHERE user.id =: id AND status.id = 3", Borrowing.class)
                    .setParameter("id", user.getUserId())
                    .list();
            return b.size();
        }
    }

    public int getBorrowedBooksCountByUser(int userId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Borrowing WHERE user.id =: id AND " +
                    "status.id != 3", Borrowing.class)
                    .setParameter("id", userId)
                    .list().size();
        }
    }

    public int getLateReturnCountByUser(int userId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Borrowing WHERE user.id =: id AND " +
                            "status.id = 3", Borrowing.class)
                    .setParameter("id", userId)
                    .list().size();
        }
    }

    public int getDamagedBooksCountByUser(int userId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Borrowing WHERE user.id =: id AND " +
                            "isDamaged = true", Borrowing.class)
                    .setParameter("id", userId)
                    .list().size();
        }
    }
}
