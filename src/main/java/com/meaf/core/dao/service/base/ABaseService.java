package com.meaf.core.dao.service.base;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.entities.Project;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ABaseService<T> implements Serializable, ICrudService<T> {


    @EJB
    protected ConfigurationBean configuration;
    @EJB
    protected ContextBean contextBean;
    @Inject
    protected SessionManagementHelper sessionManagementHelper;

    protected EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }

    @Override
    public boolean add(T obj) throws Exception {
        getUTx().begin();
        try {
            getEntityManager().persist(obj);
        } catch (Exception ex) {
            getUTx().rollback();
            throw ex;
        }
        getUTx().commit();
        return true;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        getUTx().begin();
        T obj = getById(id);
        getEntityManager().remove(obj);
        getUTx().commit();
        return true;
    }

    protected Predicate<IProjectElement> userFilter = iProjectElement ->
            sessionManagementHelper
                    .getCurrentUserProjects().stream()
                    .map(Project::getId)
                    .collect(Collectors.toList())
                    .contains(iProjectElement.getRootProject().getId());

    protected <P extends IProjectElement> List<P> filterOtherUsers(List<P> projElements) {
        if (projElements == null)
            return null;
        return projElements.stream().filter(userFilter).collect(Collectors.toList());
    }

    protected UserTransaction getUTx() {
        return contextBean.getSessionContext().getUserTransaction();
    }
}
