package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Borrowing")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrow_seq_gen")
    @SequenceGenerator(name = "borrow_seq_gen", sequenceName = "borrow_seq", allocationSize = 1)
    @Column(name = "borrowing_id")
    private int borId;

    @ManyToOne
    @JoinColumn(name = "Borrowing_Status_status_id",
            referencedColumnName = "status_id")
    private BorrowingStatus status;

    @ManyToOne
    @JoinColumn(name = "User_user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Book_book_id", referencedColumnName = "book_id")
    private Book book;

    @Column(name = "date_of_borrowing")
    private LocalDate borrowingDate;
    @Column(name = "expected_return")
    private LocalDate expectedReturnDate;
    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;
    @Column(name = "is_damaged")
    private boolean isDamaged;

    public Borrowing() {}

    public Borrowing(BorrowingStatus status, User user,
                     Book book, LocalDate borrowingDate,
                     LocalDate expectedReturnDate, LocalDate actualReturnDate, boolean isDamaged ) {
        this.status = status;
        this.user = user;
        this.book = book;
        this.borrowingDate = borrowingDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.isDamaged = isDamaged;
    }

    public Borrowing(int borId, BorrowingStatus status, User user,
                     Book book, LocalDate borrowingDate,
                     LocalDate expectedReturnDate, LocalDate actualReturnDate, boolean isDamaged ) {
        this.borId = borId;
        this.status = status;
        this.user = user;
        this.book = book;
        this.borrowingDate = borrowingDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.isDamaged = isDamaged;
    }


    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    public int getBorId() {
        return borId;
    }

    public void setBorId(int borId) {
        this.borId = borId;
    }

    public BorrowingStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowingStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(LocalDate borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
}
