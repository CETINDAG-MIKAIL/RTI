package be.hepl.model.tests;

import be.hepl.model.dao.CaddyDAO;
import be.hepl.model.dao.ClientDAO;
import be.hepl.model.entity.Caddy;
import be.hepl.model.entity.Client;

import java.time.LocalDate;

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

        System.out.println("--- SELECT BY ID ---");
        dao.load();

        // TESTTTTT ça ne fonctionne toujours pas, après remettre dao.caddies en private
        System.out.println("Affichage des caddy :");
        for(Caddy caddyyy : dao.caddies) {
            System.out.println(caddyyy);
        }
        System.out.println("--------------");
        // ------------------------------------------------------------------------------


        Caddy caddy2 = dao.getById(id);
        System.out.println(caddy2);
    }
}
