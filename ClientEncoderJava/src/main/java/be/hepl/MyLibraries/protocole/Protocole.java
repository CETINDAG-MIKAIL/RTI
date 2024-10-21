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

        System.out.println("requete à envoyer = " + requete);

        return String.valueOf(requete.length()) + '#' + requete.toString();
    }

    public static ArrayList<String> readTrame(String request) {
        ArrayList<String> fields = new ArrayList<>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
        StringBuilder lengthStr = new StringBuilder();
        int totalRead = 0;

        try {
            int byteRead;
            while ((byteRead = inputStream.read()) != -1 && (char) byteRead != '#') {
                lengthStr.append((char) byteRead);
            }

            int totalLength = Integer.parseInt(lengthStr.toString());

            while (inputStream.available() > 0 && totalRead < totalLength) {
                lengthStr.setLength(0);
                while ((byteRead = inputStream.read()) != -1 && (char) byteRead != '#') {
                    lengthStr.append((char) byteRead);
                }

                int fieldLength = Integer.parseInt(lengthStr.toString());
                if (totalRead + fieldLength > totalLength) {
                    break;
                }

                byte[] buffer = new byte[fieldLength];
                int bytesRead = inputStream.read(buffer);
                if (bytesRead != fieldLength) {
                    System.err.println("Error: " + bytesRead + " bytes read instead of " + fieldLength);
                    break;
                }

                String field = new String(buffer);
                fields.add(field);
                totalRead += fieldLength + lengthStr.length();
            }
        } catch (IOException e) {
            System.err.println("Input/output error: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Format error: " + e.getMessage());
            return null;
        }

        return fields;
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
