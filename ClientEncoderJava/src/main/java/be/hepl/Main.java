package be.hepl;

import java.net.Socket;

import static be.hepl.myLibraries.file.FileHelper.*;
import static be.hepl.myLibraries.socket.TCP.*;

public class Main {
    private static Socket sClient;

    public static void main(String[] args) {

        int portServeur = getSettingInt("../config.txt", "PORT_ENCODING");
        String ipServ = getSettingString("../config.txt", "IP_ENCODING");

        sClient = clientSocket(ipServ, portServeur);

        if(sClient == null) {
            System.out.println("Erreur de connexion");
            return;
        }


        System.out.println("Hello world!");
    }

    public static Socket getSClient() {
        return sClient;
    }

}