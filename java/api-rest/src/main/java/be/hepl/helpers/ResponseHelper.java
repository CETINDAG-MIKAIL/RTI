package be.hepl.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseHelper {

    // Envoie une réponse avec le statut HTTP et le message
    public static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length); // Envoie les en-têtes de réponse avec la longueur correcte
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    public static void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        sendResponse(exchange, statusCode, "Erreur : " + errorMessage);
    }
    public static void sendSuccessResponse(HttpExchange exchange, String successMessage) throws IOException {
        sendResponse(exchange, 200, successMessage);
    }
}
