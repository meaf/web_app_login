package com.meaf.jsfBeans;

import com.meaf.core.dao.service.AnswersAccountant;
import com.meaf.core.dao.service.base.Response;
import com.meaf.core.dao.service.project.*;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EAnswerStatus;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@ManagedBean
@ViewScoped
public class ManagingBean implements Serializable {

    @Inject
    private ProjectService projectService;
    @Inject
    private ProjectStageService projectStageService;
    @Inject
    private SurveyService surveyService;
    @Inject
    private QuestionService questionService;
    @Inject
    private AnswerService answerService;
    @Inject
    private AnswersAccountant answersAccountant;

    private Answer managedAnswer;
    private Question managedQuestion;
    private Survey managedSurvey;
    private ProjectStage managedProjectStage;
    private Project managedProject;
    private Long removableItemId;

    public void loadAnswer(Question question) {
        answerService.getBranched(question.getId());
    }

    public void answerQuestionUser(Question question) {
        List<Answer> answers = answerService.getBranched(question.getId());
        if (answers == null || answers.isEmpty())
            managedAnswer = new Answer("", EAnswerStatus.NEW, question, null);
        else managedAnswer = answers.get(0);
    }

    public void addAnswer(boolean isCompleted) {
        Response response = new Response();
        if (managedAnswer.getText().isEmpty()) {
            managedAnswer.setStatus(EAnswerStatus.NEW);
        } else if (!isCompleted) {
            managedAnswer.setStatus(EAnswerStatus.DRAFT);
        } else {
            managedAnswer.setStatus(EAnswerStatus.SUBMITTED);
        }
        managedAnswer.setQuestion(managedQuestion);
        try {
            answerService.add(managedAnswer);
            response.setSeverity(FacesMessage.SEVERITY_INFO);
            response.setTitle("Success");
            response.setInfo("Answer saved as " + managedAnswer.getStatus().name().toLowerCase());
        } catch (Exception ex) {
            response.setSeverity(FacesMessage.SEVERITY_WARN);
            response.setTitle("Error");
            response.setInfo("failed to save answer; try again");
        }

        addToast(response);
//        managedAnswer = new Answer();
    }

    public void addQuestion() throws Exception {

        Response response;
        Project currentProject = projectService.getCurrentProject();
        if (managedQuestion.getTitle().trim().isEmpty() || managedQuestion.getDescription().trim().isEmpty()) {
            response = new Response(FacesMessage.SEVERITY_ERROR, "Error", "Fill all required fields");
            addToast(response);
            return;
        }
        managedQuestion.setSurvey(managedSurvey);
        questionService.add(managedQuestion);

        managedQuestion = new Question();

        response = new Response(FacesMessage.SEVERITY_INFO, "Success", "question created");
        addToast(response);
    }

    public void addSurvey() throws Exception {
        Response response;
        if (managedSurvey.getTopic().trim().isEmpty() || managedSurvey.getDescription().trim().isEmpty()) {
            response = new Response(FacesMessage.SEVERITY_ERROR, "Error", "Fill all required fields");
            addToast(response);
            return;
        }
        managedSurvey.setStage(managedProjectStage);
        surveyService.add(managedSurvey);

        managedSurvey = new Survey();
        response = new Response(FacesMessage.SEVERITY_INFO, "Success", "survey created");
        addToast(response);
    }

    public void addProjectStage() throws Exception {
        Response response;
        Project currentProject = projectService.getCurrentProject();
        if (managedProjectStage.getName() == null || managedProjectStage.getName().trim().isEmpty()) {
            response = new Response(FacesMessage.SEVERITY_ERROR, "Error", "Enter next stage's name");
            addToast(response);
            return;
        }

        projectStageService
                .getBranched(currentProject.getId())
                .stream()
                .max(Comparator.comparing(ProjectStage::getNumber))
                .ifPresent(ps -> {
                    managedProjectStage.setPreviousStage(ps);
                    managedProjectStage.setNumber(ps.getNumber() + 1);
                });
        managedProjectStage.setProject(currentProject);
        projectStageService.add(managedProjectStage);
        managedProjectStage = new ProjectStage();

        response = new Response(FacesMessage.SEVERITY_INFO, "Success", "stage created");
        addToast(response);
    }

    public void removeSurvey() {
        Response response = new Response();
        try {
            surveyService.delete(removableItemId);
            response.setSeverity(FacesMessage.SEVERITY_INFO);
            response.setTitle("Success");
            response.setInfo("survey deleted");
        } catch (Exception e) {
            response.setSeverity(FacesMessage.SEVERITY_ERROR);
            response.setTitle("Failed");
            response.setInfo("failed to delete survey...");
            e.printStackTrace();
        }
    }

    public void removeStage() {
        Response response = new Response();
        try {
            projectStageService.delete(removableItemId);
            response.setSeverity(FacesMessage.SEVERITY_INFO);
            response.setTitle("Success");
            response.setInfo("stage deleted");
        } catch (Exception e) {
            response.setSeverity(FacesMessage.SEVERITY_ERROR);
            response.setTitle("Failed");
            response.setInfo("failed to delete stage...");
            e.printStackTrace();
        }
    }

    public void removeQuestion() {
        Response response = new Response();
        try {
            questionService.delete(removableItemId);
            response.setSeverity(FacesMessage.SEVERITY_INFO);
            response.setTitle("Success");
            response.setInfo("question deleted");
        } catch (Exception e) {
            response.setSeverity(FacesMessage.SEVERITY_ERROR);
            response.setTitle("Failed");
            response.setInfo("failed to delete question...");
            e.printStackTrace();
        }
    }

    public boolean canUserDeleteAnyStage(Project project) {
        List<ProjectStage> projectStages = projectStageService.getBranched(project.getId());
        return projectStages.stream().anyMatch(s -> {
            List<Answer> answers = answersAccountant.getStagesRelatedAnswers(s, false);
            return answers.isEmpty()
                    || answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.NEW));
        });
    }

    public boolean canUserDeleteAnySurvey(ProjectStage projectStage) {
        List<Survey> surveys = surveyService.getBranched(projectStage.getId());
        return surveys.stream().anyMatch(s -> {
            List<Answer> answers = answersAccountant.getSurveyRelatedAnswers(s, false);
            return answers.isEmpty()
                    || answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.NEW));
        });
    }

    public boolean canUserDeleteAnyQuestion(Survey survey) {
        List<Question> questions = questionService.getBranched(survey.getId());
        return questions.stream().anyMatch(s -> {
            List<Answer> answers = answersAccountant.getQuestionRelatedAnswers(s, false);
            return answers.isEmpty()
                    || answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.NEW));
        });
    }

    private void addToast(Response response) {
        if (response != null)
            FacesContext.getCurrentInstance().addMessage(null, response.generateToast());
    }

    /**
     * Get/Set section
     */
    public Answer getManagedAnswer() {
        if (managedAnswer == null)
            managedAnswer = new Answer();
        return managedAnswer;
    }

    public boolean getManagedAnswerEditable() {
        return managedAnswer == null
                || managedAnswer.getStatus().equals(EAnswerStatus.NEW)
                || managedAnswer.getStatus().equals(EAnswerStatus.DRAFT);
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

    public void setRemovableItemId(Long removableItemId) {
        this.removableItemId = removableItemId;
    }

    public Long getRemovableItemId() {
        return removableItemId;
    }
}