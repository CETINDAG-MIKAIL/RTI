package be.hepl.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseHelper {

    // Envoie une réponse avec le statut HTTP et le message
    public static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8); // Convertir la réponse en bytes en utilisant UTF-8
        exchange.sendResponseHeaders(statusCode, responseBytes.length); // Envoie les en-têtes de réponse avec la longueur correcte

        // Utilisation de try-with-resources pour s'assurer que OutputStream est fermé correctement
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes); // Écrire les données dans la réponse
        } // Le flux sera automatiquement fermé ici
    }

    // Envoie une réponse d'erreur générique
    public static void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        sendResponse(exchange, statusCode, "Erreur : " + errorMessage);
    }

    // Envoie une réponse de succès avec un message
    public static void sendSuccessResponse(HttpExchange exchange, String successMessage) throws IOException {
        sendResponse(exchange, 200, successMessage);
    }
}
