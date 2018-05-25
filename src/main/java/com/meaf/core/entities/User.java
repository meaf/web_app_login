package com.meaf.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "REGISTER_DATE")
    private Date regDate = new Date();

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @OneToOne(mappedBy = "user")
    private UserProfile userProfile;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).getId().equals(id);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserFullName() {
        return userProfile.getLastName() + " " +
                (userProfile.getFirstName() == null
                        ? ""
                        : userProfile.getFirstName().substring(0, 1).toUpperCase() + ". ") +
                (userProfile.getPatronymic() == null
                        ? ""
                        : userProfile.getPatronymic().substring(0, 1).toUpperCase() + ". ");
    }
}
