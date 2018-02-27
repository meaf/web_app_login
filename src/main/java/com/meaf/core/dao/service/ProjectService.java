package com.meaf.core.dao.service;

import com.meaf.core.entities.*;
import com.meaf.core.meta.ESurveyStatus;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named
@SessionScoped
public class ProjectService extends ABaseService{

    public void addProject(String projectName) {
        EntityManager em = getEntityManager();
        Project project = new Project(projectName);
        em.persist(project);
    }

    public void addStage(ProjectStage currentStage, String stageName, Project project) {
        EntityManager em = getEntityManager();
        ProjectStage projectStage = new ProjectStage(
                stageName,
                currentStage,
                (currentStage == null ? 1L : currentStage.getNumber() + 1),
                project);
        em.persist(projectStage);
    }

    public void addSurvey(ProjectStage projectStage, String topic){
        EntityManager em = getEntityManager();
        Survey survey = new Survey(topic, ESurveyStatus.NEW, projectStage);
        em.persist(survey);
    }

    public void addQuestion(ProjectStage projectStage, String questionText, Survey survey){
        EntityManager em = getEntityManager();
        Question question = new Question(questionText, ESurveyStatus.NEW, survey);
        em.persist(question);
    }

}
