package be.hepl;

import be.hepl.model.dao.*;
import be.hepl.model.entity.*;
import be.hepl.model.viewmodel.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class api {

    public static void main(String[] args) throws IOException {
        System.out.println("API Rest démarrée...");

        SubjectDAO subjectDAO = new SubjectDAO();
        AuthorDAO authorDAO = new AuthorDAO();
        BookDAO bookDAO = new BookDAO();


        System.out.println("Création du serveur HTTP");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);


        System.out.println("Mapping des chemins aux handlers avec DAO et mappers appropriés");
        server.createContext("/api/subjects", new handler<>(subjectDAO, Subject.class, SubjectSearchVM.class));
        server.createContext("/api/authors", new handler<>(authorDAO, Author.class, AuthorSearchVM.class));
        server.createContext("/api/books", new handler<>(bookDAO, Book.class, BookSearchVM.class));

        server.start();
        System.out.println("Serveur démarré sur le port 8080");
    }
}
