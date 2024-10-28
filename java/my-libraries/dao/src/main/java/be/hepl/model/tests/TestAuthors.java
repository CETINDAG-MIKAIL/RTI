package be.hepl.model.tests;

import be.hepl.model.dao.AuthorDAO;
import be.hepl.model.dao.SubjectDAO;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Subject;

public class TestAuthors {
    public static void main(String[] args) {
        AuthorDAO dao = new AuthorDAO();

        Author author = new Author();
        author.setId(null);
        author.setFirstName("Luca");
        author.setLastName("Geelen");
        author.setBirthDate("10-10-2003");

        dao.save(author);
        int id = author.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT BY ID ---");
        dao.load();
        Author author1 = dao.getById(id);
        System.out.println(author1);
    }
}
