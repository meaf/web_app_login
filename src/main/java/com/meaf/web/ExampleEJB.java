package com.meaf.web;

import com.meaf.entities.Role;
import com.meaf.entities.User;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateful
//
public class ExampleEJB {

    @PersistenceContext(unitName = "WalDB")
    private EntityManager entityManager;

    @Resource
    SessionContext sessionContext;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<User> getAllUsers(){
        Query query = entityManager.createQuery("select u from User u");
        return query.getResultList();
    }

    public boolean addAdmin(String username, String password)throws Exception {
//        UserTransaction ut = sessionContext.getUserTransaction();
//        ut.begin();
//        entityManager.getTransaction().begin();

        User userEntity = new User();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        Role role = entityManager.find(Role.class, 0l);
        userEntity.setRole(role);
        entityManager.persist(userEntity);

//        entityManager.getTransaction().commit();
//        ut.commit();
        return true;
    }

}
