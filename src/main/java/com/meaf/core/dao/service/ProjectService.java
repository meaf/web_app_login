package com.meaf.core.dao.service;

import com.meaf.core.entities.Project;
import com.meaf.core.entities.Role;
import com.meaf.core.entities.User;

import javax.persistence.EntityManager;

public class ProjectService extends ABaseService{

    public void addProject(String projectName) {
        EntityManager em = getEntityManager();
        Project project = new Project("newProj");
        em.persist(project);
    }

}
