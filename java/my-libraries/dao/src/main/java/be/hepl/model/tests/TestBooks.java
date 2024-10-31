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

        Author author = new Author();
        author.setId(15);
        author.setFirstName("mikail");
        author.setLastName("Lebo");
        author.setBirthDate("1950-10-15");

        int authorId = author.getId();
        System.out.println("authorID" + authorId);

        Subject subject = new Subject();
        subject.setId(1);

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

//        System.out.println("--- SELECT * ---");
//        books = dao.read();
//        for(Book b : books) {
//            System.out.println(b);
//        }
        BookSearchVM vm = new BookSearchVM();
        vm.setId(id);
        books = dao.read(vm);

        System.out.println("--- SELECT BY ID ---");
        for(Book b : books) {
            System.out.println(b);
        }
        vm.setId(null);

        System.out.println("--- SELECT maxPageCount---");
        vm.setId(null);
        vm.setMaxPageCount(300);
        books = dao.read(vm);

        for(Book b : books) {
            System.out.println(b);
       }
        System.out.println("--- SELECT minPageCount---");

        vm.setMaxPageCount(null);
        vm.setMinPageCount(30);
        books = dao.read(vm);

        for(Book b : books) {
            System.out.println(b);
        }

        System.out.println("--- SELECT maxPrice---");
        //vm.setMinPageCount(null);
        vm.setMaxPrice(100.05F);
        books = dao.read(vm);

        for(Book b : books) {
            System.out.println(b);
        }
        System.out.println("--- SELECT minPrice---");

        vm.setMaxPrice(null);
        vm.setMinPrice(30.4F);
        books = dao.read(vm);

        for(Book b : books) {
            System.out.println(b);
        }
        System.out.println("\n--- DELETE sur l'id ---");
        System.out.println("id : "+id);
        dao.delete(id);
//        books = dao.read();
//        System.out.println("\n--- SELECT * APRES DELETE ID ---");
//        for(Book b : books) {
//        System.out.println(b);
//        }
    }
}
