package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDAO implements CRUD {
    private ConnectDB connectDB;
    private ArrayList<Book> books;

    public BookDAO() {
        connectDB = new ConnectDB();
        books = new ArrayList<>();
    }

    public ArrayList<Book> getList() {
        return books;
    }

    public Book getById(Integer id) {
        for (Book book : books) {
            if (Objects.equals(book.getId(), id)) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> load() {
        return this.load(null);
    }

    public ArrayList<Book> load(BookSearchVM bsvm) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT " +
                    "books.id, " +
                    "books.title, " +
                    "books.isbn, " +
                    "books.page_count, " +
                    "books.stock_quantity, "+
                    "books.publish_year, " +
                    "books.price, "+
                    "books.author_id, "+
                    "books.subject_id, " +
                    "FROM books ";

            if (bsvm != null) {
                String where = " WHERE 1=1 ";
                if (bsvm.getId() != null) {
                    where += "AND books.id = ? ";
                }
                if (bsvm.getTitle() != null && bsvm.getTitle().length() > 0) {
                    where += "AND books.title LIKE ? ";
                }
                if (bsvm.getIsbn() != null && bsvm.getIsbn().length() > 0) {
                    where += "AND books.isbn LIKE ? ";
                }
                if (bsvm.getPage_count() != null) {
                    where += "AND books.page_count = ? ";
                }
                if (bsvm.getStock_quantity() != null) {
                    where += "AND books.stock_quantity = ? ";
                }
                if (bsvm.getPublish_year() != null) {
                    where += "AND books.publish_year = ? ";
                }
                if (bsvm.getPrice() > 0) {
                    where += "AND books.price = ? ";
                }
                sql += where + "ORDER BY books.title ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (bsvm != null) {
                int paramNumber = 0;
                if (bsvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, bsvm.getId());
                }
                if (bsvm.getTitle() != null && bsvm.getTitle().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + bsvm.getTitle() + "%");
                }
                if (bsvm.getIsbn() != null && bsvm.getIsbn().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + bsvm.getIsbn() + "%");
                }
                if (bsvm.getPage_count() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, bsvm.getPage_count());
                }
                if (bsvm.getStock_quantity() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, bsvm.getStock_quantity());
                }
                if (bsvm.getPublish_year() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, bsvm.getPublish_year());
                }
                if (bsvm.getPrice() > 0) {
                    paramNumber++;
                    stmt.setFloat(paramNumber, bsvm.getPrice());
                }
            }

            ResultSet rs = stmt.executeQuery();
            books.clear();
            while (rs.next()) {
                Integer id = rs.getInt("books.id");
                String title = rs.getString("books.title");
                String isbn = rs.getString("books.isbn");
                Integer pageCount = rs.getInt("books.page_count");
                Integer stockQuantity = rs.getInt("books.stock_quantity");
                Integer publishYear = rs.getInt("books.publish_year");
                float price = rs.getFloat("books.price");

                Author author = new AuthorDAO().getById(rs.getInt("books.author_id"));
                Subject subject = new SubjectDAO().getById(rs.getInt("books.subject_id"));

                Book book = new Book(id, author, subject, title, isbn, pageCount, stockQuantity, publishYear, price);
                books.add(book);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return books;
        }
    }
    public void save(Book book) {
        try {
            String sql;
            if (book != null) {
                if (book.getId() != null) { // UPDATE
                    sql = "UPDATE books SET "
                            + " author_id = ?, "
                            + " subject_id = ?, "
                            + " title = ?, "
                            + " isbn = ?, "
                            + " page_count = ?, "
                            + " stock_quantity = ?, "
                            + " publish_year = ?, "
                            + " price = ? "
                            + " WHERE id = ?";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setInt(1, book.getAuthor().getId());
                    pStmt.setInt(2, book.getSubject().getId());
                    pStmt.setString(3, book.getTitle());
                    pStmt.setString(4, book.getIsbn());
                    pStmt.setInt(5, book.getPage_count());
                    pStmt.setInt(6, book.getStock_quantity());
                    pStmt.setInt(7, book.getPublish_year());
                    pStmt.setFloat(8, book.getPrice());
                    pStmt.setInt(9, book.getId());
                    pStmt.executeUpdate();
                    pStmt.close();
                } else { // CREATE
                    sql = "INSERT INTO books (author_id, subject_id, title, isbn, page_count, stock_quantity, publish_year, price) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setInt(1, book.getAuthor().getId());
                    pStmt.setInt(2, book.getSubject().getId());
                    pStmt.setString(3, book.getTitle());
                    pStmt.setString(4, book.getIsbn());
                    pStmt.setInt(5, book.getPage_count());
                    pStmt.setInt(6, book.getStock_quantity());
                    pStmt.setInt(7, book.getPublish_year());
                    pStmt.setFloat(8, book.getPrice());
                    pStmt.executeUpdate();
                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    book.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Book book) {
        if (book != null && book.getId() != null) {
            this.delete(book.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                String sql = "DELETE FROM books WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
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
