package entity;

import javax.persistence.*;

@Entity
@Table(name = "Book_Status")
public class BookStatus {
    @Id
    @Column(name = "status_id")
    private int id;
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    public BookStatus() {}

    public BookStatus(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
