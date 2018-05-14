package com.meaf.core.entities;

import com.meaf.core.dao.service.base.IProjectElement;
import com.meaf.core.meta.EAnswerStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ANSWERS")
public class Answer implements Serializable, IProjectElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Lob
    @Column(name = "TEXT")
    private String text;

    @Column(name = "COMMENT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", columnDefinition = "varchar(32) default 'NEW'")
    private EAnswerStatus status = EAnswerStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate = new Date();

    @Column(name = "LAST_ACTION")
    private Date lastAction;

    @Transient
    private EAnswerStatus relativeStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EAnswerStatus getStatus() {
        return status;
    }

    public void setStatus(EAnswerStatus status) {
        this.status = status;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer(String text, EAnswerStatus status, Question question, User user) {
        this.text = text;
        this.status = status;
        this.question = question;
        this.user = user;
    }

    public Answer() {
    }

    @Override
    public Project getRootProject() {
        return getQuestion().getRootProject();
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

    public EAnswerStatus getRelativeStatus() {
        return relativeStatus;
    }

    public void setRelativeStatus(EAnswerStatus relativeStatus) {
        this.relativeStatus = relativeStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastUpdate = new Date();
    }

}
