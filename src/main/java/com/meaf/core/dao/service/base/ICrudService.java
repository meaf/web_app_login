package com.meaf.core.dao.service.base;

import java.util.List;

public interface ICrudService<T> {

    List<T> getBranch(Long rootNode) throws IllegalAccessException;

    List<T> getAll();

    T getById(Long id);

    void update(T obj);

    boolean add(T obj);

    boolean delete(Long id);

}
