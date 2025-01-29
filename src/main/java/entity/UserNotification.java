package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "User_Notification")
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_ntfc_seq_gen")
    @SequenceGenerator(name = "user_ntfc_seq_gen", sequenceName = "user_ntfc_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "User_user_id",
            referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Notification_id",
            referencedColumnName = "id")
    private Notification notification;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name="additional_info")
    private String additionalInfo;

    @Column(name = "date_sent")
    private LocalDate dateSent;

    public UserNotification() {}

    public UserNotification(User user, Notification notification,
                            boolean isRead, LocalDate dateSent) {
        this.user = user;
        this.notification = notification;
        this.isRead = isRead;
        this.dateSent = dateSent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

}
