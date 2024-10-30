package be.hepl.model.tests;

import be.hepl.model.dao.CaddyDAO;
import be.hepl.model.dao.ClientDAO;
import be.hepl.model.entity.Caddy;
import be.hepl.model.entity.Client;
import be.hepl.model.viewmodel.CaddySearchVM;
import be.hepl.model.viewmodel.ClientSearchVM;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestCaddies {
    public static void main(String[] args) {
        CaddyDAO dao = new CaddyDAO();

        Client client = new Client();
        client.setId(1);
        client.setLastName("Boss");
        client.setFirstName("Hugo");
        client.setNumClient(10);
        client.setAddress("Rue de Waremme, 4000 Liège");

        Caddy caddy = new Caddy();
        caddy.setId(null);
        caddy.setClient(client);
        caddy.setDate(LocalDate.of(2000,10,10));
        // caddy.setAmount();       // c'est calculé
        caddy.setPayed(false);


        dao.save(caddy);
        int id = caddy.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("\n--- Tous les caddies ---");
        ArrayList<Caddy> caddies = dao.load();
        for(Caddy c : caddies) {
            System.out.println(c);
        }

        System.out.println("--- SELECT BY ID ---");
        dao.load();
        Caddy caddy2 = dao.getById(id);
        System.out.println(caddy2);

        System.out.println("\n--- SELECT sur account > 10 ---");
        CaddySearchVM vm = new CaddySearchVM();
        vm.setMinAmount(10);
        caddies = dao.load(vm);
        for(Caddy c : caddies) {
            System.out.println(c);
        }

        System.out.println("\n--- SELECT sur account < 50 ---");
        vm.setMinAmount(null);
        vm.setMaxAmount(50);
        caddies = dao.load(vm);
        for(Caddy c : caddies) {
            System.out.println(c);
        }

        System.out.println("\n--- DELETE sur l'id ---");
        dao.delete(id);
        System.out.println("Caddy avec id = " + id + " a été supprimé.");

        System.out.println("\n--- Tous les caddies ---");
        caddies = dao.load();
        for(Caddy c : caddies) {
            System.out.println(c);
        }
    }
}
