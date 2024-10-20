package be.hepl.myLibraries.protocole;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static be.hepl.myLibraries.protocole.Protocole.*;
import static be.hepl.myLibraries.socket.TCP.*;

public class ProtocoleC {
    public static ArrayList<String> login(String login, String password, Socket sClient) throws IOException {
        ArrayList<String> champs = new ArrayList<>();

        champs.add("LOGIN");
        champs.add(login);
        champs.add(password);

        return responseSwing(sClient, champs);
    }

    public static ArrayList<String> logout(Socket sClient) throws IOException {
        ArrayList<String> champs = new ArrayList<>();

        champs.add("LOGOUT");;

        return responseSwing(sClient, champs);
    }

    // ----------------------------------------------------------------------------- //
    // ------------------------------------ ADD ------------------------------------ //
    // ----------------------------------------------------------------------------- //
    public static ArrayList<String> addAuthor(String lastName, String firstName, String birthDate, Socket sClient) throws IOException {
        ArrayList<String> champs = new ArrayList<>();

        champs.add("ADD_AUTHOR");
        champs.add(lastName);
        champs.add(firstName);
        champs.add(birthDate);

        return responseSwing(sClient, champs);
    }

    public static ArrayList<String> addSubject(String name, Socket sClient) throws IOException {
        ArrayList<String> champs = new ArrayList<>();

        champs.add("ADD_SUBJECT");
        champs.add(name);

        return responseSwing(sClient, champs);
    }

    public static ArrayList<String> addBook(ArrayList<String> vecBook, Socket sClient) throws IOException {
        // --- On remplace le ',' par un '.' pour le prix ---
        String priceElement = vecBook.get(6);   // se trouve à la 7e position

        priceElement = priceElement.replace(',', '.');
        vecBook.set(2, priceElement);

        ArrayList<String> champs = new ArrayList<>();

        champs.add("ADD_BOOK");
        champs.addAll(vecBook);

        return responseSwing(sClient, champs);
    }

    // ----------------------------------------------------------------------------- //
    // ------------------------------------ GET ------------------------------------ //
    // ----------------------------------------------------------------------------- //
    public static ArrayList<String> getSubjects(Socket sClient) throws IOException {
        ArrayList<String> envoyerQT = new ArrayList<>();
        ArrayList<String> champs = new ArrayList<>();
        champs.add("GET_SUBJECT");
        ArrayList<String> vecRecu = responseSwing(sClient, champs);

        // Traiter la réponse reçue
        for (int i = 4; i < vecRecu.size(); i += 2) {
            envoyerQT.add(vecRecu.get(i));
        }

        return envoyerQT;
    }

    public static ArrayList<String> getAuthors(Socket sClient) throws IOException {
        ArrayList<String> envoyerQT = new ArrayList<>();
        ArrayList<String> champs = new ArrayList<>();
        champs.add("GET_AUTHORS");
        ArrayList<String> vecRecu = responseSwing(sClient, champs);

        // Traiter la réponse reçue
        for (int i = 4; i < vecRecu.size(); i += 4) {
            String nomPrenom = vecRecu.get(i) + ", " + vecRecu.get(i + 1);
            envoyerQT.add(nomPrenom);
        }

        return envoyerQT;
    }

    public static ArrayList<String> executeRequest(Socket sClient, String trame) throws IOException {
        ArrayList<String> envoyerQT = new ArrayList<>();

        if (send(sClient, trame) == -1) {
            System.err.println("Erreur send (ProtocoleC) - logout");
            envoyerQT.add("Send erreur (ProtocoleC) - logout");
            return envoyerQT;
        }

        trame = receive(sClient);
        ArrayList<String> vecRecu = readTrame(trame);

        return vecRecu;
    }


    public static ArrayList<String> responseSwing(Socket sClient, ArrayList<String> champs) throws IOException {
        String trame = createTrame(champs);
        ArrayList<String> envoyerQT = new ArrayList<>();

        ArrayList<String> vecRecu = executeRequest(sClient, trame);

        if (vecRecu == null || vecRecu.isEmpty()) {
            System.err.println("Erreur send (ProtocoleC) - logout");
            envoyerQT.add("ko");
            envoyerQT.add("Demande non reçue du serveur, veuillez réessayer");
            return envoyerQT;
        }

        if (vecRecu.get(1).equals("ko")) {
            System.err.println("Erreur send (ProtocoleC) - logout");
            envoyerQT.add("ko");
            envoyerQT.add(vecRecu.get(2));
            return envoyerQT;
        }

        envoyerQT.add("ok");
        envoyerQT.add(vecRecu.get(2));

        if (vecRecu.get(0).equals("ADD_BOOK")) {
            envoyerQT.add(vecRecu.get(3)); // ID du livre
        }

        return envoyerQT;
    }
}
