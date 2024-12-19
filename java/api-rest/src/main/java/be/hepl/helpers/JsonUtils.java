package be.hepl.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonUtils {

    private static final Gson gson = new Gson();

    public static JsonObject convertToJsonObject(Object object) {
        System.out.println("Converting object to JsonObject");
        JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
        System.out.println("Type  : " + ((Object) jsonObject).getClass().getName());
        System.out.println(jsonObject);
        return jsonObject;
    }


    // Convertir une chaîne JSON en objet Java
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            System.out.println("Converting json to " + classOfT);
            System.out.println("json : " + json);
            System.out.println("gson.fromJson(json, classOfT)" + gson.fromJson(json, classOfT));
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            System.out.println("Json syntax exception");
            return null;
        }
    }
    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
