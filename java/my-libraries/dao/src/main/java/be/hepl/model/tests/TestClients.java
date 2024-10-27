package be.hepl.model.tests;

import be.hepl.model.dao.ClientDAO;
import be.hepl.model.entity.Client;

public class TestClients {
    public static void main(String[] args) {
        ClientDAO dao = new ClientDAO();

        Client client = new Client();
        client.setId(null);
        client.setLastName("Vilvens");
        client.setFirstName("Claude");
        client.setNumClient(10);
        client.setAddress("Rue Peetermans 80, 4100 Seraing");

        dao.save(client);
        int id = client.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT BY ID ---");
        dao.load();
        Client client2 = dao.getById(id);
        System.out.println(client2);
    }
}
