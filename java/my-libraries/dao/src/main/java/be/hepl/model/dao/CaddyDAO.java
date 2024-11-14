package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Caddy;
import be.hepl.model.entity.Client;
import be.hepl.model.viewmodel.AuthorSearchVM;
import be.hepl.model.viewmodel.CaddySearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaddyDAO implements CRUD<Caddy, CaddySearchVM> {
    private ConnectDB connectDB;
    private ArrayList<Caddy> caddies;

    public CaddyDAO() {
        connectDB = ConnectDB.getInstance();
        caddies = new ArrayList<>();
    }

    @Override
    public ArrayList<Caddy> getList() {
        return caddies;
    }

    @Override
    public Caddy getById(Integer id) {
        for (Caddy caddy : caddies) {
            if (caddy.getId() == id) {
                return caddy;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Caddy> read() {
        return this.read(null);
    }

    // language=sql
    @Override
    public ArrayList<Caddy> read(CaddySearchVM csvm) {
        try {
            String sql = "SELECT " +
                    "caddies.id AS caddy_id, " +
                    "clients.id AS client_id, " +
                    "clients.nom, " +
                    "clients.prenom, " +
                    "clients.num_client, " +
                    "clients.adresse, " +
                    "caddies.date, " +
                    "COALESCE(SUM(books.price * caddy_items.quantity), 0) AS total_amount, " +
                    "caddies.payed " +
                    "FROM caddies " +
                    "JOIN clients ON caddies.client_id = clients.id " +
                    "LEFT JOIN caddy_items ON caddies.id = caddy_items.caddy_id " +
                    "LEFT JOIN books ON caddy_items.book_id = books.id ";

            if(csvm != null) {
                String where = "WHERE 1=1 ";

                if(csvm.getId() != null) {
                    where += "AND caddies.id = ? ";
                }

                if(csvm.getClientId() != null) {
                    where += "AND clients.id = ? ";
                }

                if(csvm.getDate() != null) {
                    where += "AND caddies.date = ? ";
                }

                if(csvm.getPayed() != null) {
                    where += "AND caddies.payed = ? ";
                }

                sql += where;
            }

            // Ajout de la clause GROUP BY après WHERE
            sql += " GROUP BY caddies.id, clients.id ";

            // Ajout de la clause HAVING pour le filtre total_amount
            if (csvm != null) {
                if (csvm.getMinAmount() != null) {
                    sql += "HAVING total_amount > ? ";
                }
                if (csvm.getMaxAmount() != null) {
                    sql += "HAVING total_amount < ? ";
                }
            }

            sql += " ORDER BY caddies.id ASC";

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if(csvm != null) {
                int paramNumber = 0;

                if(csvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }

                if(csvm.getClientId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getClientId());
                }

                if(csvm.getDate() != null) {
                    paramNumber++;
                    stmt.setDate(paramNumber, Date.valueOf(csvm.getDate()));
                }

                if(csvm.getPayed() != null) {
                    paramNumber++;
                    stmt.setBoolean(paramNumber, csvm.getPayed());
                }

                if(csvm.getMinAmount() != null) {
                    paramNumber++;
                    stmt.setDouble(paramNumber, csvm.getMinAmount());
                }

                if(csvm.getMaxAmount() != null) {
                    paramNumber++;
                    stmt.setDouble(paramNumber, csvm.getMaxAmount());
                }
            }

            ResultSet rs = stmt.executeQuery();     // donne un tableau à 2 dimensions

            caddies.clear();

            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setLastName(rs.getString("clients.nom"));
                client.setFirstName(rs.getString("clients.prenom"));
                client.setNumClient(rs.getInt("clients.num_client"));
                client.setAddress(rs.getString("clients.adresse"));

                Caddy caddy = new Caddy();
                caddy.setId(rs.getInt("caddy_id"));
                caddy.setClient(client);
                caddy.setDate(rs.getDate("caddies.date").toLocalDate());
                caddy.setPayed(rs.getBoolean("caddies.payed"));
                caddy.setAmount(rs.getDouble("total_amount"));

                caddies.add(caddy);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return caddies;
        }
    }

    @Override
    public void save(Caddy caddy) {
        if (caddy != null) {
            if (caddy.getId() != null) { // UPDATE
                update(caddy);
            } else { // CREATE
                create(caddy);
            }
        }
    }

    @Override
    public void update(Caddy caddy) {
        try {
            String sql;
            sql = "UPDATE caddies SET " +
                    "client_id = ?, " +
                    "date = ?, " +
                    "payed = ? " +
                    "WHERE id = ?";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
            pStmt.setInt(1, caddy.getClient().getId());
            pStmt.setDate(2, Date.valueOf(caddy.getDate()));
            pStmt.setBoolean(3, caddy.getPayed());
            pStmt.setInt(4, caddy.getId());
            pStmt.executeUpdate();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void create(Caddy caddy) {
        try {
            String sql;
            sql = "INSERT INTO caddies (client_id, date, payed) VALUES (?, ?, ?)";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStmt.setInt(1, caddy.getClient().getId());
            pStmt.setDate(2, Date.valueOf(caddy.getDate()));
            pStmt.setBoolean(3, caddy.getPayed());
            pStmt.executeUpdate();
            ResultSet rs = pStmt.getGeneratedKeys();
            if (rs.next()) {
                caddy.setId(rs.getInt(1));         // set the generated ID
            }
            rs.close();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Caddy caddy) {
        if(caddy != null && caddy.getId() != null) {
            this.delete(caddy.getId());
        }
    }

    @Override
    public void delete(Integer id) {
        if(id != null) {
            try {
//                String sql = "DELETE FROM caddy_items WHERE caddy_id = ?";
//                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
//                stmt.setInt(1, id);;
//                stmt.executeUpdate();
//                stmt.close();

                String sql = "DELETE FROM caddies WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);;
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
