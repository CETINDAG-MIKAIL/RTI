package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Client;
import be.hepl.model.viewmodel.AuthorSearchVM;
import be.hepl.model.viewmodel.ClientSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAO implements CRUD<Client, ClientSearchVM> {
    private ConnectDB connectDB;
    private ArrayList<Client> clients;

    public ClientDAO() {
        connectDB = new ConnectDB();
        clients = new ArrayList<>();
    }

    @Override
    public ArrayList<Client> getList() {
        return clients;
    }

    @Override
    public Client getById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Client> read() {
        return this.read(null);
    }

    // language=sql
    @Override
    public ArrayList<Client> read(ClientSearchVM csvm) {
        try {
            String sql = "SELECT " +
                    "id, " +
                    "nom, " +
                    "prenom, " +
                    "num_client, " +
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
                    where += "AND num_client = ? ";
                }

                if(csvm.getContainAddress() != null) {
                    where += "AND UPPER(adresse) LIKE UPPER(?) ";
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
                    stmt.setString(paramNumber, "%" + csvm.getContainAddress() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();     // donne un tableau à 2 dimensions

            clients.clear();

            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setLastName(rs.getString("nom"));
                client.setFirstName(rs.getString("prenom"));
                client.setNumClient(rs.getInt("num_client"));
                client.setAddress(rs.getString("adresse"));

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

    @Override
    public void save(Client client) {
        try {
            if (client != null) {
                if (client.getId() != null) { // UPDATE
                    update(client);
                } else { // CREATE
                    create(client);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void create(Client client) throws SQLException {
        String sql;
        sql = "INSERT INTO clients (nom, prenom, num_client, adresse) VALUES (?, ?, ?, ?)";
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

    @Override
    public void update(Client client) throws SQLException {
        String sql;
        sql = "UPDATE clients SET " +
                "nom = ?, " +
                "prenom = ?, " +
                "num_client = ?, " +
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
    }

    @Override
    public void delete(Client client) {
        if(client != null && client.getId() != null) {
            this.delete(client.getId());
        }
    }

    @Override
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
