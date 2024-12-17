package be.hepl.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    // Lire le corps de la requête HTTP (par exemple, pour POST ou PUT)
    public static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        int byteRead;
        while ((byteRead = body.read()) != -1) {
            stringBuilder.append((char) byteRead);
        }
        return stringBuilder.toString();
    }

    // Extraire la valeur d'un paramètre de la query string
    public static String getQueryParam(HttpExchange exchange, String param) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String p : params) {
                String[] keyValue = p.split("=");
                if (keyValue[0].equals(param)) {
                    return keyValue.length > 1 ? keyValue[1] : null;
                }
            }
        }
        return null; // Si le paramètre n'existe pas
    }
    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    queryParams.put(key, value); // Stocker la clé et la valeur
                }
            }
        }
        return queryParams;
    }





}
