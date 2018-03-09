package com.meaf.core.dao.service.base;

import com.meaf.controllers.ConfigurationBean;
import com.meaf.core.entities.Role;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class ABaseService<T> implements Serializable, ICrudService<T> {
    @EJB
    protected ConfigurationBean configuration;

    protected EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }


    @Override
    public boolean add(T obj) {
        getEntityManager().persist(obj);
        commit();
        return true;
    }

    @Override
    public boolean delete(Long id) {
        T obj = getById(id);
        getEntityManager().remove(obj);
        commit();
        return true;
    }

    public void commit() {
        getEntityManager().find(Role.class, 0L);
    }
}
