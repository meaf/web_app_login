package com.meaf.jsfBeans;

import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EAnswerStatus;

import javax.annotation.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Named
@ManagedBean
@ViewScoped
public class ManagingBean implements Serializable {

    private ProjectService projectService;
    private ProjectStageService projectStageService;
    private SurveyService surveyService;
    private QuestionService questionService;
    private AnswerService answerService;

    @Inject
    public ManagingBean(AnswerService answerService, ProjectService projectService, ProjectStageService projectStageService, QuestionService questionService, SurveyService surveyService) {
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

    public void addAnswer(boolean isCompleted) throws Exception {
        if (managedAnswer.getText().isEmpty()) {
            managedAnswer.setStatus(EAnswerStatus.NEW);
        } else if (!isCompleted) {
            managedAnswer.setStatus(EAnswerStatus.DRAFT);
        } else {
            managedAnswer.setStatus(EAnswerStatus.SUBMITTED);
        }
        managedAnswer.setQuestion(managedQuestion);
        answerService.add(managedAnswer);

//        managedAnswer = new Answer();
    }

    public void addQuestion() throws Exception {
        managedQuestion.setSurvey(managedSurvey);
        questionService.add(managedQuestion);

        managedQuestion = new Question();
    }

    public void addSurvey() throws Exception {
        managedSurvey.setStage(managedProjectStage);
        surveyService.add(managedSurvey);

        managedSurvey = new Survey();
    }

    public void addProjectStage() throws Exception {

        Project currentProject = projectService.getCurrentProject();

        projectStageService
                .getBranch(currentProject.getId())
                .stream()
                .max(Comparator.comparing(ProjectStage::getNumber))
                .ifPresent(ps -> {
                    managedProjectStage.setPreviousStage(ps);
                    managedProjectStage.setNumber(ps.getNumber() + 1);
                });
        managedProjectStage.setProject(currentProject);

        projectStageService.add(managedProjectStage);

        managedProjectStage = new ProjectStage();
    }

    /**
     * Get/Set section
     */
    public Answer getManagedAnswer() {
        if (managedAnswer == null)
            managedAnswer = new Answer();
        return managedAnswer;
    }

    public void setManagedAnswer(Answer managedAnswer) {
        this.managedAnswer = managedAnswer;
    }

    public Question getManagedQuestion() {
        if (managedQuestion == null)
            managedQuestion = new Question();
        return managedQuestion;
    }

    public void setManagedQuestion(Question managedQuestion) {
        this.managedQuestion = managedQuestion;
    }

    public Survey getManagedSurvey() {
        if (managedSurvey == null)
            managedSurvey = new Survey();
        return managedSurvey;
    }

    public void setManagedSurvey(Survey managedSurvey) {
        this.managedSurvey = managedSurvey;
    }

    public ProjectStage getManagedProjectStage() {
        if (managedProjectStage == null)
            managedProjectStage = new ProjectStage();
        return managedProjectStage;
    }

    public void setManagedProjectStage(ProjectStage managedProjectStage) {
        this.managedProjectStage = managedProjectStage;
    }

    public Project getManagedProject() {
        if (managedProject == null)
            managedProject = new Project();
        return managedProject;
    }

    public void setManagedProject(Project managedProject) {
        this.managedProject = managedProject;
    }
}