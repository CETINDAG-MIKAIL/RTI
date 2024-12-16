package be.hepl;

import be.hepl.helpers.HttpUtils;
import be.hepl.helpers.JsonUtils;
import be.hepl.helpers.ResponseHelper;
import be.hepl.model.dao.CRUD;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
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
        try {
            System.out.println("handle switch: " + method);
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
        //System.out.println("search: " + search.toString());
        List<T> myList = dao.read(search); // Dao attend un SubjectSearchVM
        System.out.println("myList: " + myList);
        String response = convertToJsonList(myList); // Convertit les sujets récupérés en JSON
        ResponseHelper.sendSuccessResponse(exchange, JsonUtils.convertToJson(response)); // Envoi de la réponse
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = HttpUtils.readRequestBody(exchange);
        T entity = JsonUtils.fromJson(requestBody, entityClass);
        if (entity == null) throw new IllegalArgumentException("Données JSON invalides");
        dao.create(entity);
        ResponseHelper.sendSuccessResponse(exchange, "Entité créée avec succès");
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String requestBody = HttpUtils.readRequestBody(exchange);

        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);
        Integer id = queryParams.containsKey("id") ? Integer.parseInt(queryParams.get("id")) : null;

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

        Integer id = queryParams.containsKey("id") ? Integer.parseInt(queryParams.get("id")) : null;
        if (id == null) throw new IllegalArgumentException("ID manquant dans les paramètres de requête");

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

        Map<String, String> queryParams = HttpUtils.parseQueryParams(query); // Obtient le Map des paramètres
        System.out.println("queryParams: " + queryParams);

        try {
            // Créer une instance de S via la réflexion
            S searchVM = searchClass.getDeclaredConstructor().newInstance();
            System.out.println("searchVM initial: " + searchVM);

            // Utiliser les valeurs du Map pour définir les propriétés de searchVM
            if (queryParams.containsKey("name")) {
                String rawName = queryParams.get("name");
                searchClass.getMethod("setName", String.class).invoke(searchVM, rawName);
            }

            if (queryParams.containsKey("id")) {
                try {
                    String rawId = queryParams.get("id");
                    Integer idValue = Integer.valueOf(rawId);
                    searchClass.getMethod("setId", Integer.class).invoke(searchVM, idValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ID invalide : " + queryParams.get("id"));
                }
            }

            if (queryParams.containsKey("FirstName")) {
                String rawFirstName = queryParams.get("FirstName");
                searchClass.getMethod("setFirstName", String.class).invoke(searchVM, rawFirstName);
            }

            if (queryParams.containsKey("LastName")) {
                String rawLastName = queryParams.get("LastName");
                searchClass.getMethod("setLastName", String.class).invoke(searchVM, rawLastName);
            }

            if (queryParams.containsKey("BirthDate")) {
                String rawBirthDate = queryParams.get("BirthDate");
                searchClass.getMethod("setBirthDate", String.class).invoke(searchVM, rawBirthDate);
            }

            // Ajout des paramètres spécifiques à Book
            if (queryParams.containsKey("isbn")) {
                try {
                    String rawIsbn = queryParams.get("isbn");
                    searchClass.getMethod("setIsbn", String.class).invoke(searchVM, rawIsbn);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ISBN invalide : " + queryParams.get("isbn"));
                }
            }

            if (queryParams.containsKey("minPrice")) {
                try {
                    String rawMinPrice = queryParams.get("minPrice");
                    Float minPrice = Float.valueOf(rawMinPrice);
                    searchClass.getMethod("setMinPrice", Float.class).invoke(searchVM, minPrice);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("minPrice invalide : " + queryParams.get("minPrice"));
                }
            }

            if (queryParams.containsKey("maxPrice")) {
                try {
                    String rawMaxPrice = queryParams.get("maxPrice");
                    Float maxPrice = Float.valueOf(rawMaxPrice);
                    searchClass.getMethod("setMaxPrice", Float.class).invoke(searchVM, maxPrice);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("maxPrice invalide : " + queryParams.get("maxPrice"));
                }
            }

            if (queryParams.containsKey("title")) {
                String rawTitle = queryParams.get("title");
                searchClass.getMethod("setTitle", String.class).invoke(searchVM, rawTitle);
            }

            if (queryParams.containsKey("minStockQuantity")) {
                try {
                    String rawMinStockQuantity = queryParams.get("minStockQuantity");
                    Integer minStockQuantity = Integer.valueOf(rawMinStockQuantity);
                    searchClass.getMethod("setMinStockQuantity", Integer.class).invoke(searchVM, minStockQuantity);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("minStockQuantity invalide : " + queryParams.get("minStockQuantity"));
                }
            }


            if (queryParams.containsKey("minPublishYear")) {
                try {
                    String rawMinPublishYear = queryParams.get("minPublishYear");
                    Integer minPublishYear = Integer.valueOf(rawMinPublishYear);
                    searchClass.getMethod("setMinPublishYear", Integer.class).invoke(searchVM, minPublishYear);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("minPublishYear invalide : " + queryParams.get("minPublishYear"));
                }
            }

            if (queryParams.containsKey("maxPublishYear")) {
                try {
                    String rawMaxPublishYear = queryParams.get("maxPublishYear");
                    Integer maxPublishYear = Integer.valueOf(rawMaxPublishYear);
                    searchClass.getMethod("setMaxPublishYear", Integer.class).invoke(searchVM, maxPublishYear);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("maxPublishYear invalide : " + queryParams.get("maxPublishYear"));
                }
            }

            if (queryParams.containsKey("minPageCount")) {
                try {
                    String rawMinPageCount = queryParams.get("minPageCount");
                    Integer minPageCount = Integer.valueOf(rawMinPageCount);
                    searchClass.getMethod("setMinPageCount", Integer.class).invoke(searchVM, minPageCount);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("minPageCount invalide : " + queryParams.get("minPageCount"));
                }
            }

            if (queryParams.containsKey("maxPageCount")) {
                try {
                    String rawMaxPageCount = queryParams.get("maxPageCount");
                    Integer maxPageCount = Integer.valueOf(rawMaxPageCount);
                    searchClass.getMethod("setMaxPageCount", Integer.class).invoke(searchVM, maxPageCount);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("maxPageCount invalide : " + queryParams.get("maxPageCount"));
                }
            }

            if (queryParams.containsKey("author")) {
                String rawAuthor = queryParams.get("author");
                searchClass.getMethod("setAuthor", String.class).invoke(searchVM, rawAuthor);
            }

            if (queryParams.containsKey("subject")) {
                String rawSubject = queryParams.get("subject");
                searchClass.getMethod("setSubject", String.class).invoke(searchVM, rawSubject);
            }

            System.out.println("searchVM final: " + searchVM);
            return searchVM;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}