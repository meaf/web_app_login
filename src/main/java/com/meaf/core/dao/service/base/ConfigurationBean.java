package com.meaf.core.dao.service.base;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import static com.meaf.core.meta.ProjectConstants.PERSISTENCE_UNIT_NAME;

@Stateful
public class ConfigurationBean {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
