package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.AuthorSearchVM;
import be.hepl.model.viewmodel.SubjectSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorDAO implements CRUD<Author, AuthorSearchVM> {
    private ConnectDB connectDB;
    private ArrayList<Author> authors;

    public AuthorDAO() {
        connectDB = new ConnectDB();
        authors = new ArrayList<>();
    }

    @Override
    public ArrayList<Author> getList() {
        return authors;
    }

    @Override
    public Author getById(int id) {
        for (Author author : authors) {
            if (author.getId() == id) {
                System.out.println("getById : parametre id :" + id);
                return author;
            }
        }
        return null;
    }

    @Override
    public void save(Author author) {
        if (author != null) {
            if (author.getId() != null) { // UPDATE
               update(author);
            } else { // CREATE
               create(author);
            }
        }
    }
    @Override
    public void create(Author author) {
        try {
            String sql = "INSERT INTO authors (last_name, first_name, birth_date) VALUES (?, ?, ?)";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, author.getLastName());
            pStmt.setString(2, author.getFirstName());
            pStmt.setString(3, author.getBirthDate());
            pStmt.executeUpdate();
            ResultSet rs = pStmt.getGeneratedKeys();
            rs.next();
            author.setId((int) rs.getLong(1));
            rs.close();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Author> read() {
        return this.read(null);
    }
    @Override
    public ArrayList<Author> read(AuthorSearchVM search) {
        try {
            String sql = "SELECT " +
                    "authors.id, " +
                    "authors.last_name, " +
                    "authors.first_name, " +
                    "authors.birth_date " +
                    "FROM authors ";

            if (search != null) {
                String where = " WHERE 1=1 ";
                if (search.getId() != null) {
                    where += "AND authors.id = ? ";
                }
                if (search.getLastName() != null && search.getLastName().length() > 0) {
                    where += "AND authors.last_name LIKE ? ";
                }
                if (search.getFirstName() != null && search.getFirstName().length() > 0) {
                    where += "AND authors.first_name LIKE ? ";
                }
                if (search.getBirthDate() != null && search.getBirthDate().length() > 0) {
                    where += "AND authors.birth_date = ? ";
                }
                sql += where + "ORDER BY authors.last_name ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (search != null) {
                int paramNumber = 0;
                if (search.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getId());
                }
                if (search.getLastName() != null && search.getLastName().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + search.getLastName() + "%");
                }
                if (search.getFirstName() != null && search.getFirstName().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + search.getFirstName() + "%");
                }
                if (search.getBirthDate() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, search.getBirthDate());
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

    @Override
    public void update(Author author) {
        try {
            String sql = "UPDATE authors SET "
                    + " last_name = ?, "
                    + " first_name = ?, "
                    + " birth_date = ? "
                    + " WHERE id = ?";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
            pStmt.setString(1, author.getLastName());
            pStmt.setString(2, author.getFirstName());
            pStmt.setString(3, author.getBirthDate());
            pStmt.setInt(4, author.getId());
            pStmt.executeUpdate();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void delete(Integer id) {
        {
            if (id != null) {
                try {
                    // Supprimer les livres associés à cet auteur
//                    String deleteBooksSQL = "DELETE FROM books WHERE author_id = ?";
//                    PreparedStatement stmtBooks = connectDB.getConn().prepareStatement(deleteBooksSQL);
//                    stmtBooks.setInt(1, id);
//                    stmtBooks.executeUpdate();
//                    stmtBooks.close();

                    // Supprimer l'auteur
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
    }

    @Override
    public void delete(Author author) {
        if(author != null && author.getId() != null) {
            this.delete(author.getId());
        }
    }
}
