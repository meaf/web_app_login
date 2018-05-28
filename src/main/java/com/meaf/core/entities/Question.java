package com.meaf.core.entities;

import com.meaf.core.dao.service.base.IProjectElement;
import com.meaf.core.meta.ESurveyStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "QUESTIONS")
public class Question implements Serializable, IProjectElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", columnDefinition = "varchar(32) default 'NEW'")
    private ESurveyStatus status = ESurveyStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private Set<Answer> answers;

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

    public ESurveyStatus getStatus() {
        return status;
    }

    public void setStatus(ESurveyStatus status) {
        this.status = status;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String text) {
        title = text;
    }

    public Question(String title, ESurveyStatus status, Survey survey) {
        this.title = title;
        this.status = status;
        this.survey = survey;
    }

    public Question() {
    }

    @Override
    public Project getRootProject() {
        return getSurvey().getRootProject();
    }

    @Override
    public void updateActionTime() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public ESurveyStatus getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(ESurveyStatus surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastUpdate = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Question && ((Question) obj).getId().equals(id);
    }
}
