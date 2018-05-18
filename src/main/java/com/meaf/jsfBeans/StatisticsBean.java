package com.meaf.jsfBeans;

import com.meaf.core.dao.service.project.StatisticsService;
import com.meaf.core.entities.Project;
import com.meaf.core.entities.ProjectStage;
import com.meaf.core.entities.Question;
import com.meaf.core.entities.Survey;
import com.meaf.core.meta.ESurveyStatus;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ManagedBean
@RequestScoped
public class StatisticsBean implements Serializable {

    @Inject
    StatisticsService statisticsService;

    public String calculateStatus(Question question) {
        ESurveyStatus status = statisticsService.calculateStatusForQuestion(question);
        return status.name();
    }

    public String calculateStatus(Survey survey) {
        ESurveyStatus status = statisticsService.calculateStatusForSurvey(survey);
        return status.name();
    }

    public String calculateStatus(ProjectStage projectStage) {
        ESurveyStatus status = statisticsService.calculateStatusForProjectStage(projectStage);
        return status.name();
    }

    public String calculateStatus(Project project) {
        ESurveyStatus status = statisticsService.calculateStatusForProject(project);
        return status.name();
    }

}
