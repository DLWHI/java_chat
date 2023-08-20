package edu.school21.server.repositories;

import java.util.List;

public interface ICrudRepository<T> {
    T findById(Long id);
    List<T> findAll();
    T save(T user);
    void update(T user);
    void delete(Long id);
}
