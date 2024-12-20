package be.hepl.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {


    public static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        byte[] bytes = body.readAllBytes(); // Lit tout le contenu de l'InputStream
        String result = new String(bytes, StandardCharsets.UTF_8); // Convertit les octets en String
        System.out.println("body : " + body);
        System.out.println("result : " + result);
        return result;
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
