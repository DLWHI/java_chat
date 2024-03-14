package com.dlwhi.server.repositories;

import java.util.List;

public interface CrudRepository<T> {
    T findById(long id);
    List<T> getAll();
    boolean save(T entity);
    boolean update(T entity);
    void delete(long id);
}
