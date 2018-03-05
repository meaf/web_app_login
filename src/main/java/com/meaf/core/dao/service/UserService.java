package com.meaf.core.dao.service;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Named
public class UserService extends ABaseService<User, Void> {
    @Inject
    HttpServletRequest request;

    public void addUser(String login, String password, Role role) {
        EntityManager em = getEntityManager();
        User user = new User(login, password, role);
        em.persist(user);
        em.persist(user);
    }

    public List<User> getUsersList() {
        List<User> users = getEntityManager().createQuery("select u from User u", User.class).getResultList();
        return users;
    }

    public void addRole(String roleName) {
        EntityManager em = getEntityManager();
        Role role = new Role(roleName);
        em.persist(role);
    }

    public List<Role> getRolesList() {
        List<Role> roles = getEntityManager().createQuery("select r from Role r", Role.class).getResultList();
        return roles;
    }

    public void connectUserToProject(User user, Project project) {
        ProjectUserConnection con = new ProjectUserConnection(user, project);
    }

    public void answer(Question question, String answer) {
        request.getRemoteUser();
    }

    @Override
    public List<User> getBranch(Void rootNode) throws IllegalAccessException {
        throw new IllegalAccessException("has no root");
    }

    @Override
    public List<User> getAll() {
        return getEntityManager().createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public void update(User user) {

    }
}