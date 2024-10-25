package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.*;
import be.hepl.model.viewmodel.CaddyItemSearchVM;
import be.hepl.model.viewmodel.CaddySearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaddyItemDAO {
    private ConnectDB connectDB;
    private ArrayList<CaddyItem> caddyItems;

    public CaddyItemDAO() {
        connectDB = new ConnectDB();
        caddyItems = new ArrayList<>();
    }

    public ArrayList<CaddyItem> getList() {
        return caddyItems;
    }

    public CaddyItem getById(int id) {
        for (CaddyItem caddyItem : caddyItems) {
            if (caddyItem.getId() == id) {
                return caddyItem;
            }
        }
        return null;
    }

    public ArrayList<CaddyItem> load() {
        return this.load(null);
    }

    // language=sql
    public ArrayList<CaddyItem> load(CaddyItemSearchVM cisvm) {
        try {
            String sql = "SELECT " +
                    "id, " +
                    "caddyId, " +
                    "bookId, " +
                    "quantity " +
                    "FROM caddy_items " +
                    "JOIN caddies ON caddy_items.caddyId = caddies.id " +
                    "JOIN clients ON caddies.clientId = clients.id " +
                    "JOIN books ON caddy_items.bookId = books.id " +
                    "JOIN authors ON books.authorId = authors.id " +
                    "JOIN subjects ON books.subjectId = subjects.id ";

            if(cisvm != null) {
                String where = "WHERE 1=1 ";

                if(cisvm.getId() != null) {
                    where += "AND id = ? ";
                }

                if(cisvm.getCaddyId() != null) {
                    where += "AND caddyId = ? ";
                }

                if(cisvm.getBookId() != null) {
                    where += "AND bookId = ? ";
                }

                sql += where + "ORDER BY id ASC ";
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
                client.setId(rs.getInt("clients.id"));
                client.setLastName(rs.getString("clients.lastName"));
                client.setFirstName(rs.getString("clients.firstName"));
                client.setNumClient(rs.getInt("clients.numClient"));
                client.setAddress(rs.getString("clients.address"));

                Caddy caddy = new Caddy();
                caddy.setId(rs.getInt("caddies.id"));
                caddy.setClient(client);
                caddy.setDate(rs.getDate("caddies.date").toLocalDate());
                //caddy.setAmount(rs.getInt());     // -> à calculer ????
                caddy.setPayed(rs.getBoolean("caddies.payed"));


                Author author = new Author();
                author.setId(rs.getInt("caddies.id"));
                author.setLastName(rs.getString("caddies.lastName"));
                author.setFirstName(rs.getString("caddies.lastName"));
                author.setBirthDate(rs.getDate("caddies.birthDate").toLocalDate());

                Subject subject = new Subject();
                subject.setName(rs.getString("subjects.name"));

                Book book = new Book();
                book.setAuthor(author);
                book.setSubject(subject);
                book.setTitle(rs.getString("books.title"));
                book.setStock_quantity(rs.getInt("books.stock_quantity"));
                book.setPublish_year(rs.getInt("books.publish_year"));
                book.setPrice(rs.getFloat("books.price"));

                CaddyItem caddyItem = new CaddyItem();
                caddyItem.setId(rs.getInt("caddyItem.id"));
                caddyItem.setBook(book);
                caddyItem.setCaddy(caddy);
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



    public void save(CaddyItem caddyItem) {
        try {
            String sql;
            if (caddyItem != null) {
                if (caddyItem.getId() != null) { // UPDATE
                    sql = "UPDATE caddy_items SET " +
                            "caddyId = ?, " +
                            "bookId = ?, " +
                            "quantity = ? " +
                            "WHERE id = ?";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setInt(1, caddyItem.getCaddy().getId());
                    pStmt.setInt(2, caddyItem.getBook().getId());
                    pStmt.setInt(3, caddyItem.getQuantity());
                    pStmt.setInt(4, caddyItem.getId());
                    pStmt.executeUpdate();
                    pStmt.close();
                } else { // CREATE
                    sql = "INSERT INTO caddy_items (caddyId, bookId, quantity) VALUES (?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setInt(1, caddyItem.getCaddy().getId());
                    pStmt.setInt(2, caddyItem.getBook().getId());
                    pStmt.setInt(3, caddyItem.getQuantity());
                    pStmt.executeUpdate();
                    ResultSet rs = pStmt.getGeneratedKeys();
                    if (rs.next()) {
                        caddyItem.setId(rs.getInt(1));         // set the generated ID
                    }
                    rs.close();
                    pStmt.close();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CaddyItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(CaddyItem caddyItem) {
        if(caddyItem != null && caddyItem.getId() != null) {
            this.delete(caddyItem.getId());
        }
    }

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
