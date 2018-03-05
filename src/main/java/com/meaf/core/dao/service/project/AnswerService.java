package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.Answer;
import com.meaf.core.entities.Question;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
@SessionScoped
public class AnswerService extends ABaseService<Answer, Question> {

    @Override
    public List<Answer> getBranch(Question rootNode) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
        Root<Answer> root = cq.from(Answer.class);
        cq.select(root).where(cb.equal(root.get("question"), rootNode));
        List<Answer> answers = getEntityManager().createQuery(cq).getResultList();
        return answers;
    }

    @Override
    public List<Answer> getAll() {
        return getEntityManager().createQuery("select u from Answer u", Answer.class).getResultList();
    }

    @Override
    public Answer getById(Long id) {
        return null;
    }

    @Override
    public void update(Answer answer) {

    }
}
