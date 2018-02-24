package com.meaf.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ROLES")
public class Role implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "ROLENAME")
    private String rolename;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}