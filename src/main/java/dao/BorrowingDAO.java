package dao;

import entity.Borrowing;
import entity.User;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.NoResultException;
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
                System.out.println("b list res from db" + b);
                if(b.size() == 0){
                    return false;
                }
                return true;
            }
            catch(NoResultException e) {
                return false;
            }

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
}
