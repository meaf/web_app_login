package com.meaf.core.utils;

import com.meaf.core.dao.service.base.ConfigurationBean;
import com.meaf.core.dao.service.base.ContextBean;
import com.meaf.core.dao.service.base.IProjectElement;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

public class RelationUtil {
    @Inject
    ConfigurationBean configurationBean;
    @Inject
    ContextBean contextBean;

    public void update(IProjectElement obj) throws Exception {
        UserTransaction utx = contextBean.getSessionContext().getUserTransaction();

        utx.begin();
        obj.updateActionTime();
        utx.commit();
    }


}
