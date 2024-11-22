package Window;

import be.hepl.FileHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MySocket {
    private static MySocket instance; // Instance unique de la classe
    private static Socket socket;
    private static String ipServer;
    private static Integer portServer;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    // Constructeur privé pour empêcher l'instanciation directe
    private MySocket() throws IOException {
        createSocket();
    }

    // Méthode statique pour obtenir l'instance unique de la classe
    public static MySocket getInstance() throws IOException {
        if (instance == null) {
            instance = new MySocket();
        }
        return instance;
    }

    public Socket getMySocket() {
        return socket;
    }

    private static void createSocket() throws IOException {
        // Chargement de la configuration si nécessaire
        if (ipServer == null) {
            ipServer = FileHelper.getSettingString("./config.txt", "IP_PURCHASE");
        }

        if (portServer == null) {
            portServer = FileHelper.getSettingInt("./config.txt", "PORT_PURCHASE");
        }

        // Création de la socket si elle n'est pas encore initialisée
        if (socket == null) {
            socket = new Socket(ipServer, portServer);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
    }

    public static void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                instance = null;
                socket = null;
                oos = null;
                ois = null;
            }
        }
    }

    // Méthodes pour accéder aux flux d'entrée et de sortie
    public ObjectOutputStream getOutputStream() {
        return oos;
    }

    public ObjectInputStream getInputStream() {
        return ois;
    }
}
