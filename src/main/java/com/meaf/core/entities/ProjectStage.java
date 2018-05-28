package com.meaf.core.entities;

import com.meaf.core.dao.service.base.IProjectElement;
import com.meaf.core.meta.ESurveyStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

    @OneToMany(mappedBy = "stage", cascade = CascadeType.REMOVE)
    private Set<Survey> surveys;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "LAST_ACTION")
    private Date lastAction;

    @Transient
    private ESurveyStatus surveyStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }

    public ESurveyStatus getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(ESurveyStatus surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    @Override
    public Project getRootProject() {
        return getProject().getRootProject();
    }

    @Override
    public void updateActionTime() {

    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastUpdate = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ProjectStage && ((ProjectStage) obj).getId().equals(id);
    }
}
