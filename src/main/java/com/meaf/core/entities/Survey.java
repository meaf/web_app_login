package com.meaf.core.entities;

import com.meaf.core.meta.ESurveyStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SURVEYS")
public class Survey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "TOPIC")
    private String name;

    @Column(name = "STATUS", columnDefinition = "varchar(32) default 'NEW'")
    private ESurveyStatus status = ESurveyStatus.NEW;

    @OneToOne
    @JoinColumn(name = "PROJECT_STAGE")
    private ProjectStage stage;

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

    public Survey(String name, ESurveyStatus status, ProjectStage stage) {
        this.name = name;
        this.status = status;
        this.stage = stage;
    }

    public Survey() {
    }
}
