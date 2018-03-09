package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.Question;
import com.meaf.core.entities.Survey;
import com.meaf.core.meta.ESurveyStatus;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
public class QuestionService extends ABaseService<Question> {


    public void addQuestion(String questionText, Survey survey) {
        Question question = new Question(questionText, ESurveyStatus.NEW, survey);
        getEntityManager().persist(question);
    }

    @Override
    public List<Question> getBranch(Long rootNode) throws IllegalAccessException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
        Root<Question> root = cq.from(Question.class);
        cq.select(root).where(cb.equal(root.get("survey").get("id"), rootNode));
        List<Question> answers = getEntityManager().createQuery(cq).getResultList();
        return answers;
    }

    @Override
    public List<Question> getAll() {
        return getEntityManager().createQuery("select u from Question u", Question.class).getResultList();
    }

    @Override
    public Question getById(Long id) {
        return getEntityManager().find(Question.class, id);
    }

    @Override
    public void update(Question question) {
        Question qu = getById(question.getId());
        qu.setText(question.getText());
        qu.setStatus(question.getStatus());
    }
}