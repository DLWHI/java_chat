package com.dlwhi.server.repositories;

import java.util.List;

public interface CrudRepository<T> {
    T findById(Long id);
    List<T> getAll();
    void save(T entity);
    void update(T entity);
    void delete(Long id);
}
