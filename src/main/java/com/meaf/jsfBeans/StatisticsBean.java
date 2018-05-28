package com.meaf.jsfBeans;

import com.meaf.core.dao.service.AnswersAccountant;
import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EAnswerStatus;
import com.meaf.core.meta.EProjectRole;
import com.meaf.core.meta.ESurveyStatus;
import org.primefaces.model.chart.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@SessionScoped
public class StatisticsBean implements Serializable {

    @Inject
    AnswersAccountant answersAccountant;
    @Inject
    SessionManagementHelper sessionManagementHelper;

    private Question handledQuestion;
    private Survey handledSurvey;
    private ProjectStage handledProjectStage;
    private Project handledProject;


    public PieChartModel drawPieChart() {
        Project project = getHandledProject();
        List<Answer> answers = answersAccountant.getProjectRelatedAnswers(project);
        List<Question> questions = answersAccountant.getProjectRelatedQuestions(project);
        List<User> users = answersAccountant.getProjectExperts(project);

        return constructPie(answers, questions, users);
    }

    private PieChartModel constructPie(List<Answer> answers, List<Question> questions, List<User> users) {

        PieChartModel pieModel = new PieChartModel();

        Integer pendingAnswers = Math.multiplyExact(users.size(), questions.size());
        Long submitted = answers.stream().filter(a -> a.getStatus() == EAnswerStatus.SUBMITTED).count();
        Long drafted = answers.stream().filter(a -> a.getStatus() == EAnswerStatus.DRAFT).count();
        Long remaining = Math.subtractExact(pendingAnswers.longValue(), (Math.addExact(submitted, drafted)));

        pieModel.set(EAnswerStatus.SUBMITTED.getLocal(), submitted);
        pieModel.set(EAnswerStatus.DRAFT.getLocal(), drafted);
        pieModel.set(EAnswerStatus.NEW.getLocal(), remaining);
        pieModel.setTitle("Відповіді");
        pieModel.setLegendPosition("ne");
        pieModel.setLegendPlacement(LegendPlacement.OUTSIDE);

        return pieModel;
    }


    public BarChartModel drawBarChart() {
        Project project = getHandledProject();
        List<Answer> answers = answersAccountant.getProjectRelatedAnswers(project);
        List<Question> questions = answersAccountant.getProjectRelatedQuestions(project);
        List<User> users = answersAccountant.getProjectExperts(project);

        return constructBar(answers, questions, users);
    }

    private BarChartModel constructBar(List<Answer> answers, List<Question> questions, List<User> users) {

        BarChartModel barChartModel = new HorizontalBarChartModel();

        Integer pendingAnswers = Math.multiplyExact(users.size(), questions.size());
        Long submitted = answers.stream().filter(a -> a.getStatus() == EAnswerStatus.SUBMITTED).count();
        Long drafted = answers.stream().filter(a -> a.getStatus() == EAnswerStatus.DRAFT).count();

        Long remaining = Math.subtractExact(pendingAnswers.longValue(), (Math.addExact(submitted, drafted)));

        ChartSeries draftChartSeries = constructUserChartSeries(answers, questions, users, EAnswerStatus.DRAFT);
        barChartModel.addSeries(draftChartSeries);
        ChartSeries submittedChartSeries = constructUserChartSeries(answers, questions, users, EAnswerStatus.SUBMITTED);
        barChartModel.addSeries(submittedChartSeries);

//        Axis yAxis = barChartModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Y axis");

        Axis xAxis = barChartModel.getAxis(AxisType.X);
        xAxis.setLabel("");
        xAxis.setMin(0);
        xAxis.setMax(questions.size());
        xAxis.setTickInterval("2");

        barChartModel.setStacked(true);
//        barChartModel.setAnimate(true);
        barChartModel.setLegendPosition("ne");
        barChartModel.setLegendPlacement(LegendPlacement.OUTSIDE);

        return barChartModel;
    }

    private ChartSeries constructUserChartSeries(List<Answer> answers, List<Question> questions, List<User> users, EAnswerStatus status) {
        Map<String, List<Answer>> chartMap = answers
                .stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.groupingBy(a -> a.getUser().getUserFullName()));

        ChartSeries chartSeries = new ChartSeries();
        chartSeries.setLabel(status.getLocal());
        users.stream()
                .map(User::getUserFullName)
                .forEach(u -> {
                    if (!chartMap.keySet().contains(u))
                        chartMap.put(u, new LinkedList<>());

                });

        chartMap.forEach((k, v) -> chartSeries.set(k, v.size()));
        return chartSeries;

    }

    public boolean isUserInProjectRole(EProjectRole role) {
        ProjectUserConnection connection = sessionManagementHelper.getCurrentSessionProjectUserConnection();
        return connection != null
                && role.equals(connection.getRole());
    }


    public String calculateStatus(Question question) {
        ESurveyStatus status = calculate(
                answersAccountant.getQuestionRelatedAnswers(question, isUserInProjectRole(EProjectRole.EXPERT)),
                1); // only one answer to question
        return status.getLocal();
    }

    public String calculateStatus(Survey survey) {
        ESurveyStatus status = calculate(
                answersAccountant.getSurveyRelatedAnswers(survey, isUserInProjectRole(EProjectRole.EXPERT)),
                answersAccountant.getSurveyRelatedQuestions(survey).size());
        return status.getLocal();
    }

    public String calculateStatus(ProjectStage projectStage) {
        ESurveyStatus status = calculate(
                answersAccountant.getStagesRelatedAnswers(projectStage, isUserInProjectRole(EProjectRole.EXPERT)),
                answersAccountant.getStagesRelatedQuestions(projectStage).size());
        return status.getLocal();
    }

    public String calculateStatus(Project project) {
        ESurveyStatus status = calculate(
                answersAccountant.getProjectRelatedAnswers(project),
                answersAccountant.getProjectRelatedQuestions(project).size());
        return status.getLocal();
    }

    public ESurveyStatus calculate(List<Answer> answers, int expectedAnswersAmount) {
        if (expectedAnswersAmount == answers.size() && answers.size() != 0) {
            if (answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.SUBMITTED)))
                return ESurveyStatus.ANSWERED;
        }
        if (answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.NEW)))
            return ESurveyStatus.NEW;
        if (answers.stream().anyMatch(q -> q.getStatus().equals(EAnswerStatus.SUBMITTED)))
            return ESurveyStatus.PARTIALY_ANSWERED;
        if (answers.stream().anyMatch(q -> q.getStatus().equals(EAnswerStatus.DRAFT)))
            return ESurveyStatus.STARTED;

        throw new IllegalArgumentException("Cannot resolve for statuses [" +
                answers.stream()
                        .map(a -> a.getStatus().name())
                        .collect(Collectors.joining(", ")) +
                "]");
    }

    public Question getHandledQuestion() {
        return handledQuestion;
    }

    public void setHandledQuestion(Question handledQuestion) {
        this.handledQuestion = handledQuestion;
    }

    public Survey getHandledSurvey() {
        return handledSurvey;
    }

    public void setHandledSurvey(Survey handledSurvey) {
        this.handledSurvey = handledSurvey;
    }

    public ProjectStage getHandledProjectStage() {
        return handledProjectStage;
    }

    public void setHandledProjectStage(ProjectStage handledProjectStage) {
        this.handledProjectStage = handledProjectStage;
    }

    public Project getHandledProject() {
        return handledProject;
    }

    public void setHandledProject(Project handledProject) {
        this.handledProject = handledProject;
    }
}
