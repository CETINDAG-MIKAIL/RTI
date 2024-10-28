package be.hepl.model.tests;

import be.hepl.model.dao.*;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;

public class TestBooks {
    public static void main(String[] args) {
        BookDAO dao = new BookDAO();
        AuthorDAO authorDAO = new AuthorDAO();
        SubjectDAO subjectDAO = new SubjectDAO();

        Author author = new Author();
        author.setFirstName("mikail");
        author.setLastName("Lebo");
        author.setBirthDate("10-15-1950");
        authorDAO.save(author);
        int authorId = author.getId();
        System.out.println(authorId);

        Subject subject = new Subject();

        subject.setName("Informatique");
        subjectDAO.save(subject);
        int subjectId = subject.getId();
        System.out.println(subjectId);


        Book book = new Book();
        book.setId(null);
        book.setAuthor(author);
        book.setSubject(subject);
        book.setTitle("Livre pour les nuls");
        book.setIsbn("10");
        book.setPageCount(100);
        book.setStockQuantity(20);
        book.setPrice(130);
        book.setPublishYear(2003);

        dao.save(book);
        int id = book.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT BY ID ---");
        dao.load();
        Book book1 = dao.getById(id);
        System.out.println(book1);
    }
}
