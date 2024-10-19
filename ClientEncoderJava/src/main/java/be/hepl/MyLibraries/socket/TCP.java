package be.hepl.MyLibraries.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                maRequete.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
            sSocket.close();
            return "";
        }

        System.out.println("Trame reçue = " + maRequete);

        return maRequete.toString();
    }
}
