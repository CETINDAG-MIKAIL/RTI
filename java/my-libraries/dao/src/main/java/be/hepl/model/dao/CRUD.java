package be.hepl.model.dao;

import java.util.List;

public interface CRUD<T>{
    boolean create(T entity);
    T findById(int id);
    List<T> findAll();
    boolean update(T entity);
    void delete(int id);
}