package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.*;
import be.hepl.model.viewmodel.CaddyItemSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaddyItemDAO implements CRUD<CaddyItem, CaddyItemSearchVM> {
    private ConnectDB connectDB;
    private ArrayList<CaddyItem> caddyItems;

    public CaddyItemDAO() {
        connectDB = new ConnectDB();
        caddyItems = new ArrayList<>();
    }

    @Override
    public ArrayList<CaddyItem> getList() {
        return caddyItems;
    }

    @Override
    public CaddyItem getById(int id) {
        for (CaddyItem caddyItem : caddyItems) {
            if (caddyItem.getId() == id) {
                return caddyItem;
            }
        }
        return null;
    }

    @Override
    public ArrayList<CaddyItem> read() {
        return this.read(null);
    }

    // language=sql
    @Override
    public ArrayList<CaddyItem> read(CaddyItemSearchVM cisvm) {
        try {
            String sql = "SELECT " +
                    "caddy_items.id AS caddy_item_id, " +
                    "caddy_items.caddy_id, " +
                    "caddy_items.book_id, " +
                    "caddy_items.quantity, " +
                    "caddies.id AS caddy_id, " +
                    "caddies.client_id, " +
                    "caddies.date, " +
                    "caddies.payed, " +
                    "clients.id AS client_id, " +
                    "clients.nom AS client_nom, " +
                    "clients.prenom AS client_prenom, " +
                    "clients.num_client, " +
                    "clients.adresse, " +
                    "books.id AS book_id, " +
                    "books.title AS book_title, " +
                    "books.isbn AS book_isbn, " +
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
                    "FROM caddy_items " +
                    "JOIN caddies ON caddy_items.caddy_id = caddies.id " +
                    "LEFT JOIN clients ON caddies.client_id = clients.id " +
                    "LEFT JOIN books ON caddy_items.book_id = books.id " +
                    "LEFT JOIN authors ON books.author_id = authors.id " +
                    "LEFT JOIN subjects ON books.subject_id = subjects.id ";

            if(cisvm != null) {
                String where = "WHERE 1=1 ";

                if(cisvm.getId() != null) {
                    where += "AND caddy_items.id = ? ";
                }

                if(cisvm.getCaddyId() != null) {
                    where += "AND caddy_items.caddy_id = ? ";
                }

                if(cisvm.getBookId() != null) {
                    where += "AND caddy_items.book_id = ? ";
                }

                sql += where + "ORDER BY caddy_items.id ASC ";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if(cisvm != null) {
                int paramNumber = 0;

                if(cisvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, cisvm.getId());
                }

                if(cisvm.getCaddyId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, cisvm.getCaddyId());
                }

                if(cisvm.getBookId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, cisvm.getBookId());
                }
            }

            ResultSet rs = stmt.executeQuery();     // donne un tableau à 2 dimensions

            caddyItems.clear();

            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setLastName(rs.getString("client_nom"));
                client.setFirstName(rs.getString("client_prenom"));
                client.setNumClient(rs.getInt("num_client"));
                client.setAddress(rs.getString("adresse"));

                Caddy caddy = new Caddy();
                caddy.setId(rs.getInt("caddy_id"));
                caddy.setClient(client);
                caddy.setDate(rs.getDate("date").toLocalDate());
                caddy.setPayed(rs.getBoolean("payed"));

                Author author = new Author();
                author.setId(rs.getInt("author_id"));
                author.setLastName(rs.getString("author_last_name"));
                author.setFirstName(rs.getString("author_first_name"));
                author.setBirthDate(rs.getDate("author_birth_date").toLocalDate().toString());

                Subject subject = new Subject();
                subject.setId(rs.getInt("subject_id"));
                subject.setName(rs.getString("subject_name"));

                Book book = new Book();
                book.setId(rs.getInt("book_id"));
                book.setAuthor(author);
                book.setSubject(subject);
                book.setTitle(rs.getString("book_title"));
                book.setIsbn(rs.getString("book_isbn"));
                book.setStockQuantity(rs.getInt("stock_quantity"));
                book.setPageCount(rs.getInt("page_count"));
                book.setPublishYear(rs.getInt("publish_year"));
                book.setPrice(rs.getFloat("price"));

                CaddyItem caddyItem = new CaddyItem();
                caddyItem.setId(rs.getInt("caddy_item_id"));
                caddyItem.setBook(book);
                caddyItem.setCaddy(caddy);
                caddyItem.setQuantity(rs.getInt("quantity"));
                // la quantité est calculée

                caddyItems.add(caddyItem);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaddyItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return caddyItems;
        }
    }

    @Override
    public void save(CaddyItem caddyItem) {
        if (caddyItem != null) {
            if (caddyItem.getId() != null) { // UPDATE
                update(caddyItem);
            } else { // CREATE
                create(caddyItem);
            }
        }
    }


    @Override
    public void create(CaddyItem caddyItem) {
        try {
            String sql;
            sql = "INSERT INTO caddy_items (caddy_id, book_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStmt.setInt(1, caddyItem.getCaddy().getId());
            pStmt.setInt(2, caddyItem.getBook().getId());
            pStmt.setInt(3, caddyItem.getQuantity());
            pStmt.executeUpdate();
            ResultSet rs = pStmt.getGeneratedKeys();

            if (rs.next()) {
                caddyItem.setId(rs.getInt(1)); // set the generated ID
            }
            rs.close();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaddyItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(CaddyItem caddyItem) {
        try {
            String sql;
            if (caddyItem.getId() != null) { // UPDATE
                sql = "UPDATE caddy_items SET " +
                        "caddy_id = ?, " +
                        "book_id = ?, " +
                        "quantity = ? " +
                        "WHERE id = ?";
                PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                pStmt.setInt(1, caddyItem.getCaddy().getId());
                pStmt.setInt(2, caddyItem.getBook().getId());
                pStmt.setInt(3, caddyItem.getQuantity());
                pStmt.setInt(4, caddyItem.getId());
                pStmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaddyItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(CaddyItem caddyItem) {
        if(caddyItem != null && caddyItem.getId() != null) {
            this.delete(caddyItem.getId());
        }
    }

    @Override
    public void delete(Integer id) {
        if(id != null) {
            try {
                String sql = "DELETE FROM caddy_items WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);;
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CaddyItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
