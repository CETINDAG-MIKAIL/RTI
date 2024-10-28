package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;

import java.io.InterruptedIOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDAO implements CRUD<Book, BookSearchVM> {
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
        books = read(bsvm);
        return books;
    }
    public void save(Book book) {
        try {
            String sql;
            if (book != null) {
                if (book.getId() != null) { // UPDATE
                   update(book);
                } else { // CREATE
                    create(book);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



   @Override
    public void create(Book book) throws SQLException {
       String sql = "INSERT INTO books (author_id, subject_id, title, isbn, page_count, stock_quantity, publish_year, price) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
       PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
       pStmt.setInt(1, book.getAuthor().getId());
       pStmt.setInt(2, book.getSubject().getId());
       pStmt.setString(3, book.getTitle());
       pStmt.setString(4, book.getIsbn());
       pStmt.setInt(5, book.getPageCount());
       pStmt.setInt(6, book.getStockQuantity());
       pStmt.setInt(7, book.getPublishYear());
       pStmt.setFloat(8, book.getPrice());
       pStmt.executeUpdate();
       ResultSet rs = pStmt.getGeneratedKeys();
       rs.next();
       book.setId((int) rs.getLong(1));
       rs.close();
       pStmt.close();
    }

    @Override
    public ArrayList<Book> read() {
        return this.load(null);
    }

    @Override
    public ArrayList<Book> read(BookSearchVM search) {
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
                    "books.subject_id " +
                    "FROM books ";

            if (search != null) {
                String where = " WHERE 1=1 ";
                if (search.getId() != null) {
                    where += "AND books.id = ? ";
                }
                if (search.getTitle() != null && search.getTitle().length() > 0) {
                    where += "AND books.title LIKE ? ";
                }
                if (search.getIsbn() != null && search.getIsbn().length() > 0) {
                    where += "AND books.isbn LIKE ? ";
                }
                if (search.getMinPageCount() != null) {
                    where += "AND books.page_count >= ? ";
                }
                if (search.getMaxPageCount() != null) {
                    where += "AND books.page_count <= ? ";
                }
                if (search.getMinStockQuantity() != null) {
                    where += "AND books.stock_quantity >= ? ";
                }
                if (search.getMinStockQuantity() != null) {
                    where += "AND books.publish_year >= ? ";
                }
                if (search.getMaxPublishYear() != null) {
                    where += "AND books.publish_year <= ? ";
                }
                if (search.getMinPrice() > 0) {
                    where += "AND books.price >= ? ";
                }
                if (search.getMaxPrice() > 0) {
                    where += "AND books.price <= ? ";
                }
                sql += where + "ORDER BY books.title ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (search != null) {
                int paramNumber = 0;
                if (search.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getId());
                }
                if (search.getTitle() != null && search.getTitle().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + search.getTitle() + "%");
                }
                if (search.getIsbn() != null && search.getIsbn().length() > 0) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + search.getIsbn() + "%");
                }
                if (search.getMinPageCount() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getMinPageCount());
                }
                if (search.getMaxPageCount() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getMaxPageCount());
                }
                if (search.getMinStockQuantity() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getMinStockQuantity());
                }
                if (search.getMinPublishYear() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getMinPublishYear());
                }
                if (search.getMaxPublishYear() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, search.getMaxPublishYear());
                }
                if (search.getMinPrice() > 0) {
                    paramNumber++;
                    stmt.setFloat(paramNumber, search.getMinPrice());
                }
                if (search.getMaxPrice() > 0) {
                    paramNumber++;
                    stmt.setFloat(paramNumber, search.getMaxPrice());
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


    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET "
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
        pStmt.setInt(5, book.getPageCount());
        pStmt.setInt(6, book.getStockQuantity());
        pStmt.setInt(7, book.getPublishYear());
        pStmt.setFloat(8, book.getPrice());
        pStmt.setInt(9, book.getId());
        pStmt.executeUpdate();
        pStmt.close();
    }

    @Override
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
    public void delete(Book book) {
        if (book != null && book.getId() != null) {
            this.delete(book.getId());
        }
    }


}
