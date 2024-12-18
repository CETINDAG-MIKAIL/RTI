package be.hepl.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerHtml implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        File file = new File(requestPath);
        if (file.isDirectory()) {
            requestPath += "index.html";
        }

        System.out.print("HandlerHtml (methode " + requestMethod + ")   = " + requestPath + " --> ");

        requestPath = "java/serveur-http/src/main/java/be/hepl/view/html" + requestPath;

        file = new File(requestPath);
        if (file.exists())
        {
            exchange.sendResponseHeaders(200, file.length());
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            OutputStream os = exchange.getResponseBody();
            Files.copy(file.toPath(), os);
            os.close();
            System.out.println("OK");
        }
        else HandlerRoot.Erreur404(exchange);
    }
}
