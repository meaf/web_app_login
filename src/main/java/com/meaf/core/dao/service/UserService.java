package com.meaf.core.dao.service;

import com.meaf.core.entities.Role;
import com.meaf.core.entities.User;

import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.List;

@Named
public class UserService extends ABaseService {

    public void addUser(String login, String password, Role role) {
        EntityManager em = getEntityManager();
        User user = new User("admin", "admin", em.find(Role.class, 0l));
        em.persist(user);
    }

    public void addRole(String roleName) {
        EntityManager em = getEntityManager();
        Role role = new Role(roleName);
        em.persist(role);
    }

    public List<Role> getRolesList() {
        List<Role> roles = getEntityManager().createNamedQuery("select * from roles", Role.class).getResultList();
        return roles;
    }
}