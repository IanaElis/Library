package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrow_seq_gen")
    @SequenceGenerator(name = "borrow_seq_gen", sequenceName = "borrow_seq", allocationSize = 1)
    @Column(name = "book_id")
    private int id;
    @Column(name = "isbn", length = 30)
    private String isbn;
    @Column(name = "title", length = 100)
    private String title;
    @Column(name = "author", length = 100)
    private String author;
    @Column(name = "genre", length = 100)
    private String genre;
    @Column(name = "year_published")
    private LocalDate year;

    @ManyToOne
    @JoinColumn(name = "Book_Status_status_id",
            referencedColumnName = "status_id")
    private BookStatus status;

    @Column(name = "total_quantity")
    private int totalQuantity;
    @Column(name = "available_quantity")
    private int availableQuantity;
    @Column(name = "publisher", length = 100)
    private String publisher;

    public Book() {}

    public Book(String isbn, String title, String author, String genre,
                LocalDate year, BookStatus status, int totalQuantity,
                int availableQuantity, String publisher){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.status = status;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.publisher = publisher;
    }

    public Book(int id, String isbn, String title, String author, String genre,
                LocalDate year, BookStatus status, int totalQuantity,
                int availableQuantity, String publisher) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.status = status;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
