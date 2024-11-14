package be.hepl.model.dao;

import be.hepl.model.entity.Author;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.SubjectSearchVM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface CRUD<T, S>{
    ArrayList<T> getList();
    T getById(Integer id);
    void save(T t);

    void create(T entity);

    ArrayList<T> read();
    ArrayList<T> read(S search);

    void update(T entity);

    void delete(Integer id);
    void delete(T entity);
}