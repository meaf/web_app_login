package com.meaf.core.dao.service;

import com.meaf.core.dao.service.project.AnswerService;
import com.meaf.core.dao.service.project.ProjectStageService;
import com.meaf.core.dao.service.project.QuestionService;
import com.meaf.core.dao.service.project.SurveyService;
import com.meaf.core.dao.service.users.ProjectUserConnectionService;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EProjectRole;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class AnswersAccountant {

    @Inject
    private ProjectStageService projectStageService;
    @Inject
    private SurveyService surveyService;
    @Inject
    private QuestionService questionService;
    @Inject
    private AnswerService answerService;
    @Inject
    private ProjectUserConnectionService projectUserConnectionService;

    public List<Answer> getQuestionRelatedAnswers(@NotNull Question question) {
        return answerService.getBranchedUnfiltered(question.getId());
    }

    public List<Answer> getSurveyRelatedAnswers(@NotNull Survey survey) {
        List<Question> questions = questionService.getBranched(survey.getId());
        return questions.stream().map(this::getQuestionRelatedAnswers).collect(LinkedList::new, List::addAll, List::addAll);
    }

    public List<Answer> getStagesRelatedAnswers(@NotNull ProjectStage stage) {
        List<Survey> surveys = surveyService.getBranched(stage.getId());
        return surveys.stream().map(this::getSurveyRelatedAnswers).collect(LinkedList::new, List::addAll, List::addAll);
    }

    public List<Answer> getProjectRelatedAnswers(@NotNull Project project) {
        List<ProjectStage> projectStages = projectStageService.getBranched(project.getId());
        return projectStages.stream().map(this::getStagesRelatedAnswers).collect(LinkedList::new, List::addAll, List::addAll);
    }

    public List<Question> getSurveyRelatedQuestions(@NotNull Survey survey) {
        return questionService.getBranched(survey.getId());
    }

    public List<Question> getStagesRelatedQuestions(@NotNull ProjectStage stage) {
        List<Survey> surveys = surveyService.getBranched(stage.getId());
        return surveys.stream().map(this::getSurveyRelatedQuestions).collect(LinkedList::new, List::addAll, List::addAll);
    }

    public List<Question> getProjectRelatedQuestions(@NotNull Project project) {
        List<ProjectStage> projectStages = projectStageService.getBranched(project.getId());
        return projectStages.stream().map(this::getStagesRelatedQuestions).collect(LinkedList::new, List::addAll, List::addAll);
    }

    public List<User> getProjectExperts(Project project) {
        List<ProjectUserConnection> connections = projectUserConnectionService.getProjectConnections(project, EProjectRole.EXPERT);
        return connections.stream().map(ProjectUserConnection::getUser).collect(Collectors.toList());
    }


}
