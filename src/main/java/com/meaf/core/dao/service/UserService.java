package com.meaf.core.dao.service;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EProjectRole;
import com.meaf.core.meta.EUserRole;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.*;
import java.util.List;

@Named
public class UserService extends ABaseService<User> {
    @Inject
    HttpServletRequest request;

    public void addUser(String login, String password, Role role) throws Exception {
        super.add(new User(login, password, role));
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

    public Role getRoleByName(EUserRole role) {
        return findSingleByWhereClause(Role.class, "rolename", role.name());
    }

    public void connectUserToProject(User user, Project project, EProjectRole role) {
        ProjectUserConnection con = new ProjectUserConnection(user, project, role);
    }

    public void connectUserWithInvite(User user, String invite) {


//        ProjectUserConnection con = new ProjectUserConnection(user, project, role);
    }

    public void answer(Question question, String answer) {
        request.getRemoteUser();
    }

    @Override
    public List<User> getBranch(Long rootNode) throws IllegalAccessException {
        throw new IllegalAccessException("has no root");
    }

    @Override
    public List<User> getAll() {
        return getEntityManager().createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getById(Long id) {
        return getEntityManager().find(User.class, id);
    }

    public User getUserByName(String username) {
        return findSingleByWhereClause(User.class, "username", username);
    }

    @Override
    public void update(User user) {
        User olUser = getById(user.getId());
        olUser.setPassword(user.getPassword());
        olUser.setUsername(user.getUsername());
    }

    public void addConnection(ProjectUserConnection connection) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {

        getUTx().begin();
        try {
            getEntityManager().persist(connection);
        } catch (Exception ex) {
            getUTx().rollback();
            throw ex;
        }
        getUTx().commit();
    }
}