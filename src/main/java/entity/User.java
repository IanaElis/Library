package entity;

import dao.UserNotificationDAO;
import observer.Observer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "\"User\"")
public class User implements Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;
    @Column(name = "password", nullable = false, length = 25)
    private String password;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "phone_number")
    private int phoneNumber;
    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @OneToOne
    @JoinColumn(name = "Register_Form_reg_form_id",
            referencedColumnName = "reg_form_id")
    private RegisterForm registerForm;

    @ManyToOne
    @JoinColumn(name = "Role_role_id",
            referencedColumnName = "role_id")
    private Role role;


    public User() {}

    public User( String email, String password, String name,
                int phoneNumber, LocalDate approvalDate,
                RegisterForm registerForm, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.approvalDate = approvalDate;
        this.registerForm = registerForm;
        this.role = role;
    }

    public User(int userId, String email, String password, String name,
                 int phoneNumber, LocalDate approvalDate,
                 RegisterForm registerForm, Role role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.approvalDate = approvalDate;
        this.registerForm = registerForm;
        this.role = role;
    }


    @Override
    public void update(Notification notification, String email) {
        UserNotificationDAO un = new UserNotificationDAO();
        UserNotification userNotification = new UserNotification(this, notification, false, LocalDate.now());
        if(email != null) {
            userNotification.setAdditionalInfo(email);
        }
        un.saveOrUpdate(userNotification);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public RegisterForm getRegisterForm() {
        return registerForm;
    }

    public void setRegisterForm(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", approvalDate=" + approvalDate +
                ", registerForm=" + registerForm +
                ", role=" + role +
                '}';
    }
}
