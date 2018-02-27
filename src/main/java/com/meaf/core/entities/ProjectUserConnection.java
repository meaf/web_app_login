package com.meaf.core.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROJECT_USER_CONNECTIONS")
public class ProjectUserConnection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ProjectUserConnection(User user, Project project) {
        this.user = user;
        this.project = project;
        this.role = user.getRole();
    }

    public ProjectUserConnection() {
    }
}
