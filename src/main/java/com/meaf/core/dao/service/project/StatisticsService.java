package com.meaf.core.dao.service.project;

import com.meaf.core.entities.*;
import com.meaf.core.meta.EAnswerStatus;
import com.meaf.core.meta.ESurveyStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class StatisticsService {

    @Inject
    private ProjectStageService projectStageService;
    @Inject
    private SurveyService surveyService;
    @Inject
    private QuestionService questionService;
    @Inject
    private AnswerService answerService;

    public ESurveyStatus calculateStatusForQuestion(Question question) {
        return calculate(getQuestionRelatedAnswers(question));
    }

    public ESurveyStatus calculateStatusForSurvey(Survey survey) {
        return calculate(getSurveyRelatedAnswers(survey));
    }

    public ESurveyStatus calculateStatusForProjectStage(ProjectStage projectStage) {
        return calculate(getStagesRelatedAnswers(projectStage));
    }

    public ESurveyStatus calculateStatusForProject(Project project) {
        return calculate(getProjectRelatedAnswers(project));
    }

    private List<Answer> getQuestionRelatedAnswers(@NotNull Question question) {
        return answerService.getBranched(question.getId());
    }

    private List<Answer> getSurveyRelatedAnswers(@NotNull Survey survey) {
        List<Question> questions = questionService.getBranched(survey.getId());
        return questions.stream().map(this::getQuestionRelatedAnswers).collect(LinkedList::new, List::addAll, List::addAll);
    }

    private List<Answer> getStagesRelatedAnswers(@NotNull ProjectStage stage) {
        List<Survey> surveys = surveyService.getBranched(stage.getId());
        return surveys.stream().map(this::getSurveyRelatedAnswers).collect(LinkedList::new, List::addAll, List::addAll);
    }

    private List<Answer> getProjectRelatedAnswers(@NotNull Project project) {
        List<ProjectStage> projectStages = projectStageService.getBranched(project.getId());
        return projectStages.stream().map(this::getStagesRelatedAnswers).collect(LinkedList::new, List::addAll, List::addAll);
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

}
