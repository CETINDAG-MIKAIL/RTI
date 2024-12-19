package be.hepl;

import be.hepl.helpers.HttpUtils;
import be.hepl.helpers.JsonUtils;
import be.hepl.helpers.ResponseHelper;
import be.hepl.model.dao.AuthorDAO;
import be.hepl.model.dao.BookDAO;
import be.hepl.model.dao.CRUD;
import be.hepl.model.dao.SubjectDAO;
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
    private final Class<T> entityClass;
    private final Class<S> searchClass;

    public handler(CRUD<T, S> dao, Class<T> entityClass, Class<S> searchClass) {
        this.dao = dao;
        this.entityClass = entityClass;
        this.searchClass = searchClass;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");

        // Si la requête est une demande de pré-vol (OPTIONS), envoyer une réponse vide
        if ("OPTIONS".equals(method)) {
            exchange.sendResponseHeaders(200, -1);
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
        String query = exchange.getRequestURI().getQuery();

        S search = parseSearchParams(query);
        List<T> myList = dao.read(search);
        JsonArray jsonArray = new JsonArray();

        for (T item : myList) {
            JsonObject jsonObject = JsonUtils.convertToJsonObject(item);
            jsonArray.add(jsonObject);
        }
        ResponseHelper.sendSuccessResponse(exchange, jsonArray.toString());
    }
    private int getElementBody(String key, String requestBody) {
        int value = -1;
        try {
            int keyIndex = requestBody.indexOf(key);
            if (keyIndex != -1) {
                int startIndex = requestBody.indexOf(":", keyIndex) + 2;
                int endIndex = requestBody.indexOf("\"", startIndex);
                value = Integer.parseInt(requestBody.substring(startIndex, endIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = HttpUtils.readRequestBody(exchange);
        requestBody = ForBook(requestBody);
        T entity = JsonUtils.fromJson(requestBody, entityClass);
        if (entity == null) throw new IllegalArgumentException("Données JSON invalides");
        dao.create(entity);
        ResponseHelper.sendSuccessResponse(exchange, "Entité créée avec succès");
    }
    private String ForBook(String requestBody)
    {
        int idAuthor = getElementBody("idAuthor", requestBody);
        int idSubject = getElementBody("idSubject", requestBody);

        if (idAuthor != -1 && idSubject != -1) {
            AuthorDAO authorDAO = new AuthorDAO();
            authorDAO.read();
            Author author = authorDAO.getById(idAuthor);
            if (author == null) {
                throw new IllegalArgumentException("Auteur introuvable avec l'ID : " + idAuthor);
            }

            SubjectDAO subjectDAO = new SubjectDAO();
            subjectDAO.read();

            Subject subject = subjectDAO.getById(idSubject);
            if (subject == null) {
                throw new IllegalArgumentException("Sujet introuvable avec l'ID : " + idSubject);
            }
            String jsonAuthor = JsonUtils.toJson(author);

            String jsonSubject = JsonUtils.toJson(subject);
            requestBody = requestBody.replace("\"idAuthor\":\"" + idAuthor + "\"", "\"author\":" + jsonAuthor);
            requestBody = requestBody.replace("\"idSubject\":\"" + idSubject + "\"", "\"subject\":" + jsonSubject);
        }
        return requestBody;
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        System.out.println("query: " + query);

        String requestBody = HttpUtils.readRequestBody(exchange);
        System.out.println("requestBody: " + requestBody);

        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);
        System.out.println("queryParams: " + queryParams);

        Integer id = null;
        if (queryParams.containsKey("id")) {
            String idValue = queryParams.get("id");
            try {
                id = Integer.parseInt(idValue);
                System.out.println("id: " + id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID invalide : " + idValue);
            }
        }
        if (id == null) throw new IllegalArgumentException("ID manquant dans les paramètres de requête");
        System.out.println("11111111111111111111");
        dao.read();
        System.out.println("dao.read()" + dao.read());
        T existingEntity = dao.getById(id);
        System.out.println("existingEntity: " + existingEntity);
        System.out.println("dao " + dao.getById(id));
        System.out.println("222222222222222222222222222");
        if (existingEntity == null) throw new IllegalArgumentException("Entité non trouvée pour l'ID : " + id);
        System.out.println("existingEntity: " + existingEntity);


        String updatedRequestBody = addIdToRequestBody(requestBody, id);
        System.out.println("updatedRequestBody: " + updatedRequestBody);

        T updatedEntity = JsonUtils.fromJson(updatedRequestBody, entityClass);
        if (updatedEntity == null) throw new IllegalArgumentException("Données JSON invalides");
        System.out.println("updatedEntity: " + updatedEntity);

        dao.update(updatedEntity);

        ResponseHelper.sendSuccessResponse(exchange, "Entité mise à jour avec succès");
    }
    private String addIdToRequestBody(String requestBody, Integer id) {
        String updatedRequestBody = "{\"id\": " + id + ", " + requestBody.substring(1);
        return updatedRequestBody;
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);

        Integer id = null;
        if (queryParams.containsKey("id")) {
            Object idValue = queryParams.get("id");
            if (idValue instanceof String) {
                try {
                    id = Integer.parseInt((String) idValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ID invalide : " + idValue);
                }
            } else {
                throw new IllegalArgumentException("ID doit être une chaîne de caractères");
            }
        }

        if (id == null) throw new IllegalArgumentException("ID manquant dans les paramètres de requête");

        dao.delete(id);
        ResponseHelper.sendSuccessResponse(exchange, "Entité supprimée avec succès");
    }
    private S parseSearchParams(String query) {
        if (query == null || query.isEmpty()) return null;
        query = query.replaceAll("[\n\r\t]", "").trim();

        Map<String, String> queryParams = HttpUtils.parseQueryParams(query);

        try {
            S searchVM = searchClass.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (searchVM instanceof BookSearchVM) {
                    BookSearchVM bookSearchVM = (BookSearchVM) searchVM;
                    if ("idAuthor".equals(key)) {
                        Author author = new Author();
                        author.setId(Integer.parseInt(value));

                        bookSearchVM.setAuthor(author);
                    }
                    else if ("idSubject".equals(key)) {
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
                } else if ("name".equals(key)) {
                    searchClass.getMethod("setName", String.class).invoke(searchVM, value);
                } else if ("lastName".equals(key)) {
                    searchClass.getMethod("setLastName", String.class).invoke(searchVM, value);
                }else if ("firstName".equals(key)) {
                    searchClass.getMethod("setFirstName", String.class).invoke(searchVM, value);
                }else if ("birthDate".equals(key)) {
                    searchClass.getMethod("setBirthDate", String.class).invoke(searchVM, value);
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
            return searchVM;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }








}