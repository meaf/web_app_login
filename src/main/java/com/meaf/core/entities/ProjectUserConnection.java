package com.meaf.core.entities;

import com.meaf.core.meta.EProjectRole;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PROJECT_USER_CONNECTIONS")
public class ProjectUserConnection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private EProjectRole role;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "LAST_ACTION")
    private Date lastAction;

    @Column(name = "INVITE")
    private String invite;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public EProjectRole getRole() {
        return role;
    }

    public void setRole(EProjectRole role) {
        this.role = role;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    public ProjectUserConnection(User user, Project project, EProjectRole role) {
        this.user = user;
        this.project = project;
        this.role = role;
    }

    public ProjectUserConnection() {
    }
}