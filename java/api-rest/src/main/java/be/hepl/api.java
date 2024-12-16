package be.hepl;

import be.hepl.model.dao.*;
import be.hepl.model.entity.*;
import be.hepl.model.viewmodel.*;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

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

        // Ajout du handler pour CORS sur toutes les routes
        HttpHandler corsHandler = new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Ajouter les en-têtes CORS
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");  // Autoriser toutes les origines
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");  // Méthodes autorisées
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");  // Entêtes autorisés

                // Si la requête est une OPTIONS (pré-vol), on répond immédiatement
                if ("OPTIONS".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(200, -1);
                    return;
                }

                // Sinon, on continue le traitement habituel
                exchange.sendResponseHeaders(200, 0);
            }
        };

        // Créer les contextes pour les différentes routes
        System.out.println("Mapping des chemins aux handlers avec DAO et mappers appropriés");
        server.createContext("/api/subjects", new handler<>(subjectDAO, Subject.class, SubjectSearchVM.class));
        server.createContext("/api/authors", new handler<>(authorDAO, Author.class, AuthorSearchVM.class));
        server.createContext("/api/books", new handler<>(bookDAO, Book.class, BookSearchVM.class));

        // Ajouter le handler CORS pour chaque contexte
        server.createContext("/api/subjects", corsHandler);
        server.createContext("/api/authors", corsHandler);
        server.createContext("/api/books", corsHandler);

        server.start();
        System.out.println("Serveur démarré sur le port 8080");
    }
}
