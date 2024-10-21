package be.hepl.myLibraries.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCP {
    public static Socket clientSocket(String ipServeur, int portServeur) {
        Socket sClient = new Socket();
        try {
            sClient.connect(new InetSocketAddress(InetAddress.getByName(ipServeur), portServeur));
            System.out.println("connect() réussi !");
        } catch (IOException e) {
            System.err.println("Erreur de connect() : " + e.getMessage());
            return null;
        }
        return sClient;
    }

    public static int send(Socket sSocket, String trame) throws IOException {
        System.out.println("Trame envoyée = " + trame);
        try {
            OutputStream os = sSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true);
            writer.println(trame);

            return trame.length();
        } catch (IOException e) {
            System.err.println("Erreur d'envoi : " + e.getMessage());
            sSocket.close();
            return -1;
        }
    }

    public static String receive(Socket sSocket) throws IOException {
        StringBuilder maRequete = new StringBuilder();
        try {
            int bite, nbBiteAuTotal = 0;

            while ((bite = sSocket.getInputStream().read()) != -1 && bite != '#') {
                maRequete.append(Character.toString(bite));
                nbBiteAuTotal = nbBiteAuTotal * 10 + Character.getNumericValue(bite);
            }
            maRequete.append('#');

            byte[] responseBytes = new byte[nbBiteAuTotal];

            sSocket.getInputStream().read(responseBytes);

            maRequete.append(new String(responseBytes));

        } catch (IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
            sSocket.close();
            return "";
        }
        System.out.println("Trame reçue = " + maRequete);

        return maRequete.toString();
    }
}
