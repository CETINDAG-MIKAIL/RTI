package be.hepl.model.tests;

import be.hepl.model.dao.AuthorDAO;
import be.hepl.model.dao.SubjectDAO;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Caddy;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.AuthorSearchVM;

import java.util.ArrayList;

public class TestAuthors {
    public static void main(String[] args) {
        ArrayList<Author> authors;
        AuthorDAO dao = new AuthorDAO();

        Author author = new Author();
        author.setId(null);
        author.setFirstName("mikail");
        author.setLastName("Lebo");
        author.setBirthDate("1950-10-15");

        dao.save(author);
        System.out.println(author.getId());
        System.out.println(author.getFirstName());
        System.out.println(author.getLastName());
        System.out.println(author.getBirthDate());
        int id = author.getId();
        System.out.println("Ajout réussi : id = " + id);


        AuthorSearchVM vm = new AuthorSearchVM();

        authors = dao.read();
        System.out.println("\n--- SELECT * ---");
        for(Author c : authors) {
            System.out.println(c);
        }
        System.out.println("\n--- SELECT BY ID ---");
        dao.read();
        vm.setId(id);
        Author author1 = dao.getById(id);
        System.out.println(author1);


        System.out.println("\n--- SELECT BY firstname ---");
        vm.setFirstName("Luca");

        authors = dao.read(vm);
        for(Author c : authors) {
            System.out.println(c);
        }

        System.out.println("\n--- SELECT BY birth_day ---");
        vm = new AuthorSearchVM();
        vm.setBirthDate("2003-10-10");
        authors = dao.read(vm);

        for(Author c : authors) {
            System.out.println(c);
        }
        System.out.println("\nautheur avec id = " + id + " a été supprimé.");
        dao.delete(id);


        dao.read();
        System.out.println("\n--- SELECT * ---");
        for(Author c : authors) {
            System.out.println(c);
        }
    }
}
