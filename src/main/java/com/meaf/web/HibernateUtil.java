package com.meaf.web;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import com.arjuna.ats.jta.TransactionManager;

public class HibernateUtil {

    private static final String PERSISTENT_UNIT_NAME = "WalDB";

    private static final EntityManagerFactory emf;
//    private static final TransactionManager s;

    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
