package com.meaf.core.dao.service;

import com.meaf.core.entities.*;

import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Named
@SessionScoped
public class UserService extends ABaseService {
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

    public void connectUserToProject(User user, Project project){
        ProjectUserConnection con = new ProjectUserConnection(user, project);
    }

    public void answer(Question question, String answer){
        request.getRemoteUser();
    }

}