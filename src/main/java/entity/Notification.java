package entity;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Notification")
public class Notification  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ntfc_seq_gen")
    @SequenceGenerator(name = "ntfc_seq_gen", sequenceName = "ntfc_seq", allocationSize = 1)
    @Column(name = "id")
    private int notifID;
    @Column(name = "message")
    private String message;
    @Column(name = "ntfc_date")
    private Date creation_date;

    public Notification() {}

    public Notification(String message,
                        Date creation_date, boolean isRead) {
        this.message = message;
        this.creation_date = creation_date;
    }

    public Notification(int notifID, String message,
                        Date creation_date, boolean isRead) {
        this.notifID = notifID;
        this.message = message;
        this.creation_date = creation_date;
    }

    public int getNotifID() {
        return notifID;
    }

    public void setNotifID(int notifID) {
        this.notifID = notifID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

}
