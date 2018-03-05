package com.meaf.core.dao.service.base;

import com.meaf.controllers.ConfigurationBean;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class ABaseService<T, S> implements Serializable, ICrudService<T, S> {
    @EJB
    protected ConfigurationBean configuration;

    protected EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }

    @Override
    public void add(T obj) {
        getEntityManager().persist(obj);
    }

    @Override
    public void delete(Long id) {
        T obj = getById(id);
        getEntityManager().remove(obj);
    }
}
