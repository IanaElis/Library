package entity;

import javax.persistence.*;

@Entity
@Table(name = "Register_Status")
public class RegisterStatus {
    @Id
    @Column(name = "id")
    private int regStatId;
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    public RegisterStatus() {}

    public RegisterStatus(String status, int regStatId) {
        this.status = status;
        this.regStatId = regStatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRegStatId() {
        return regStatId;
    }

    public void setRegStatId(int regStatId) {
        this.regStatId = regStatId;
    }
}
