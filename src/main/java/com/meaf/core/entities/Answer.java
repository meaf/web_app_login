package com.meaf.core.entities;

import com.meaf.core.meta.EAnswerStatus;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "QUESTIONS")
public class Answer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "STATUS")
    private EAnswerStatus status;

    @ManyToOne
    @JoinColumn(name = "PROJECT_STAGE_ID")
    private Survey survey;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EAnswerStatus getStatus() {
        return status;
    }

    public void setStatus(EAnswerStatus status) {
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

    public Answer(String text, EAnswerStatus status, Survey survey) {
        this.text = text;
        this.status = status;
        this.survey = survey;
    }

    public Answer() {
    }
}
