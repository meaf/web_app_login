package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.ProjectStage;
import com.meaf.core.entities.Question;
import com.meaf.core.entities.Survey;
import com.meaf.core.meta.ESurveyStatus;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
public class QuestionService extends ABaseService<Question, Survey> {


    public void addQuestion(ProjectStage projectStage, String questionText, Survey survey) {
        Question question = new Question(questionText, ESurveyStatus.NEW, survey);
        getEntityManager().persist(question);
    }

    @Override
    public List<Question> getBranch(Survey rootNode) throws IllegalAccessException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
        Root<Question> root = cq.from(Question.class);
        cq.select(root).where(cb.equal(root.get("survey"), rootNode));
        List<Question> answers = getEntityManager().createQuery(cq).getResultList();
        return answers;
    }

    @Override
    public List<Question> getAll() {
        return getEntityManager().createQuery("select u from Question u", Question.class).getResultList();
    }

    @Override
    public Question getById(Long id) {
        return null;
    }

    @Override
    public void update(Question question) {

    }
}
