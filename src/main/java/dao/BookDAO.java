package dao;

import entity.Book;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class BookDAO extends BaseDAO<Book> {

    public Book getBookById(int bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, bookId);
        }
    }

    public Book getBookByTitle(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Book WHERE title =: t", Book.class)
                    .setParameter("t", title)
                    .getSingleResult();
        }
    }

    public List<Book> getAllBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Book", Book.class).list();
        }
    }
}
