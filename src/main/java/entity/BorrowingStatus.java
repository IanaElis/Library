package entity;

import javax.persistence.*;

@Entity
@Table(name = "Borrowing_Status")
public class BorrowingStatus {
    @Id
    @Column(name = "status_id")
    private int borStatId;
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    public BorrowingStatus() {}

    public BorrowingStatus(int regStatId, String status) {
        this.status = status;
        this.borStatId = regStatId;
    }

    public int getBorStatId() {
        return borStatId;
    }

    public void setBorStatId(int borStatId) {
        this.borStatId = borStatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
