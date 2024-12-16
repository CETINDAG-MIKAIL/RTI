package be.hepl;

import be.hepl.helpers.HttpUtils;
import be.hepl.helpers.JsonUtils;
import be.hepl.helpers.ResponseHelper;
import be.hepl.model.dao.CRUD;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;
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

        Map<String, Object> queryParams = HttpUtils.parseQueryParams(query);

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
        Map<String, Object> queryParams = HttpUtils.parseQueryParams(query);

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

        Map<String, Object> queryParams = HttpUtils.parseQueryParams(query); // Obtient le Map des paramètres
        System.out.println("queryParams: " + queryParams);

        try {
            // Créer une instance de S via la réflexion
            S searchVM = searchClass.getDeclaredConstructor().newInstance();
            System.out.println("searchVM initial: " + searchVM);

            // Parcourir les paramètres et définir les valeurs dynamiquement
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                System.out.println("Checking key: " + key);

                // Vérifier si la clé est 'author', qui contient un sous-objet
                if (key.equals("author")) {
                    // Ici, 'author' est un Map contenant une ou plusieurs clés (ex: firstName, lastName, etc.)
                    Map<String, Object> authorParams = (Map<String, Object>) value; // Cela donne {firstName=Dan, lastName=Brown, id=1}

                    // Créer un nouvel auteur à chaque fois qu'on entre dans ce bloc
                    Author author = new Author();

                    // Extraire les différents paramètres de l'auteur et les affecter à l'objet author
                    String firstName = (String) authorParams.get("firstName");
                    String lastName = (String) authorParams.get("lastName");
                    String birthDate = (String) authorParams.get("birthDate");
                    String id = (String) authorParams.get("id");

                    if (searchVM instanceof BookSearchVM) {
                        BookSearchVM bookSearchVM = (BookSearchVM) searchVM;

                        // Assigner les valeurs à l'auteur
                        if (firstName != null) {
                            System.out.println("firstName: " + firstName);
                            author.setFirstName(firstName);
                        }
                        if (id != null) {
                            System.out.println("id: " + id);
                            author.setId(Integer.parseInt(id));
                        }
                        if (lastName != null) {
                            System.out.println("lastName: " + lastName);
                            author.setLastName(lastName);
                        }
                        if (birthDate != null) {
                            System.out.println("birthDate: " + birthDate);
                            author.setBirthDate(birthDate);
                        }

                        // Toujours assigner un nouvel auteur
                        bookSearchVM.setAuthor(author);
                    }
                }
                // Cas du champ 'subject'...
                else if (key.equals("subject")) {
                    System.out.println("Processing subject field: " + key);

                    if (searchVM instanceof BookSearchVM) {
                        BookSearchVM bookSearchVM = (BookSearchVM) searchVM;

                        // Créer un nouvel objet Subject à chaque fois qu'on entre dans ce bloc
                        Subject subject = new Subject();

                        // On suppose que 'value' pour 'subject' est un Map comme pour 'author'
                        if (value instanceof Map) {
                            Map<String, Object> subjectParams = (Map<String, Object>) value;

                            // Extraire les sous-clés de "subject" (par exemple "Id" et "name")
                            String subjectId = (String) subjectParams.get("id");  // "id" pour l'identifiant du sujet
                            String subjectName = (String) subjectParams.get("name");  // "name" pour le nom du sujet

                            // Assigner les valeurs extraites à l'objet subject
                            if (subjectId != null) {
                                System.out.println("subjectId: " + subjectId);
                                subject.setId(Integer.valueOf(subjectId));  // Assigner à subject.id
                            }
                            if (subjectName != null) {
                                System.out.println("subjectName: " + subjectName);
                                subject.setName(subjectName);  // Assigner à subject.name
                            }

                            // Toujours assigner un nouvel objet subject
                            bookSearchVM.setSubject(subject);
                        }
                    }
                } else if ("id".equals(key)) {
                    Integer idValue = Integer.valueOf(value.toString());
                    searchClass.getMethod("setId", Integer.class).invoke(searchVM, idValue);
                } else if ("title".equals(key)) {
                    searchClass.getMethod("setTitle", String.class).invoke(searchVM, value);
                } else if ("isbn".equals(key)) {
                    searchClass.getMethod("setIsbn", String.class).invoke(searchVM, value);
                } else if ("minPrice".equals(key)) {
                    Float minPrice = Float.valueOf(value.toString());
                    searchClass.getMethod("setMinPrice", Float.class).invoke(searchVM, minPrice);
                } else if ("maxPrice".equals(key)) {
                    Float maxPrice = Float.valueOf(value.toString());
                    searchClass.getMethod("setMaxPrice", Float.class).invoke(searchVM, maxPrice);
                } else if ("FirstName".equals(key)) {
                    searchClass.getMethod("setFirstName", String.class).invoke(searchVM, value);
                } else if ("LastName".equals(key)) {
                    searchClass.getMethod("setLastName", String.class).invoke(searchVM, value);
                } else if ("BirthDate".equals(key)) {
                    searchClass.getMethod("setBirthDate", String.class).invoke(searchVM, value);
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