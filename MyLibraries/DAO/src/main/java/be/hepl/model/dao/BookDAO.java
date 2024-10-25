package be.hepl.model.dao;

import be.hepl.model.entity.Book;

import java.util.List;

public class BookDAO implements CRUD {
    @Override
    public boolean create(Object entity) {
        return false;
    }

    @Override
    public Object findById(int id) {
        return null;
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public void delete(int id) {

    }
}
