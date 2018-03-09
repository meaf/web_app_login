package com.meaf.controllers;

import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@RequestScoped
public class NavigationBean implements Serializable {

    @Inject
    private HttpServletRequest httpRequest;

    @Inject
    private AnswerService answerService;
    @Inject
    private ProjectService projectService;
    @Inject
    private ProjectStageService projectStageService;
    @Inject
    private QuestionService questionService;
    @Inject
    private SurveyService surveyService;

    private Long id;
    private Project currentProject;

    public List<Project> getProjects() {
        return projectService.getAll();
    }

    public List<ProjectStage> getProjectSteps() {
        httpRequest.getRemoteUser();
        Question question;
        return projectStageService.getAll();
    }


    public List<Survey> getSurveys() {
        return surveyService.getAll();
    }

    public List<Question> getQuestions() {
        return questionService.getAll();
    }

    public Question getQuestion(Long id) {
        return questionService.getById(id);
    }

    public List<Answer> getAnswers() {
        return answerService.getAll();
    }

    public List<Survey> getBranchedSurveys() throws IllegalAccessException {
        return surveyService.getBranch(id);
    }

    public List<Question> getBranchedQuestions() throws IllegalAccessException {
        return questionService.getBranch(id);
    }

    public List<Answer> getBranchedAnswers() {
        return answerService.getBranch(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get/Set section
     */
//    public Answer getCurrentAnswer() {
//        return currentAnswer;
//    }
//
//    public void setCurrentAnswer(Answer currentAnswer) {
//        this.currentAnswer = currentAnswer;
//    }
    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }
//
//    public ProjectStage getCurrentProjectStage() {
//        return currentProjectStage;
//    }
//
//    public void setCurrentProjectStage(ProjectStage currentProjectStage) {
//        this.currentProjectStage = currentProjectStage;
//    }
//
//    public Question getCurrentQuestion() {
//        return currentQuestion;
//    }
//
//    public void setCurrentQuestion(Question currentQuestion) {
//        this.currentQuestion = currentQuestion;
//    }
//
//    public Survey getCurrentSurvey() {
//        return currentSurvey;
//    }
//
//    public void setCurrentSurvey(Survey currentSurvey) {
//        this.currentSurvey = currentSurvey;
//    }
}