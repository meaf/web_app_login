package com.meaf.core.entities;

import com.meaf.core.dao.service.base.IProjectElement;
import com.meaf.core.meta.ESurveyStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "LAST_ACTION")
    private Date lastAction;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
