package be.hepl;

import java.net.Socket;

import static be.hepl.MyLibraries.file.FileHelper.*;
import static be.hepl.MyLibraries.socket.TCP.*;

public class Main {
    public static void main(String[] args) {

        int portServeur = getSettingInt("../config.txt", "PORT_ENCODING");
        String ipServ = getSettingString("../config.txt", "IP_ENCODING");

        Socket sClient = clientSocket(ipServ, portServeur);

        if(sClient == null) {
            System.out.println("Erreur de connexion");
            return;
        }


        System.out.println("Hello world!");
    }
}