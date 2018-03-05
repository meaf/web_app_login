package com.meaf.controllers;

import com.meaf.core.dao.service.UserService;
import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {

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
    @Inject
    private UserService userService;

    private Answer currentAnswer;
    private Project currentProject;
    private ProjectStage currentProjectStage;
    private Question currentQuestion;
    private Survey currentSurvey;


    public List<ProjectStage> getProjectSteps() {
        return projectStageService.getAll();
    }

    public List<Survey> getSurveys() {
        return surveyService.getAll();
    }

    public List<Question> getQuestions() {
        return questionService.getAll();
    }

    public List<Answer> getAnswers() {
        return answerService.getAll();
    }


    /**
     * Get/Set section
     */
    public Answer getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(Answer currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public ProjectStage getCurrentProjectStage() {
        return currentProjectStage;
    }

    public void setCurrentProjectStage(ProjectStage currentProjectStage) {
        this.currentProjectStage = currentProjectStage;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Survey getCurrentSurvey() {
        return currentSurvey;
    }

    public void setCurrentSurvey(Survey currentSurvey) {
        this.currentSurvey = currentSurvey;
    }
}