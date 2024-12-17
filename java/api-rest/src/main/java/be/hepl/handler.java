package be.hepl;

import be.hepl.helpers.HttpUtils;
import be.hepl.helpers.JsonUtils;
import be.hepl.helpers.ResponseHelper;
import be.hepl.model.dao.CRUD;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class handler<T, S> implements HttpHandler {
    private final CRUD<T, S> dao;
    private final Class<T> entityClass; // Pour convertir JSON en entité
    private final Class<S> searchClass; // Pour construire l'objet de recherche

    public handler(CRUD<T, S> dao, Class<T> entityClass, Class<S> searchClass) {
        this.dao = dao;
        this.entityClass = entityClass;
        this.searchClass = searchClass;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Ajouter les en-têtes CORS pour autoriser les requêtes depuis n'importe quelle origine
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");  // Autorise toutes les origines
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");  // Autorise ces méthodes HTTP
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");  // Autorise l'en-tête Content-Type
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");  // Permet les cookies (si nécessaire)

        // Si la requête est une demande de pré-vol (OPTIONS), envoyer une réponse vide
        if ("OPTIONS".equals(method)) {
            exchange.sendResponseHeaders(200, -1);  // Répondre avec un code 200 pour OPTIONS
            return;
        }

        try {
            System.out.println("handle switch: " + method);
            System.out.println(exchange);
            System.out.println(exchange.getRequestURI());
            switch (method) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "PUT" -> handlePut(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> ResponseHelper.sendErrorResponse(exchange, 405, "Méthode non autorisée");
            }
        } catch (IllegalArgumentException e) {
            ResponseHelper.sendErrorResponse(exchange, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ResponseHelper.sendErrorResponse(exchange, 500, "Erreur interne du serveur");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        System.out.println("handle get");
        String query = exchange.getRequestURI().getQuery();
        System.out.println("query: " + query);
        S search = parseSearchParams(query); // Conversion query string -> SearchVM
        List<T> myList = dao.read(search); // Dao attend un SubjectSearchVM
        JsonArray jsonArray = new JsonArray(); // Créer un tableau JSON

        // Pour chaque objet dans la liste, convertissez-le en JsonObject et ajoutez-le au JsonArray
        for (T item : myList) {
            JsonObject jsonObject = JsonUtils.convertToJsonObject(item); // Convertissez chaque item en JsonObject
            jsonArray.add(jsonObject); // Ajoutez l'objet JSON au tableau
        }
        ResponseHelper.sendSuccessResponse(exchange, jsonArray.toString()); // Envoi de la réponse
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        System.out.println("handle post");
        String requestBody = HttpUtils.readRequestBody(exchange);
        System.out.println("requestBody: " + requestBody);
        T entity = JsonUtils.fromJson(requestBody, entityClass);
        System.out.println("entity: " + entity);
        if (entity == null) throw new IllegalArgumentException("Données JSON invalides");
        dao.create(entity);
        ResponseHelper.sendSuccessResponse(exchange, "Entité créée avec succès");
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String requestBody = HttpUtils.readRequestBody(exchange);

        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);

        Integer id = null;
        if (queryParams.containsKey("id")) {
            Object idValue = queryParams.get("id");
            if (idValue instanceof String) {
                try {
                    id = Integer.parseInt((String) idValue); // Casting explicite vers String
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ID invalide : " + idValue);
                }
            } else {
                throw new IllegalArgumentException("ID doit être une chaîne de caractères");
            }
        }

        if (id == null) throw new IllegalArgumentException("ID manquant dans les paramètres de requête");

        T existingEntity = dao.getById(id);
        if (existingEntity == null) throw new IllegalArgumentException("Entité non trouvée pour l'ID : " + id);

        T updatedEntity = JsonUtils.fromJson(requestBody, entityClass);
        if (updatedEntity == null) throw new IllegalArgumentException("Données JSON invalides");

        dao.update(updatedEntity);

        ResponseHelper.sendSuccessResponse(exchange, "Entité mise à jour avec succès");
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);

        Integer id = null;
        if (queryParams.containsKey("id")) {
            Object idValue = queryParams.get("id");
            if (idValue instanceof String) {
                try {
                    id = Integer.parseInt((String) idValue); // Casting explicite vers String
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ID invalide : " + idValue);
                }
            } else {
                throw new IllegalArgumentException("ID doit être une chaîne de caractères");
            }
        }

        if (id == null) throw new IllegalArgumentException("ID manquant dans les paramètres de requête");

        // Suppression de l'entité
        dao.delete(id);
        ResponseHelper.sendSuccessResponse(exchange, "Entité supprimée avec succès");
    }

    private <T> String convertToJsonList(List<T> myList) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < myList.size(); i++) {
            T myEntity = myList.get(i);

            if (myEntity instanceof Subject) {
                Subject subject = (Subject) myEntity;
                json.append("{\"id\": ").append(subject.getId())
                        .append(", \"name\": \"").append(cleanString(subject.getName())).append("\"}");
            } else if (myEntity instanceof Author) {
                Author author = (Author) myEntity;
                json.append("{\"id\": ").append(author.getId())
                        .append(", \"Firstname\": \"").append(cleanString(author.getFirstName())).append("\"")
                        .append(", \"Lastname\": \"").append(cleanString(author.getLastName())).append("\"")
                        .append(", \"BirthDate\": \"").append(cleanString(author.getBirthDate())).append("\"}");
            } else if (myEntity instanceof Book) {
                Book book = (Book) myEntity;
                json.append("{\"id\": ").append(cleanString(String.valueOf(book.getId())))
                        .append(", \"isbn\": \"").append(cleanString(book.getIsbn())).append("\"")
                        .append(", \"price\": \"").append(cleanString(String.valueOf(book.getPrice()))).append("\"")
                        .append(", \"stockQuantity\": \"").append(cleanString(String.valueOf(book.getStockQuantity()))).append("\"")
                        .append(", \"publishYear\": \"").append(cleanString(String.valueOf(book.getPublishYear()))).append("\"")
                        .append(", \"minPageCount\": \"").append(cleanString(String.valueOf(book.getPageCount()))).append("\"")
                        .append(", \"maxPageCount\": \"").append(cleanString(String.valueOf(book.getPageCount()))).append("\"")
                        .append(", \"title\": \"").append(cleanString(book.getTitle())).append("\"")
                        // Object author
                        .append(", \"author\": {")
                        .append("\"id\": ").append(book.getAuthor().getId()).append(", ")
                        .append("\"firstName\": \"").append(cleanString(book.getAuthor().getFirstName())).append("\", ")
                        .append("\"lastName\": \"").append(cleanString(book.getAuthor().getLastName())).append("\"}")
                        // Object subject
                        .append(", \"subject\": {")
                        .append("\"id\": ").append(book.getSubject().getId()).append(", ")
                        .append("\"name\": \"").append(cleanString(book.getSubject().getName())).append("\"}")
                        .append("}");
            }

            // Séparateur de virgule entre les objets
            if (i < myList.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }


    // Méthode pour nettoyer les chaînes (trim et vérification des nulls)
    private String cleanString(String input) {
        return input != null ? input.trim() : "";
    }
    private S parseSearchParams(String query) {
        System.out.println("parseSearchParams: " + query);
        if (query == null || query.isEmpty()) return null;
        query = query.replaceAll("[\n\r\t]", "").trim();

        // Remplacement par Map<String, String>
        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);
        System.out.println("queryParams: " + queryParams);

        try {
            S searchVM = searchClass.getDeclaredConstructor().newInstance();
            System.out.println("searchVM initial: " + searchVM);

            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue(); // Directement une chaîne
                System.out.println("Checking key: " + key);
                if (searchVM instanceof BookSearchVM) {
                    BookSearchVM bookSearchVM = (BookSearchVM) searchVM;
                    if ("idAuthor".equals(key)) {
                        System.out.println("idAuthor: " + value);
                        Author author = new Author();
                        author.setId(Integer.parseInt(value));

                        bookSearchVM.setAuthor(author);
                    }
                    else if ("idSubject".equals(key)) {
                        System.out.println("idSubject: " + value);
                        Subject subject = new Subject();
                        subject.setId(Integer.parseInt(value));

                        bookSearchVM.setSubject(subject);
                    }
                }
                if ("id".equals(key)) {
                    Integer idValue = Integer.valueOf(value);
                    searchClass.getMethod("setId", Integer.class).invoke(searchVM, idValue);
                } else if ("title".equals(key)) {
                    searchClass.getMethod("setTitle", String.class).invoke(searchVM, value);
                } else if ("isbn".equals(key)) {
                    searchClass.getMethod("setIsbn", String.class).invoke(searchVM, value);
                } else if ("minPrice".equals(key)) {
                    Float minPrice = Float.valueOf(value);
                    searchClass.getMethod("setMinPrice", Float.class).invoke(searchVM, minPrice);
                } else if ("maxPrice".equals(key)) {
                    Float maxPrice = Float.valueOf(value);
                    searchClass.getMethod("setMaxPrice", Float.class).invoke(searchVM, maxPrice);
                }
                else if ("maxPageCount".equals(key)) {
                    Integer maxPageCount = Integer.valueOf(value);
                    searchClass.getMethod("setMaxPageCount", Integer.class).invoke(searchVM, maxPageCount);
                }
                else if ("minPageCount".equals(key)) {
                    Integer minPageCount = Integer.valueOf(value);
                    searchClass.getMethod("setMinPageCount", Integer.class).invoke(searchVM, minPageCount);
                }
                else if ("minPublishYear".equals(key)) {
                    Integer minPublishYear = Integer.valueOf(value);
                    searchClass.getMethod("setMinPublishYear", Integer.class).invoke(searchVM, minPublishYear);
                }
                else if ("maxPublishYear".equals(key)) {
                    Integer maxPublishYear = Integer.valueOf(value);
                    searchClass.getMethod("setMaxPublishYear", Integer.class).invoke(searchVM, maxPublishYear);
                }
                else if ("minStockQuantity".equals(key)) {
                    Integer minStockQuantity = Integer.valueOf(value);
                    searchClass.getMethod("setMinStockQuantity", Integer.class).invoke(searchVM, minStockQuantity);
                }
            }
            System.out.println("searchVM final: " + searchVM);
            return searchVM;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }








}