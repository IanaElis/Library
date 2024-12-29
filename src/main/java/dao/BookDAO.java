package dao;

import entity.Book;
import org.hibernate.Session;
import util.HibernateUtil;
import java.util.List;

public class BookDAO extends BaseDAO<Book> {
 /*   public void saveBook(Book book) {
        Transaction transaction = null;
        if (book == null) {
            throw new IllegalArgumentException("Book is null");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(book);
            System.out.println(book.toString());
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteBook(Book book) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Book b = session.get(Book.class, book.getId());
            if (b != null) {
                session.delete(b);
            } else {
                System.out.println("Book with ID " + book.getId() + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
*/

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
