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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Inject
    private SessionManagementHelper sessionManagementHelper;

    public Set<Answer> getQuestionRelatedAnswers(@NotNull Question question, boolean filter) {
        Set<Answer> answers = question.getAnswers();
        if (answers == null)
            return Collections.EMPTY_SET;
        return filter
                ? answers.stream()
                .filter(a -> a.getUser().equals(sessionManagementHelper.getCurrentUser()))
                .collect(Collectors.toSet())
                : answers;
    }

    public Set<Answer> getSurveyRelatedAnswers(@NotNull Survey survey, boolean filter) {
        Set<Question> questions = survey.getQuestions();
        return questions.stream().map(s -> getQuestionRelatedAnswers(s, filter)).collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public Set<Answer> getStagesRelatedAnswers(@NotNull ProjectStage stage, boolean filter) {
        Set<Survey> surveys = stage.getSurveys();
        return surveys.stream().map(s -> getSurveyRelatedAnswers(s, filter)).collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public Set<Answer> getProjectRelatedAnswers(@NotNull Project project) {
        boolean filter = projectUserConnectionService.getUserProjectConnections(sessionManagementHelper.getCurrentUser(), project).getRole() == EProjectRole.EXPERT;
        Set<ProjectStage> projectStages = new HashSet<>(projectStageService.getBranched(project.getId()));
        return projectStages.stream().map(s -> getStagesRelatedAnswers(s, filter)).collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public Set<Question> getSurveyRelatedQuestions(@NotNull Survey survey) {
        return survey.getQuestions();
    }

    public Set<Question> getStagesRelatedQuestions(@NotNull ProjectStage stage) {
        Set<Survey> surveys = stage.getSurveys();
        return surveys.stream().map(this::getSurveyRelatedQuestions).collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public Set<Question> getProjectRelatedQuestions(@NotNull Project project) {
        Set<ProjectStage> projectStages = new HashSet<>(projectStageService.getBranched(project.getId()));
        return projectStages.stream().map(this::getStagesRelatedQuestions).collect(HashSet::new, Set::addAll, Set::addAll);
    }

    public List<User> getProjectExperts(Project project) {
        List<ProjectUserConnection> connections = projectUserConnectionService.getProjectConnections(project, EProjectRole.EXPERT);
        return connections.stream().map(ProjectUserConnection::getUser).collect(Collectors.toList());
    }


}
