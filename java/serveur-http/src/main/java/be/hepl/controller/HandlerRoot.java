package be.hepl.controller;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class HandlerRoot implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        if (requestPath.endsWith(".html"))
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
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
        System.out.println("KO");
    }
    public static void testHandle(HttpExchange exchange) throws IOException {
        System.out.println("Hello World!");

        // Lecture de la requete
        String requestPath = exchange.getRequestURI().getPath();
        System.out.println("requestPath = " + requestPath);

        String requestMethod = exchange.getRequestMethod();
        System.out.println("requestMethod = " + requestMethod);

        Headers requestHeaders = exchange.getRequestHeaders();
        System.out.println("Header : " );
        for (String key : requestHeaders.keySet())
            System.out.println(key + ": " + requestHeaders.get(key));

        InputStream is = exchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String ligne;
        System.out.println("Request Body :");
        while ((ligne = br.readLine()) != null)
            System.out.println(ligne);

        // Ecriture de la reponse
        String reponse = "ceci est le corps de la reponse";
        exchange.getResponseHeaders().set("Content-Type","text/plain");
        exchange.sendResponseHeaders(200,reponse.length());
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();

        System.out.println("--- Reponse envoyee ---\n");
    }
}
