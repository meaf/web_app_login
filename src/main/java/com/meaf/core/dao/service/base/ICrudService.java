package com.meaf.core.dao.service.base;

import java.util.List;

public interface ICrudService<T> {

    List<T> getBranch(Long rootNode) throws IllegalAccessException;

    List<T> getAll();

    T getById(Long id);

    void update(T obj) throws Exception;

    boolean add(T obj) throws Exception;

    boolean delete(Long id) throws Exception;

}
