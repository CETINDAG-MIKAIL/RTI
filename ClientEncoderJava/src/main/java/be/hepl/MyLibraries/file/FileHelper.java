package be.hepl.MyLibraries.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {
    public static int getSettingInt(String nomFichier, String nomParam) {
        int setting = -1;
        String line;

        try (BufferedReader configFile = new BufferedReader(new FileReader(nomFichier))) {
            while ((line = configFile.readLine()) != null) {
                if (line.startsWith(nomParam + "=")) {
                    String value = line.substring(nomParam.length() + 1);
                    setting = Integer.parseInt(value);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible d'ouvrir le fichier : " + e.getMessage());
            return -1;
        }

        return setting;
    }

    public static String getSettingString(String nomFichier, String nomParam) {
        String setting = "";
        String line;

        try (BufferedReader configFile = new BufferedReader(new FileReader(nomFichier))) {
            while ((line = configFile.readLine()) != null) {
                if (line.startsWith(nomParam + "=")) {

                    setting = line.substring(nomParam.length() + 1);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible d'ouvrir le fichier : " + e.getMessage());
            return "";
        }

        return setting;
    }

    public static void main (String[] args) {
        System.out.println("\n| Test de la classe FileHelper : |");

        System.out.println("Répertoire de travail actuel : " + System.getProperty("user.dir") + "\n");

        String nomFichier = "../config.txt";

        int portServeur = getSettingInt(nomFichier, "PORT_ENCODING");
        int nbThreadsPool = getSettingInt(nomFichier, "NUM_THREADS");
        String ipServeur = getSettingString(nomFichier, "IP_ENCODING");

        System.out.println("portServeur = " + portServeur);
        System.out.println("nbThreadsPool = " + nbThreadsPool);
        System.out.println("ipServeur = " + ipServeur);
    }
}
