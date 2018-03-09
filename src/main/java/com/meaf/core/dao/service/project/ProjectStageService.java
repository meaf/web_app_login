package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.ProjectStage;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
public class ProjectStageService extends ABaseService<ProjectStage> {

    @Override
    public List<ProjectStage> getBranch(Long rootNode) throws IllegalAccessException {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProjectStage> cq = cb.createQuery(ProjectStage.class);
        Root<ProjectStage> root = cq.from(ProjectStage.class);
        cq.select(root).where(cb.equal(root.get("project").get("id"), rootNode));
        List<ProjectStage> stages = getEntityManager().createQuery(cq).getResultList();
        return stages;
    }

    @Override
    public List<ProjectStage> getAll() {
        return getEntityManager().createQuery("select u from ProjectStage u", ProjectStage.class).getResultList();
    }

    @Override
    public ProjectStage getById(Long id) {
        return getEntityManager().find(ProjectStage.class, id);
    }

    @Override
    public void update(ProjectStage projectStage) {
        ProjectStage ps = getById(projectStage.getId());
        ps.setName(projectStage.getName());
    }
}
