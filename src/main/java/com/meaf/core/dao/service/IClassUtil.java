package com.meaf.core.dao.service;

import java.io.Serializable;

public interface IClassUtil extends Serializable {
    default String getServiceName() {
        return getClass().getName();
    }
}
