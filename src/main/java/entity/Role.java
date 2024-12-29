package entity;

import javax.persistence.*;

@Entity
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "role_id")
    private int roleID;
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    @Column(name = "description", length = 100)
    private String description;

    public Role() {}

    public Role(int roleID, String name, String description) {
        this.roleID = roleID;
        this.name = name;
        this.description = description;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleID=" + roleID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
