package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.ProjectStage;
import com.meaf.core.entities.Survey;
import com.meaf.core.meta.ESurveyStatus;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
public class SurveyService extends ABaseService<Survey, ProjectStage> {

    public void addSurvey(ProjectStage projectStage, String topic) {
        Survey survey = new Survey(topic, ESurveyStatus.NEW, projectStage);
        getEntityManager().persist(survey);
    }

    @Override
    public List<Survey> getBranch(ProjectStage rootNode) throws IllegalAccessException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> root = cq.from(Survey.class);
        cq.select(root).where(cb.equal(root.get("project"), rootNode));
        List<Survey> stages = getEntityManager().createQuery(cq).getResultList();
        return stages;
    }

    @Override
    public List<Survey> getAll() {
        return getEntityManager().createQuery("select u from Survey u", Survey.class).getResultList();
    }

    @Override
    public Survey getById(Long id) {
        return null;
    }

    @Override
    public void update(Survey survey) {
    }
}
