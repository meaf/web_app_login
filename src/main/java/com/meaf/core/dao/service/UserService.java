package com.meaf.core.dao.service;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EUserRole;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> root = cq.from(Role.class);
        cq.select(root).where(cb.equal(root.get("rolename"), role.name()));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public void connectUserToProject(User user, Project project, Role role) {
        ProjectUserConnection con = new ProjectUserConnection(user, project, role);
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