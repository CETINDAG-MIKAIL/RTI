package be.hepl;

import be.hepl.controller.HandlerRoot;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        HttpServer serveur = null;
        try
        {
            int portServer = FileHelper.getSettingInt("./config.properties", "PORT_ADMIN_HTTP");
            serveur = HttpServer.create(new InetSocketAddress(portServer),0);
            serveur.createContext("/",new HandlerRoot());
            System.out.println("Demarrage du serveur HTTP sur le port " + portServer + "...");
            serveur.start();
        }
        catch (IOException e)
        {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}