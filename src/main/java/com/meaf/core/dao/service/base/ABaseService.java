package com.meaf.core.dao.service.base;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.entities.Project;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.LinkedList;
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
    public T add(T obj) throws Exception {
        getUTx().begin();
        try {
            T saved = getEntityManager().merge(obj);
            getUTx().commit();
            return saved;
        } catch (Exception ex) {
//            getUTx().rollback();
            throw ex;
        }
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
            return new LinkedList<>();
        return projElements.stream().filter(userFilter).collect(Collectors.toList());
    }

    protected <U> U findSingleByWhereClause(Class clazz, String fieldName, Object val) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<U> cq = cb.createQuery(clazz);
        Root<U> root = cq.from(clazz);
        cq.select(root).where(cb.equal(root.get(fieldName), val));
        return getEntityManager().createQuery(cq).getResultList().stream().findAny().orElse(null);
    }

    protected UserTransaction getUTx() {
        return contextBean.getSessionContext().getUserTransaction();
    }
}
