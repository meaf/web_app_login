package com.meaf.core.dao.service;

import com.meaf.controllers.ConfigurationBean;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public abstract class ABaseService implements Serializable {

//    @Resource
//    SessionContext sessionContext;

    @EJB
    protected ConfigurationBean configuration;

//    protected Logger getLogger() {
//        return LogManager.getLogManager().getLogger(getServiceName());
//    }

    protected EntityManager getEntityManager() {
        return configuration.getEntityManager();
    }
}
