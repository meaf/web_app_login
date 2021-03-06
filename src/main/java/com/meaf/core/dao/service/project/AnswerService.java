package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.Answer;
import com.meaf.core.entities.ProjectUserConnection;
import com.meaf.core.entities.Question;
import com.meaf.core.meta.EAnswerStatus;
import com.meaf.core.meta.EProjectRole;

import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class AnswerService extends ABaseService<Answer> {

    @Inject
    SessionManagementHelper sessionManagementHelper;

    @Override
    public List<Answer> getBranched(Long rootNode) {
        List<Answer> answers = getBranchedUnfiltered(rootNode);
        Optional<Answer> optAnsw = answers.stream().findAny();
        if (optAnsw.isPresent()) {
            ProjectUserConnection connection = sessionManagementHelper.getCurrentSessionProjectUserConnection();
            if (connection == null)
                connection = sessionManagementHelper.getConnectionBetween(optAnsw.get().getRootProject(), sessionManagementHelper.getCurrentUser());
            if (connection != null) {
                if (EProjectRole.EXPERT.equals(connection.getRole())) {
                    return filterOtherUsersProjects(answers)
                            .stream()
                            .filter(a -> a.getUser().equals(sessionManagementHelper.getCurrentUser()))
                            .collect(Collectors.toList());
                }
                if (EProjectRole.ORGANIZER.equals(connection.getRole()))
                    return answers;
            }
        }
        return new LinkedList<>();
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


    @Override
    public Answer add(Answer obj) throws Exception {
        obj.setUser(sessionManagementHelper.getCurrentUser());
        return super.add(obj);
    }

    public List<Answer> getBranchedUnfiltered(Long rootNode) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
        Root<Answer> root = cq.from(Answer.class);
        cq.select(root).where(cb.equal(root.get("question").get("id"), rootNode));
        List<Answer> answers = getEntityManager().createQuery(cq).getResultList();
        return answers;
    }

    public List<Answer> getOrgViewByQuestion(Question question) {
        List<Answer> answers = getBranchedUnfiltered(question.getId()).stream()
                .filter(a -> a.getStatus() == EAnswerStatus.SUBMITTED)
                .map(this::markWithUserNumber)
                .collect(Collectors.toList());
        return answers;
    }

    private Answer markWithUserNumber(Answer ans) {
        ProjectUserConnection connection =
                sessionManagementHelper.getConnectionBetween(ans.getRootProject(), ans.getUser());
        ans.setUserNumber(connection.getNumber());
        return ans;
    }
}
