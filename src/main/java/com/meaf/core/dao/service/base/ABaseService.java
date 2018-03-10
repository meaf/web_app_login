package com.meaf.core.dao.service.base;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.entities.Role;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ABaseService<T> implements Serializable, ICrudService<T> {

    @EJB
    protected ConfigurationBean configuration;
    @Inject
    protected SessionManagementHelper sessionManagementHelper;

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

    protected Predicate<IProjectElement> userFilter = iProjectElement -> sessionManagementHelper.getCurrentUserProjects().contains(iProjectElement.getRootProject());

    protected <P extends IProjectElement> List<P> filterOtherUsers(List<P> projElements) {
        if (projElements == null)
            return null;
        return projElements.stream().filter(userFilter).collect(Collectors.toList());
    }
}
