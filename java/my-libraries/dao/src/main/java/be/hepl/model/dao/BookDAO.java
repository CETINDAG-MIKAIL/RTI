package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDAO implements CRUD<Book, BookSearchVM> {
    private ConnectDB connectDB;
    private ArrayList<Book> books;

    public BookDAO() {
        connectDB = ConnectDB.getInstance();
        books = new ArrayList<>();
    }

    @Override
    public ArrayList<Book> getList() {
        return books;
    }

    @Override
    public Book getById(Integer id) {
        for (Book book : books) {
            if (book.getId() == id ) {
                return book;
            }
        }
        return null;
    }

    @Override
    public void save(Book book) {
        if (book != null) {
            if (book.getId() != null) { // UPDATE
                update(book);
            } else { // CREATE
                create(book);
            }
        }
    }

    @Override
    public void create(Book book) {
        try {
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
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Book> read() {
        return this.read(null);
    }
    @Override
    public ArrayList<Book> read(BookSearchVM search) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT " +
                    "books.id AS book_id, " +
                    "books.title, " +
                    "books.isbn, " +
                    "books.page_count, " +
                    "books.stock_quantity, " +
                    "books.publish_year, " +
                    "books.price, " +
                    "authors.id AS author_id, " +
                    "authors.last_name AS author_last_name, " +
                    "authors.first_name AS author_first_name, " +
                    "authors.birth_date AS author_birth_date, " +
                    "subjects.id AS subject_id, " +
                    "subjects.name AS subject_name " +
                    "FROM books " +
                    "JOIN authors ON books.author_id = authors.id " +
                    "JOIN subjects ON books.subject_id = subjects.id");

            // Building the WHERE clause
            if (search != null) {
                sql.append(" WHERE 1=1");

                if (search.getAuthor() != null && search.getAuthor().getId() != null) {
                    sql.append(" AND authors.id = ?");
                }
                if (search.getAuthor() != null) {
                    if (search.getAuthor().getFirstName() != null && !search.getAuthor().getFirstName().isEmpty()) {
                        sql.append(" AND authors.first_name LIKE ?");
                    }
                    if (search.getAuthor().getLastName() != null && !search.getAuthor().getLastName().isEmpty()) {
                        sql.append(" AND authors.last_name LIKE ?");
                    }
                }
                if (search.getSubject() != null) {
                    if(search.getSubject().getId() != null)
                        sql.append(" AND subjects.id = ?");
                    if (search.getSubject().getName() != null && !search.getSubject().getName().isEmpty()) {
                        sql.append(" AND subjects.name LIKE ?");
                    }
                }

                if (search.getId() != null) {
                    sql.append(" AND books.id = ?");
                }
                if (search.getTitle() != null && !search.getTitle().isEmpty()) {
                    sql.append(" AND books.title LIKE ?");
                }
                if (search.getIsbn() != null && !search.getIsbn().isEmpty()) {
                    sql.append(" AND books.isbn LIKE ?");
                }
                if (search.getMinPageCount() != null) {
                    sql.append(" AND books.page_count >= ?");
                }
                if (search.getMaxPageCount() != null) {
                    sql.append(" AND books.page_count <= ?");
                }
                if (search.getMinPrice() != null) {
                    sql.append(" AND books.price >= ?");
                }
                if (search.getMaxPrice() != null) {
                    sql.append(" AND books.price <= ?");
                }
                if (search.getMinStockQuantity() != null) {
                    sql.append(" AND books.stock_quantity >= ?");
                }
                if (search.getMinPublishYear() != null) {
                    sql.append(" AND books.publish_year >= ?");
                }
                if (search.getMaxPublishYear() != null) {
                    sql.append(" AND books.publish_year <= ?");
                }
            }
            sql.append(" ORDER BY books.id ASC;");


            System.out.println("SQL : ---" + sql);
            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql.toString());

            // Setting parameters for the prepared statement

            if (search != null) {
                System.out.println("Search Parameters:");
                int paramNumber = 0;
                if (search.getAuthor() != null && search.getAuthor().getId() != null) {
                    stmt.setInt(++paramNumber, search.getAuthor().getId());
                    System.out.println("Author ID: " + search.getAuthor().getId());
                }
                if (search.getAuthor() != null) {
                    // Ajout des paramètres pour prénom et nom de l'auteur (modification ici)
                    if (search.getAuthor().getFirstName() != null && !search.getAuthor().getFirstName().isEmpty()) {
                        stmt.setString(++paramNumber, "%" + search.getAuthor().getFirstName() + "%");
                    }
                    if (search.getAuthor().getLastName() != null && !search.getAuthor().getLastName().isEmpty()) {
                        stmt.setString(++paramNumber, "%" + search.getAuthor().getLastName() + "%");
                    }
                }
                if (search.getSubject() != null) {
                     if(search.getSubject().getId() != null) {
                         stmt.setInt(++paramNumber, search.getSubject().getId());
                         System.out.println("Subject ID: " + search.getSubject().getId());
                     }
                     if(search.getSubject().getName() != null && !search.getSubject().getName().isEmpty())
                         stmt.setString(++paramNumber, "%" + search.getSubject().getName() + "%");
                }
                if (search.getId() != null) {
                    stmt.setInt(++paramNumber, search.getId());
                    System.out.println("Book ID: " + search.getId());
                }
                if (search.getTitle() != null && !search.getTitle().isEmpty()) {
                    String decodedTitle = null;
                    try {
                        decodedTitle = URLDecoder.decode(search.getTitle(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    stmt.setString(++paramNumber, "%" + decodedTitle + "%");
                    System.out.println("Book Title: " + decodedTitle);
                }
                if (search.getIsbn() != null && !search.getIsbn().isEmpty()) {
                    stmt.setString(++paramNumber, "%" + search.getIsbn().trim().toLowerCase() + "%");
                }
                if (search.getMinPageCount() != null) {
                    stmt.setInt(++paramNumber, search.getMinPageCount());
                }
                if (search.getMaxPageCount() != null) {
                    stmt.setInt(++paramNumber, search.getMaxPageCount());
                }
                if (search.getMinPrice() != null) {
                    stmt.setFloat(++paramNumber, search.getMinPrice());
                }
                if (search.getMaxPrice() != null) {
                    stmt.setFloat(++paramNumber, search.getMaxPrice());
                }
                if (search.getMinStockQuantity() != null) {
                    stmt.setInt(++paramNumber, search.getMinStockQuantity());
                }
                if (search.getMinPublishYear() != null) {
                    stmt.setInt(++paramNumber, search.getMinPublishYear());
                }
                if (search.getMaxPublishYear() != null) {
                    stmt.setInt(++paramNumber, search.getMaxPublishYear());
                }
            }
            System.out.println("stmt : ------------" + stmt);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Result: ----------" + rs);

            // Processing the results
            System.out.println("111111111111111111111111111111111111111111111111111&");
            while (rs.next()) {
                System.out.println("2222222222222222222222222222222222222222");
                // Fetching the book details
                Integer id = rs.getInt("book_id");
                String title = rs.getString("books.title");
                String isbn = rs.getString("books.isbn");
                Integer pageCount = rs.getInt("books.page_count");
                Integer stockQuantity = rs.getInt("books.stock_quantity");
                Integer publishYear = rs.getInt("books.publish_year");
                float price = rs.getFloat("books.price");

                // Fetching the author details
                Author author = new Author();
                author.setId(rs.getInt("author_id"));
                author.setLastName(rs.getString("author_last_name"));
                author.setFirstName(rs.getString("author_first_name"));
                author.setBirthDate(String.valueOf(rs.getDate("author_birth_date").toLocalDate()));

                // Fetching the subject details
                Subject subject = new Subject();
                subject.setId(rs.getInt("subject_id"));
                subject.setName(rs.getString("subject_name"));

                // Creating and adding the book to the list
                Book book = new Book(id, author, subject, title, isbn, pageCount, stockQuantity, publishYear, price);
                System.out.println("Book -----------------" + book);
                books.add(book);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return books;
    }

    @Override
    public void update(Book book) {
        try {
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

        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
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
