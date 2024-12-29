package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Register_Form")
public class RegisterForm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reg_form_seq_gen")
    @SequenceGenerator(name = "reg_form_seq_gen", sequenceName = "reg_form_seq", allocationSize = 1)
    @Column(name = "reg_form_id")
    private int regFormId;
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name = "password", nullable = false, length = 25)
    private String password;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "phone_number")
    private int phoneNumber;

    @ManyToOne
    @JoinColumn(name = "Register_Status_id", referencedColumnName = "id")
    private RegisterStatus status;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    public RegisterForm() {}

    public RegisterForm(String email, String password,
                        String name, int phoneNumber,
                        RegisterStatus status, LocalDate dateCreated) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.dateCreated = dateCreated;
    }


    public RegisterForm(int regFormId, String email, String password,
                        String name, int phoneNumber,
                        RegisterStatus status, LocalDate  dateCreated) {
        this.regFormId = regFormId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.dateCreated = dateCreated;
    }

    public int getRegFormId() {
        return regFormId;
    }

    public void setRegFormId(int regFormId) {
        this.regFormId = regFormId;
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

    public RegisterStatus getStatus() {
        return status;
    }

    public void setStatus(RegisterStatus status) {
        this.status = status;
    }

    public LocalDate  getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate  dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "RegisterForm{" +
                "regFormId=" + regFormId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", status=" + status +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
