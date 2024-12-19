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
        System.out.println("body : "+ body);
        StringBuilder stringBuilder = new StringBuilder();
        int byteRead;
        while ((byteRead = body.read()) != -1) {
            stringBuilder.append((char) byteRead);
        }
        System.out.println("stringBuilder : "+ stringBuilder);
        return stringBuilder.toString();

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
