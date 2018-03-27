package com.meaf.core.entities;

import com.meaf.core.dao.service.base.IProjectElement;
import com.meaf.core.meta.ESurveyStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SURVEYS")
public class Survey implements Serializable, IProjectElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOPIC")
    private String topic;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", columnDefinition = "varchar(32) default 'NEW'")
    private ESurveyStatus status = ESurveyStatus.NEW;

    @OneToOne
    @JoinColumn(name = "PROJECT_STAGE")
    private ProjectStage stage;

    // todo: sub-surveys

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "LAST_ACTION")
    private Date lastAction;

    @Transient
    private ESurveyStatus relativeStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String name) {
        topic = name;
    }

    public ESurveyStatus getStatus() {
        return status;
    }

    public void setStatus(ESurveyStatus status) {
        this.status = status;
    }

    public ProjectStage getStage() {
        return stage;
    }

    public void setStage(ProjectStage stage) {
        this.stage = stage;
    }

    public Survey(String topic, ESurveyStatus status, ProjectStage stage) {
        this.topic = topic;
        this.status = status;
        this.stage = stage;
    }

    public Survey() {
    }

    @Override
    public Project getRootProject() {
        return getStage().getRootProject();
    }

    @Override
    public void updateActionTime() {
        getStage().updateActionTime();
    }

    public ESurveyStatus getRelativeStatus() {
        return relativeStatus;
    }

    public void setRelativeStatus(ESurveyStatus relativeStatus) {
        this.relativeStatus = relativeStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
