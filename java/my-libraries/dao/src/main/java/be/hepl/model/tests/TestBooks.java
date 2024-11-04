package be.hepl.model.tests;

import be.hepl.model.dao.*;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;

import java.util.ArrayList;

public class TestBooks {
    public static void main(String[] args) {
        ArrayList<Book> books = new ArrayList<>();
        BookDAO dao = new BookDAO();
      BookSearchVM vm = new BookSearchVM();
        Author author = new Author();
        author.setId(10);
        author.setFirstName("mikail");
        author.setLastName("Lebo");
        author.setBirthDate("1950-10-15");

        int authorId = author.getId();
        System.out.println("authorID" + authorId);

        Subject subject = new Subject();
        subject.setId(10);
        subject.setName("Science-Fiction");

        //subject [id=1, name=Science-Fiction]
        int subjectId = subject.getId();
        System.out.println("subjectID" + subjectId);


        Book book = new Book();
        book.setId(null);
        book.setAuthor(author);
        book.setSubject(subject);
        book.setTitle("Livre pour les nuls");
        book.setIsbn("10");
        book.setPageCount(100);
        book.setStockQuantity(20);
        book.setPrice(130F);
        book.setPublishYear(2003);

        dao.save(book);
        int id = book.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT * ---");
        books = dao.read();
        for(Book b : books) {
            System.out.println(b);
        }
        System.out.println();


        vm.setId(id);
        books = dao.read(vm);
        System.out.println("--- SELECT BY ID ---");
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setId(null);

        System.out.println();

        vm.setMaxPageCount(300);
        books = dao.read(vm);
        System.out.println("--- SELECT maxPageCount---" + vm.getMaxPageCount());
        for(Book b : books) {
            System.out.println(b);
       }
        vm.setMaxPageCount(null);


        vm.setMinPageCount(101);
        books = dao.read(vm);
        System.out.println("--- SELECT minPageCount---"+ vm.getMinPageCount());
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setMinPageCount(null);


        vm.setMaxPrice(100.05F);
        books = dao.read(vm);
        System.out.println("--- SELECT maxPrice---" + vm.getMaxPrice());
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setMaxPrice(null);

        System.out.println();

        vm.setMinPrice(30.4F);
        books = dao.read(vm);
        System.out.println("--- SELECT minPrice---" + vm.getMinPrice());
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setMinPrice(null);

        System.out.println();

        vm.setTitle("Livre pour les nuls");
        books = dao.read(vm);
        System.out.println("--- SELECT Title---" + vm.getTitle());
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setTitle(null);

        System.out.println();
        vm.setAuthor(author);
        books = dao.read(vm);
        System.out.println("--- SELECT author---" + vm.getAuthor());
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setAuthor(null);

        System.out.println();

        vm.setSubject(subject);
        books = dao.read(vm);
        System.out.println("--- SELECT subject---" + vm.getSubject());
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setSubject(null);

        System.out.println();
        System.out.println("\n--- DELETE sur l'id ---");
        System.out.println("id : "+id);
        dao.delete(id);
        books = dao.read();
        System.out.println("\n--- SELECT * APRES DELETE ID ---");
        System.out.println();
        for(Book b : books) {
        System.out.println(b);
        }
        System.out.println();
    }
}
