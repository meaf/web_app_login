package com.meaf.core.entities;

import com.meaf.core.meta.ESurveyStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "QUESTIONS")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "STATUS", columnDefinition = "varchar(32) default 'NEW'")
    private ESurveyStatus status = ESurveyStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Question(String text, ESurveyStatus status, Survey survey) {
        this.text = text;
        this.status = status;
        this.survey = survey;
    }

    public Question() {
    }
}
