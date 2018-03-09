package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.Project;

import javax.inject.Named;
import java.util.List;

@Named
public class ProjectService extends ABaseService<Project> {


    public void addProject(String projectName) {
        super.add(new Project(projectName));
    }


    @Override
    public List<Project> getBranch(Long rootNode) throws IllegalAccessException {
        throw new IllegalAccessException("is the rootest");
    }

    @Override
    public List<Project> getAll() {
        return getEntityManager().createQuery("select u from Project u", Project.class).getResultList();
    }

    @Override
    public Project getById(Long id) {
        return getEntityManager().find(Project.class, id);
    }

    @Override
    public void update(Project project) {
        Project p = getById(project.getId());
        p.setName(project.getName());
        p.setDescription(project.getDescription());
        commit();
    }
}

