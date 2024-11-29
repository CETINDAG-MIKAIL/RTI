package be.hepl;

import java.io.*;
import java.util.Properties;

public class FileHelper {
    public static boolean writeToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(content + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("ERROR : cannot write to file " + filename + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean clearFileContent(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            return true;
        } catch (IOException e) {
            System.out.println("ERROR : cannot clear file " + filename + " content : " + e.getMessage());
            return false;
        }
    }

    public static int getSettingInt(String nomFichier, String nomParam) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(nomFichier)) {
            properties.load(fis);
            String value = properties.getProperty(nomParam);
            if (value != null) {
                return Integer.parseInt(value);
            } else {
                System.err.println("Paramètre non trouvé : " + nomParam);
                return -1;
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
            return -1;
        } catch (NumberFormatException e) {
            System.err.println("Valeur invalide pour le paramètre : " + nomParam);
            return -1;
        }
    }

    public static String getSettingString(String nomFichier, String nomParam) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(nomFichier)) {
            properties.load(fis);
            String value = properties.getProperty(nomParam);
            if (value == null) {
                System.err.println("Paramètre non trouvé : " + nomParam);
            }
            return value;

        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Valeur invalide pour le paramètre : " + nomParam);
            return null;
        }
    }

    public static void main (String[] args) {
        System.out.println("\n| Test de la classe FileHelper : |");

        System.out.println("Répertoire de travail actuel : " + System.getProperty("user.dir") + "\n");

        String nomFichier = "./config.properties";

        int portServeur = getSettingInt(nomFichier, "PORT_ENCODING");
        int nbThreadsPool = getSettingInt(nomFichier, "NUM_THREADS");
        String ipServeur = getSettingString(nomFichier, "IP_ENCODING");

        System.out.println("portServer = " + portServeur);
        System.out.println("nbThreadsPool = " + nbThreadsPool);
        System.out.println("ipServeur = " + ipServeur);
    }
}
