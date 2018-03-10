package com.meaf.core.dao.service.base;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless
public class ContextBean {

    @Resource
    private SessionContext sessionContext;

    public SessionContext getSessionContext() {
        return sessionContext;
    }
}
