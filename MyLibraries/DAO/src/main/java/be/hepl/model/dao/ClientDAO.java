package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Client;
import be.hepl.model.viewmodel.ClientSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAO {
    private ConnectDB connectDB;
    private ArrayList<Client> clients;

    public ClientDAO() {
        connectDB = new ConnectDB();
        clients = new ArrayList<>();
    }

    public ArrayList<Client> getList() {
        return clients;
    }

    public Client getById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    public ArrayList<Client> load() {
        return this.load(null);
    }

    public ArrayList<Client> load(ClientSearchVM csvm) {
        try {
            String sql = "SELECT " +
                    "id, " +
                    "nom, " +
                    "prenom, " +
                    "numClient, " +
                    "adresse " +
                    "FROM clients ";

            if(csvm != null) {
                String where = "WHERE 1=1 ";

                if(csvm.getId() != null) {
                    where += "AND id = ? ";
                }

                if(csvm.getLastName() != null) {
                    where += "AND nom = ? ";
                }

                if(csvm.getFirstName() != null) {
                    where += "AND prenom = ? ";
                }

                if(csvm.getNumClient() != null) {
                    where += "AND numClient = ? ";
                }

                if(csvm.getContainAddress() != null) {
                    where += "AND adresse LIKE ? ";
                }

                sql += where + "ORDER BY id ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if(csvm != null) {
                int paramNumber = 0;

                if(csvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }

                if(csvm.getLastName() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getLastName());
                }

                if(csvm.getFirstName() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getFirstName());
                }

                if(csvm.getNumClient() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getNumClient());
                }

                if(csvm.getContainAddress() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getContainAddress());
                }
            }

            ResultSet rs = stmt.executeQuery();     // donne un tableau à 2 dimensions

            clients.clear();

            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setLastName(rs.getString("lastName"));
                client.setFirstName(rs.getString("firstName"));
                client.setNumClient(rs.getInt("numClient"));
                client.setAddress(rs.getString("address"));

                clients.add(client);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return clients;
        }
    }

    public void save(Client client) {
        try {
            String sql;
            if (client != null) {
                if (client.getId() != null) { // UPDATE
                    sql = "UPDATE clients SET " +
                            "nom = ?, " +
                            "prenom = ?, " +
                            "numClient = ?, " +
                            "adress = ? " +
                            "WHERE id = ?";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setString(1, client.getLastName());
                    pStmt.setString(2, client.getFirstName());
                    pStmt.setInt(3, client.getNumClient());
                    pStmt.setString(4, client.getAddress());
                    pStmt.setInt(5, client.getId());
                    pStmt.executeUpdate();
                    pStmt.close();
                } else { // CREATE
                    sql = "INSERT INTO clients (nom, prenom, numClient, adresse) VALUES (?, ?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setString(1, client.getLastName());
                    pStmt.setString(2, client.getFirstName());
                    pStmt.setInt(3, client.getNumClient());
                    pStmt.setString(4, client.getAddress());
                    pStmt.executeUpdate();
                    ResultSet rs = pStmt.getGeneratedKeys();
                    if (rs.next()) {
                        client.setId(rs.getInt(1));         // set the generated ID
                    }
                    rs.close();
                    pStmt.close();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Client client) {
        if(client != null && client.getId() != null) {
            this.delete(client.getId());
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
