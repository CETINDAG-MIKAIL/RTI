package be.hepl.model.dao;

import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.SubjectSearchVM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CRUD<T, S>{
    void create(T entity) throws SQLException;
    ArrayList<T> read();
    ArrayList<T> read(S search);
    void update(T entity) throws SQLException;
    void delete(Integer id);
    void delete(T entity);
}