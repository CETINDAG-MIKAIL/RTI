package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.viewmodel.AuthorSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorDAO implements CRUD {
    private ConnectDB connectDB;
    private ArrayList<Author> authors;

    public AuthorDAO() {
        connectDB = new ConnectDB();
        authors = new ArrayList<>();
    }

    public ArrayList<Author> getList() {
        return authors;
    }

    public Author getById(Integer id) {
        for (Author entity : authors) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Author> load() {
        return this.load(null);
    }

    public ArrayList<Author> load(AuthorSearchVM csvm) {
        ArrayList<Author> authors = new ArrayList<>();
        try {
            String sql = "SELECT " +
                    "authors.id, " +
                    "authors.last_name, " +
                    "authors.first_name, " +
                    "authors.birth_date " + // Correction : suppression de la virgule
                    "FROM authors ";

            if (csvm != null) {
                String where = " WHERE 1=1 ";
                if (csvm.getId() != null) {
                    where += "AND authors.id = ? ";
                }
                if (csvm.getLast_name() != null && csvm.getLast_name().length() > 0) {
                    where += "AND authors.last_name LIKE ? ";
                }
                if (csvm.getFirst_name() != null && csvm.getFirst_name().length() > 0) {
                    where += "AND authors.first_name LIKE ? ";
                }
                if (csvm.getBirth_date() != null) {
                    where += "AND authors.birth_date LIKE ? ";
                }
                sql += where + "ORDER BY authors.last_name ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (csvm != null) {
                int paramNumber = 0;
                if (csvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }
                if (csvm.getLast_name() != null && csvm.getLast_name().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + csvm.getLast_name() + "%");
                }
                if (csvm.getFirst_name() != null && csvm.getFirst_name().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + csvm.getFirst_name() + "%");
                }
                if (csvm.getBirth_date() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getBirth_date());
                }
            }

            ResultSet rs = stmt.executeQuery();
            authors.clear();
            while (rs.next()) {
                Integer authorId = rs.getInt("authors.id");
                String lastName = rs.getString("authors.last_name");
                String firstName = rs.getString("authors.first_name");
                String birthDate = rs.getString("authors.birth_date");
                Author author = new Author(authorId, lastName, firstName, birthDate);
                authors.add(author);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return authors;
        }
    }

    public void save(Author author) {
        try {
            String sql;
            if (author != null) {
                if (author.getId() != null) { // UPDATE
                    sql = "UPDATE authors SET "
                            + " last_name = ?, "
                            + " first_name = ?, "
                            + " birth_date = ? "
                            + " WHERE id = ?";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setString(1, author.getLast_name());
                    pStmt.setString(2, author.getFirst_name());
                    pStmt.setString(3, author.getBirth_date());
                    pStmt.setInt(4, author.getId());
                    pStmt.executeUpdate();
                    pStmt.close();
                } else { // CREATE
                    sql = "INSERT INTO authors (last_name, first_name, birth_date) VALUES (?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setString(1, author.getLast_name());
                    pStmt.setString(2, author.getFirst_name());
                    pStmt.setString(3, author.getBirth_date());
                    pStmt.executeUpdate();
                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    author.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Author author) {
        if (author != null && author.getId() != null) {
            this.delete(author.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                String sql = "DELETE FROM authors WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
