package com.meaf.jsfBeans;

import com.meaf.core.dao.service.ProjectUserConnectionService;
import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;

import javax.annotation.ManagedBean;
import javax.faces.bean.RequestScoped;
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
    @Inject
    private ProjectUserConnectionService projectUserConnectionService;

    public List<Project> getProjects() {
        return projectService.getAll();
    }

    public List<ProjectStage> getProjectStages() {
        httpRequest.getRemoteUser();
        return projectStageService.getAll();
    }

    /**
     * BY ID
     */
    public Project projectById(Long id) {
        return projectService.getById(id);
    }

    public ProjectStage projectStageById(Long id) {
        return projectStageService.getById(id);
    }

    public Survey surveyById(Long id) {
        return surveyService.getById(id);
    }

    public Question questionById(Long id) {
        return questionService.getById(id);
    }

    public Answer answerById(Long id) {
        return answerService.getById(id);
    }

    /**
     * BY USER
     */
    public List<ProjectStage> stagesAll() {
        return projectStageService.getAll();
    }

    public List<Survey> surveysAll() {
        return surveyService.getAll();
    }

    public List<Question> questionsAll() {
        return questionService.getAll();
    }

    public List<Answer> answersAll() {
        return answerService.getAll();
    }

    public List<Answer> answersAllByQuestion(Question question) {
        return answerService.getAllByQuestion(question);
    }


    /**
     * BY ROOT
     */
    public List<ProjectStage> stagesBranched(Long rootId) {
        return projectStageService.getBranched(rootId);
    }

    public List<Survey> surveysBranched(Long rootId) {
        return surveyService.getBranched(rootId);
    }

    public List<Question> questionsBranched(Long rootId) {
        return questionService.getBranched(rootId);
    }

    public List<Answer> answersBranched(Long rootId) {
        return answerService.getBranched(rootId);
    }

    public List<ProjectUserConnection> getValidInvites() {
        return projectUserConnectionService.getUnusedInvites();
    }

}