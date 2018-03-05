package com.meaf.core.dao.service.base;

import java.util.List;

public interface ICrudService<T, S> {

    List<T> getBranch(S rootNode) throws IllegalAccessException;

    List<T> getAll();

    T getById(Long id);

    void update(T obj);

    void add(T obj);

    void delete(Long id);
}
