package com.meaf.controllers;

import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@ManagedBean
@RequestScoped
public class ManagingBean implements Serializable {

    private HttpServletRequest httpRequest;
    private ProjectService projectService;
    private ProjectStageService projectStageService;
    private SurveyService surveyService;
    private QuestionService questionService;
    private AnswerService answerService;

    @Inject
    public ManagingBean(HttpServletRequest httpRequest, AnswerService answerService, ProjectService projectService, ProjectStageService projectStageService, QuestionService questionService, SurveyService surveyService) {
        this.httpRequest = httpRequest;
        this.answerService = answerService;
        this.projectService = projectService;
        this.projectStageService = projectStageService;
        this.questionService = questionService;
        this.surveyService = surveyService;
    }


    private Answer managedAnswer;
    private Question managedQuestion;
    private Survey managedSurvey;
    private ProjectStage managedProjectStage;
    private Project managedProject;

    public void addAnswer(Question question) {
        managedAnswer.setQuestion(question);
        answerService.add(managedAnswer);
    }

    /**
     * Get/Set section
     */
    public Answer getManagedAnswer() {
        return managedAnswer;
    }

    public void setManagedAnswer(Answer managedAnswer) {
        this.managedAnswer = managedAnswer;
    }

    public Question getManagedQuestion() {
        return managedQuestion;
    }

    public void setManagedQuestion(Question managedQuestion) {
        this.managedQuestion = managedQuestion;
    }

    public Survey getManagedSurvey() {
        return managedSurvey;
    }

    public void setManagedSurvey(Survey managedSurvey) {
        this.managedSurvey = managedSurvey;
    }

    public ProjectStage getManagedProjectStage() {
        return managedProjectStage;
    }

    public void setManagedProjectStage(ProjectStage managedProjectStage) {
        this.managedProjectStage = managedProjectStage;
    }

    public Project getManagedProject() {
        return managedProject;
    }

    public void setManagedProject(Project managedProject) {
        this.managedProject = managedProject;
    }
}