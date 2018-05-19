package com.meaf.jsfBeans;

import com.meaf.core.dao.service.AnswersAccountant;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EAnswerStatus;
import com.meaf.core.meta.ESurveyStatus;
import org.primefaces.model.chart.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class StatisticsBean implements Serializable {

    @Inject
    AnswersAccountant answersAccountant;

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

        pieModel.set("Submitted", submitted);
        pieModel.set("Drafted", drafted);
        pieModel.set("New", remaining);
        pieModel.setTitle("Answers");
        pieModel.setLegendPosition("e");

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

        barChartModel.setStacked(true);
        barChartModel.setAnimate(true);
        barChartModel.setLegendPosition("ne");
        barChartModel.setLegendPlacement(LegendPlacement.INSIDE);

        return barChartModel;
    }

    private ChartSeries constructUserChartSeries(List<Answer> answers, List<Question> questions, List<User> users, EAnswerStatus status) {


        Map<String, List<Answer>> chartMap = answers.stream().filter(a -> a.getStatus() == status).collect(Collectors.groupingBy(a -> a.getUser().getUsername()));

        ChartSeries chartSeries = new ChartSeries();
        chartSeries.setLabel(status.name());

        users.stream()
                .map(User::getUsername)
                .forEach(u -> {
                    if (!chartMap.keySet().contains(u))
                        chartMap.put(u, new LinkedList<>());

                });

        chartMap.forEach((k, v) -> chartSeries.set(k, v.size()));

        return chartSeries;

    }


    public String calculateStatus(Question question) {
        ESurveyStatus status = calculate(answersAccountant.getQuestionRelatedAnswers(question));
        return status.name();
    }

    public String calculateStatus(Survey survey) {
        ESurveyStatus status = calculate(answersAccountant.getSurveyRelatedAnswers(survey));
        return status.name();
    }

    public String calculateStatus(ProjectStage projectStage) {
        ESurveyStatus status = calculate(answersAccountant.getStagesRelatedAnswers(projectStage));
        return status.name();
    }

    public String calculateStatus(Project project) {
        ESurveyStatus status = calculate(answersAccountant.getProjectRelatedAnswers(project));
        return status.name();
    }

    public ESurveyStatus calculate(List<Answer> answers) {
        if (answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.NEW)))
            return ESurveyStatus.NEW;
        if (answers.stream().allMatch(q -> q.getStatus().equals(EAnswerStatus.SUBMITTED)))
            return ESurveyStatus.ANSWERED;

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
