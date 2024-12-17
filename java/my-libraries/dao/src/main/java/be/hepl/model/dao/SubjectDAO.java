package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.SubjectSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectDAO implements CRUD<Subject, SubjectSearchVM> {
    private ConnectDB connectDB;
    private ArrayList<Subject> subjects;

    public SubjectDAO() {
        connectDB = ConnectDB.getInstance();
        subjects = new ArrayList<>();
    }

    @Override
    public ArrayList<Subject> getList() {
        return subjects;
    }

    @Override
    public Subject getById(Integer id) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) {
                return subject;
            }
        }
        return null;
    }

    @Override
    public void save(Subject subject) {
        if (subject != null) {
            if (subject.getId() != null) { // UPDATE
              update(subject);
            } else { // CREATE
             create(subject);
            }
        }
    }

    @Override
    public void create(Subject subject) {
        try {
            String sql = "INSERT INTO subjects (name) VALUES (?)";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, subject.getName());
            pStmt.executeUpdate();
            ResultSet rs = pStmt.getGeneratedKeys();
            rs.next();
            subject.setId((int) rs.getLong(1));
            rs.close();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Subject> read(SubjectSearchVM search) {
        try {
            String sql = "SELECT"+
                    " subjects.id, "+
                    "subjects.name  "+
                    "FROM subjects ";

            if (search != null) {
                String where = " WHERE 1=1 ";
                if (search.getId() != null) {
                    where += "AND subjects.id = ? ";
                }
                if (search.getName() != null && !search.getName().isEmpty()) {
                    where += "AND subjects.name LIKE ? ";
                }
                sql += where + " ORDER BY subjects.id ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (search != null) {
                int paramNumber = 0;
                if (search.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getId());
                }
                if (search.getName() != null && !search.getName().isEmpty()) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + search.getName() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            subjects.clear();
            while (rs.next()) {
                Integer id = rs.getInt("subjects.id");
                String name = rs.getString("subjects.name");

                Subject subject = new Subject(id, name);
                subjects.add(subject);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return subjects;
        }
    }

    @Override
    public ArrayList<Subject> read() {
        return this.read(null);
    }

    @Override
    public void update(Subject subject) {
        try {
            String sql = "UPDATE subjects SET name = ? WHERE id = ?";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
            pStmt.setString(1, subject.getName());
            pStmt.setInt(2, subject.getId());
            pStmt.executeUpdate();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Integer id) {
        if (id != null) {
            try {
//                String deleteBooksSQL = "DELETE FROM books WHERE subject_id = ?";
//                PreparedStatement stmtBooks = connectDB.getConn().prepareStatement(deleteBooksSQL);
//                stmtBooks.setInt(1, id);
//                stmtBooks.executeUpdate();
//                stmtBooks.close();

                String sql = "DELETE FROM subjects WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void delete(Subject subject) {
        if(subject != null && subject.getId() != null) {
            this.delete(subject.getId());
        }
    }
}
