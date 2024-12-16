package be.hepl.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonUtils {

    private static final Gson gson = new Gson();

    // Convertir un objet Java en JSON
    public static String convertToJson(Object object) {
        System.out.println("Converting object to json" + object);
        return gson.toJson(object);
    }

    // Convertir une chaîne JSON en objet Java
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            return null; // Si la syntaxe JSON est invalide, renvoie null
        }
    }
}
