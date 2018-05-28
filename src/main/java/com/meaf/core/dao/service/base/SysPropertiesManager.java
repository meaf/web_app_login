package com.meaf.core.dao.service.base;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.entities.SysProperties;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

@Named
@SessionScoped
public class SysPropertiesManager {

    @EJB
    private ConfigurationBean configuration;
    @EJB
    private ContextBean contextBean;
    @Inject
    private SessionManagementHelper sessionManagementHelper;

    private EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }

    private UserTransaction getUTx() {
        return contextBean.getSessionContext().getUserTransaction();
    }

    public SysProperties getByKey(String key) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SysProperties> cq = cb.createQuery(SysProperties.class);
        Root<SysProperties> root = cq.from(SysProperties.class);
        cq.select(root).where(cb.equal(root.get("name"), key));
        return getEntityManager().createQuery(cq).getResultList().stream().findAny().orElse(null);
    }

    public void update(String key, String newValue) throws Exception {
        getUTx().begin();
        SysProperties sysProperty = getByKey(key);
        sysProperty.setValue(newValue);
        getUTx().commit();
    }

    public SysProperties add(SysProperties obj) throws Exception {
        getUTx().begin();
        try {
            SysProperties saved = getEntityManager().merge(obj);
            getUTx().commit();
            return saved;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
