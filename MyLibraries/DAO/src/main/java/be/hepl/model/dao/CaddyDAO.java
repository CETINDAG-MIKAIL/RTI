package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Caddy;
import be.hepl.model.entity.Client;
import be.hepl.model.viewmodel.CaddySearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaddyDAO {
    private ConnectDB connectDB;
    private ArrayList<Caddy> caddies;

    public CaddyDAO() {
        connectDB = new ConnectDB();
        caddies = new ArrayList<>();
    }

    public ArrayList<Caddy> getList() {
        return caddies;
    }

    public Caddy getById(int id) {
        for (Caddy caddy : caddies) {
            if (caddy.getId() == id) {
                return caddy;
            }
        }
        return null;
    }

    public ArrayList<Caddy> load() {
        return this.load(null);
    }

    // language=sql
    public ArrayList<Caddy> load(CaddySearchVM csvm) {
        try {
            String sql = "SELECT " +
                    "id, " +
                    "clientId, " +
                    "date, " +
                    "SUM(b.price * ci.quantity) AS amount, "
                    "payed " +
                    "FROM caddies "
                    "JOIN clients ON caddies.clientId = clients.id "
                    "JOIN caddy_items ON caddies.id = caddy_items.caddyId ";

            if(csvm != null) {
                String where = "WHERE 1=1 ";

                if(csvm.getId() != null) {
                    where += "AND id = ? ";
                }

                if(csvm.getClientId() != null) {
                    where += "AND clientId = ? ";
                }

                if(csvm.getDate() != null) {
                    where += "AND date = ? ";
                }

                if(csvm.getPayed() != null) {
                    where += "AND payed LIKE ? ";
                }

                sql += where + "ORDER BY id ASC ";

                if(csvm.getMinAmount() != null) {
                    sql += "HAVING amount > ? ";
                } else if(csvm.getMaxAmount() != null) {
                    sql += "HAVING amount < ? ";
                }
            }

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
            }

            ResultSet rs = stmt.executeQuery();     // donne un tableau à 2 dimensions

            caddies.clear();

            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setLastName(rs.getString("lastName"));
                client.setFirstName(rs.getString("firstName"));
                client.setNumClient(rs.getInt("numClient"));
                client.setAddress(rs.getString("address"));

                Caddy caddy = new Caddy();
                caddy.setId(rs.getInt("id"));
                caddy.setClient(client);
                caddy.setDate(rs.getDate("date").toLocalDate());
                //caddy.setAmount(rs.getInt());     // -> à calculer ????
                caddy.setPayed(rs.getBoolean("payed"));

                caddies.add(caddy);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return caddies;
        }
    }

    private Integer id;
    private Client client;
    private LocalDate date;
    private Integer amount;             // ce sera calculé, donc pas stocké dans la BD
    private Boolean payed;

    public void save(Caddy caddy) {
        try {
            String sql;
            if (caddy != null) {
                if (caddy.getId() != null) { // UPDATE
                    sql = "UPDATE clients SET " +
                            "clientId = ?, " +
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
                } else { // CREATE
                    sql = "INSERT INTO clients (clientId, date, payed) VALUES (?, ?, ?)";
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
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Caddy caddy) {
        if(caddy != null && caddy.getId() != null) {
            this.delete(caddy.getId());
        }
    }

    public void delete(Integer id) {
        if(id != null) {
            try {
                String sql = "DELETE FROM clients WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);;
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
