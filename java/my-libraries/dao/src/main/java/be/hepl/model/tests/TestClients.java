package be.hepl.model.tests;

import be.hepl.model.dao.ClientDAO;
import be.hepl.model.entity.Client;
import be.hepl.model.viewmodel.ClientSearchVM;

import java.util.ArrayList;

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

        System.out.println("\n--- Tous les clients ---");
        ArrayList<Client> clients = dao.load();
        for(Client c : clients) {
            System.out.println(c);
        }

        System.out.println("\n--- SELECT BY ID ---");
        dao.load();
        Client client2 = dao.getById(id);
        System.out.println(client2);

        System.out.println("\n--- SELECT sur le prénom ---");
        ClientSearchVM vm = new ClientSearchVM();
        vm.setFirstName("Claude");
        clients = dao.load(vm);
        for(Client c : clients) {
            System.out.println(c);
        }

        System.out.println("\n--- SELECT sur l'adresse contenant 'seraing' ---");
        vm.setFirstName(null);
        vm.setContainAddress("seraing");
        clients = dao.load(vm);
        for(Client c : clients) {
            System.out.println(c);
        }

        System.out.println("\n--- DELETE sur l'id ---");
        dao.delete(id);
        System.out.println("Client avec id = " + id + " a été supprimé.");

        System.out.println("\n--- Tous les clients ---");
        clients = dao.load();
        for(Client c : clients) {
            System.out.println(c);
        }
    }
}
