package be.hepl.model.tests;

import be.hepl.model.dao.CaddyDAO;
import be.hepl.model.dao.CaddyItemDAO;
import be.hepl.model.entity.*;

import java.time.LocalDate;

public class TestCaddyItems {
    public static void main(String[] args) {
        CaddyItemDAO dao = new CaddyItemDAO();

        Client client = new Client();
        client.setId(1);
        client.setLastName("Pouratte");
        client.setFirstName("Hugues");
        client.setNumClient(10);
        client.setAddress("Rue Deschamps, 4000 Liège");

        Caddy caddy = new Caddy();
        caddy.setId(2);
        caddy.setClient(client);
        caddy.setDate(LocalDate.of(2024,10,10));
        caddy.setPayed(false);


        Author author = new Author();
        author.setId(null);
        author.setFirstName("Michel");
        author.setLastName("Cetindagues");
        author.setBirthDate("2003-01-01");

        Subject subject = new Subject();
        subject.setId(null);
        subject.setName("Informatique");

        Book book = new Book();
        book.setId(3);
        book.setAuthor(author);
        book.setSubject(subject);
        book.setTitle("Livre les génies");
        book.setIsbn("9910154");
        book.setPageCount(155);
        book.setStockQuantity(8000);
        book.setPrice(19);
        book.setPublishYear(2015);


        CaddyItem caddyItem = new CaddyItem();
        caddyItem.setId(null);
        caddyItem.setCaddy(caddy);
        caddyItem.setBook(book);
        caddyItem.setQuantity(100);



        dao.save(caddyItem);
        int id = caddyItem.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT BY ID ---");
        dao.load();


        CaddyItem caddyItem2 = dao.getById(id);
        System.out.println(caddyItem2);
    }
}
