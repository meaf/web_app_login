package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.Answer;
import com.meaf.core.entities.ProjectUserConnection;
import com.meaf.core.meta.EUserRole;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
@SessionScoped
public class AnswerService extends ABaseService<Answer> {

    @Override
    public List<Answer> getBranch(Long rootNode) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
        Root<Answer> root = cq.from(Answer.class);
        cq.select(root).where(cb.equal(root.get("question").get("id"), rootNode));
        List<Answer> answers = getEntityManager().createQuery(cq).getResultList();

        ProjectUserConnection connection = sessionManagementHelper.getCurrentSessionProjectUserConnection();
        if (connection != null) {
            if (EUserRole.expert.isRole(connection.getRole())) {
                return filterOtherUsers(answers);
            }
            if (EUserRole.organizer.isRole(connection.getRole()))
                return answers;
        }
        return null;
    }

    @Override
    public List<Answer> getAll() {
        return getEntityManager().createQuery("select u from Answer u", Answer.class).getResultList();
    }

    @Override
    public Answer getById(Long id) {
        return getEntityManager().find(Answer.class, id);
    }

    @Override
    public void update(Answer answer) throws Exception {
        getUTx().begin();
        Answer ans = getById(answer.getId());
        ans.setQuestion(answer.getQuestion());
        ans.setStatus(answer.getStatus());
        ans.setText(answer.getText());
        getUTx().commit();
//        commit();
    }


}
