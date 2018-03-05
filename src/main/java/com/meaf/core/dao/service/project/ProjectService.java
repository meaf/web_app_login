package com.meaf.core.dao.service.project;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.Project;

import javax.inject.Named;
import java.util.List;

@Named
public class ProjectService extends ABaseService<Project, Void> {


    public void addProject(String projectName) {
        Project project = new Project(projectName);
        getEntityManager().persist(project);
    }


    @Override
    public List<Project> getBranch(Void rootNode) throws IllegalAccessException {
        throw new IllegalAccessException("is the rootest");
    }

    @Override
    public List<Project> getAll() {
        return getEntityManager().createQuery("select u from Project u", Project.class).getResultList();
    }

    @Override
    public Project getById(Long id) {
        return null;
    }

    @Override
    public void update(Project project) {

    }
}

