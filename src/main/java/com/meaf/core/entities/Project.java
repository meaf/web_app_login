package com.meaf.core.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROJECTS")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "PROJECT_NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Project(String name) {
        this.name = name;
    }

    public Project() {
    }
}
