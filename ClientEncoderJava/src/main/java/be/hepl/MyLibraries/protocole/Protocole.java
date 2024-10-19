package be.hepl.myLibraries.protocole;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Protocole {
    public static String createTrame(ArrayList<String> champs) {
        StringBuilder requete = new StringBuilder();
        String longueurChamp;

        for(String champ : champs)
        {
            longueurChamp = String.valueOf(champ.length());
            requete.append(longueurChamp);
            requete.append('#');
            requete.append(champ);
        }

        return requete.toString();
    }

    public static ArrayList<String> readTrame(String requete) throws IOException {
        ArrayList<String> champs = new ArrayList<>();

        // Convertir la requête en un tableau de bytes
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requete.getBytes());

        try {
            while (inputStream.available() > 0) {
                // ----- Lecture du nombre avant le '#' -----
                StringBuilder longueurStr = new StringBuilder();
                int byteRead;

                while ((byteRead = inputStream.read()) != -1 && (char)byteRead != '#') {
                    longueurStr.append((char) byteRead);
                }

                if (longueurStr.isEmpty()) {
                    System.err.println("Erreur : Pas de longueur spécifiée.");
                    return null;
                }

                // ----- Lecture du contenu après le '#' -----
                int longueur = Integer.parseInt(longueurStr.toString());

                byte[] buffer = new byte[longueur];
                int bytesRead = inputStream.read(buffer);

                if (bytesRead != longueur) {
                    System.err.println("Erreur : " + bytesRead + " bytes lus au lieu de " + longueur);
                    return null;
                }

                String champ = new String(buffer);
                champs.add(champ);
            }
        } catch (IOException e) {
            System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
            return null;
        }

        return champs;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("\n| Test de la classe Protocole : |");
        ArrayList<String> champsCration = new ArrayList<>();
        champsCration.add("Petite");
        champsCration.add("phrase");
        champsCration.add("de");
        champsCration.add("test");
        champsCration.add("avecUnLongMot.");

        String requete = createTrame(champsCration);

        System.out.println("La requete créée par createTrame = " + requete);

        ArrayList<String> champsLecture = readTrame(requete);

        if (champsLecture != null) {
            for (String champ : champsLecture) {
                System.out.println(champ);
            }
        } else {
            System.out.println("Erreur dans la lecture de la trame.");
        }
    }
}
