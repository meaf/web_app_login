package com.meaf.core.dao.service.users;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.dao.service.base.Response;
import com.meaf.core.entities.ProjectUserConnection;
import com.meaf.core.entities.Question;
import com.meaf.core.entities.Role;
import com.meaf.core.entities.User;
import com.meaf.core.meta.EUserRole;

import javax.faces.application.FacesMessage;
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

    public Response connectUserWithInvite(User user, String invite) {
        Response response = new Response();

        ProjectUserConnection con = findSingleByWhereClause(ProjectUserConnection.class, "invite", invite);
        if (con == null || con.getUser() != null) {
            response.setSeverity(FacesMessage.SEVERITY_WARN);
            response.setTitle("Запрошення не вірне");
            response.setInfo("");
            return response;
        }
        con.setUser(user);
        getEntityManager().persist(con);

        response.setSeverity(FacesMessage.SEVERITY_INFO);
        response.setTitle("Успіх!");
        response.setInfo("");
        return response;

    }

    public void answer(Question question, String answer) {
        request.getRemoteUser();
    }

    @Override
    public List<User> getBranched(Long rootNode) throws IllegalAccessException {
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

    public boolean isUsernameTaken(String username) {
        return getEntityManager()
                .createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList().isEmpty();
    }
}