package be.hepl.model.tests;

import be.hepl.model.dao.CaddyItemDAO;
import be.hepl.model.entity.*;

import java.time.LocalDate;

public class TestCaddyItems {
    public static void main(String[] args) {
        CaddyItemDAO dao = new CaddyItemDAO();

        /*
        -- Insérer un client
        INSERT INTO clients (nom, prenom, num_client, adresse) VALUES ('Pouratte', 'Hugues', 10, 'Rue Deschamps, 4000 Liège');
        -- Insérer un Caddy
        INSERT INTO caddies (client_id, date, payed) VALUES (LAST_INSERT_ID(), '2024-10-10', FALSE);
        -- Insérer un Author
        INSERT INTO authors (last_name, first_name, birth_date) VALUES ('Cetindagues', 'Michel', '2003-01-01');
        -- Insérer un Subject
        INSERT INTO subjects (name) VALUES ('Informatique');
        -- Insérer un Book
        INSERT INTO books (author_id, subject_id, title, isbn, page_count, stock_quantity, publish_year, price) VALUES (9, LAST_INSERT_ID(), 'Livre les génies', '9910154', 155, 8000, 2015, 19);
        */

        Client client = new Client();
        client.setId(3);
        client.setLastName("Pouratte");
        client.setFirstName("Hugues");
        client.setNumClient(10);
        client.setAddress("Rue Deschamps, 4000 Liège");

        // Créer un Caddy
        Caddy caddy = new Caddy();
        caddy.setId(37);
        caddy.setClient(client);
        caddy.setDate(LocalDate.of(2024, 10, 10));
        caddy.setPayed(false);

        // Créer un Author
        Author author = new Author();
        author.setId(9);
        author.setFirstName("Michel");
        author.setLastName("Cetindagues");
        author.setBirthDate("2003-01-01");

        // Créer un Subject
        Subject subject = new Subject();
        subject.setId(4);
        subject.setName("Informatique");

        // Créer un Book
        Book book = new Book();
        book.setId(14);
        book.setAuthor(author);
        book.setSubject(subject);
        book.setTitle("Livre les génies");
        book.setIsbn("9910154");
        book.setPageCount(155);
        book.setStockQuantity(8000);
        book.setPrice(19);
        book.setPublishYear(2015);

        // Créer un CaddyItem
        CaddyItem caddyItem = new CaddyItem();
        caddyItem.setId(null);
        caddyItem.setCaddy(caddy);
        caddyItem.setBook(book);
        caddyItem.setQuantity(100); // Quantité à ajouter

        dao.save(caddyItem);

        System.out.println("Ajout réussi : id = " + caddyItem.getId());


        int id = caddyItem.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT BY ID ---");
        dao.load();


        CaddyItem caddyItem2 = dao.getById(id);
        System.out.println(caddyItem2);
    }
}
