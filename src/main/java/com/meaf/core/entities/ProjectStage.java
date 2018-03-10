package com.meaf.core.entities;

import com.meaf.core.dao.service.base.IProjectElement;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROJECT_STAGES")
public class ProjectStage implements Serializable, IProjectElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STAGE_NAME")
    private String name;

    @OneToOne
    @JoinColumn(name = "PREVIOUS_STAGE")
    private ProjectStage previousStage;

    @Column(name = "STAGE_NUMBER")
    private Long number;

    @ManyToOne
    @JoinColumn(name = "PROJECT")
    private Project project;

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

    public ProjectStage getPreviousStage() {
        return previousStage;
    }

    public void setPreviousStage(ProjectStage previousStage) {
        this.previousStage = previousStage;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectStage(String name, ProjectStage previousStage, long number, Project project) {
        this.name = name;
        this.previousStage = previousStage;
        this.number = number;
        this.project = project;
    }

    public ProjectStage() {
    }

    @Override
    public Project getRootProject() {
        return getProject().getRootProject();
    }
}
