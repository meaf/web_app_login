package com.meaf.controllers;

import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EAnswerStatus;

import javax.annotation.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@ViewScoped
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

    public void loadAnswer(Question question) {
        answerService.getBranch(question.getId());
    }

    public void answerQuestionUser(Question question) {
        List<Answer> answers = answerService.getBranch(question.getId());
        if (answers == null || answers.isEmpty())
            managedAnswer = new Answer("", EAnswerStatus.NEW, question, null);
        else managedAnswer = answers.get(0);
    }

    public void addAnswer() throws Exception {
        managedAnswer.setQuestion(managedQuestion);
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