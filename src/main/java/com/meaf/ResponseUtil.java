package com.meaf;

import com.meaf.entities.Role;
import com.meaf.web.HibernateUtil;

import javax.persistence.EntityManager;

public class ResponseUtil {

    public static String getRole(long id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        String rn = entityManager.find(Role.class, 1).getRolename();
        entityManager.close();
        return rn;
    }
}