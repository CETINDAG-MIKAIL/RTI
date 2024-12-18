package be.hepl.controller;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;

public class HandlerRoot implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        File file = new File(requestPath);

        System.out.println("----- is file directory ? " + file.isDirectory());

        if (file.isDirectory()) {
            System.out.println("Directory");
            requestPath += "index.html";
        }

        System.out.println("\n----- requestPath = " + requestPath);
        System.out.println("----- requestMethod = " + requestMethod + "\n");

        if (requestPath.endsWith(".html") || file.isDirectory())
        {
            HandlerHtml handlerHtml = new HandlerHtml();
            handlerHtml.handle(exchange);
        } else if (requestPath.endsWith(".css"))
        {
            HandlerCss handlerCss = new HandlerCss();
            handlerCss.handle(exchange);
        } else if (requestPath.endsWith(".jpg") || requestPath.endsWith(".jpeg") || requestPath.endsWith(".png") || requestPath.endsWith(".gif"))
        {
            HandlerImages handlerImages = new HandlerImages();
            handlerImages.handle(exchange);
        } else if(requestPath.endsWith(".js"))
        {
            HandlerJs handlerJs = new HandlerJs();
            handlerJs.handle(exchange);
        }
        else Erreur404(exchange);
    }
    public static void Erreur404(HttpExchange exchange) throws IOException
    {
        String reponse = "Fichier introuvable !!!";
        String requestPath = "java/serveur-http/src/main/java/be/hepl/view/html/404.html";

        File file = new File(requestPath);
        exchange.sendResponseHeaders(200, file.length());
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        OutputStream os = exchange.getResponseBody();
        Files.copy(file.toPath(), os);
        os.close();

        System.out.println("KO");
    }
}
