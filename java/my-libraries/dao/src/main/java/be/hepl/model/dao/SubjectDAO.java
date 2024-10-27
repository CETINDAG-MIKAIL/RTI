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

public class SubjectDAO implements CRUD {
    private ConnectDB connectDB;
    private ArrayList<Subject> subjects;

    public SubjectDAO() {
        connectDB = new ConnectDB();
        subjects = new ArrayList<>();
    }

    public ArrayList<Subject> getList() {
        return subjects;
    }

    public Subject getById(Integer id) {
        for (Subject subject : subjects) {
            if (Objects.equals(subject.getId(), id)) {
                return subject;
            }
        }
        return null;
    }

    public ArrayList<Subject> load() {
        return this.load(null);
    }

    public ArrayList<Subject> load(SubjectSearchVM ssvm) {
        ArrayList<Subject> subjects = new ArrayList<>();
        try {
            String sql = "SELECT"+
                    " subjects.id, "+
                    "subjects.name,  "+
                    "FROM subjects ";

            if (ssvm != null) {
                String where = " WHERE 1=1 ";
                if (ssvm.getId() != null) {
                    where += "AND subjects.id = ? ";
                }
                if (ssvm.getName() != null && !ssvm.getName().isEmpty()) {
                    where += "AND subjects.name LIKE ? ";
                }
                sql += where + " ORDER BY subjects.name ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (ssvm != null) {
                int paramNumber = 0;
                if (ssvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, ssvm.getId());
                }
                if (ssvm.getName() != null && !ssvm.getName().isEmpty()) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + ssvm.getName() + "%");
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
    public void save(Subject subject) {
        try {
            String sql;
            if (subject != null) {
                if (subject.getId() != null) { // UPDATE
                    sql = "UPDATE subjects SET name = ? WHERE id = ?";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setString(1, subject.getName());
                    pStmt.setInt(2, subject.getId());
                    pStmt.executeUpdate();
                    pStmt.close();
                } else { // CREATE
                    sql = "INSERT INTO subjects (name) VALUES (?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setString(1, subject.getName());
                    pStmt.executeUpdate();
                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    subject.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Subject subject) {
        if (subject != null && subject.getId() != null) {
            this.delete(subject.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
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
