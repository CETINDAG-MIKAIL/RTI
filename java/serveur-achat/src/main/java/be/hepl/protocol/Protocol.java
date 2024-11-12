package be.hepl.protocol;

import be.hepl.myException.EndConnexionException;
import src.main.java.be.hepl.request.Request;
import src.main.java.be.hepl.response.Response;

import java.net.Socket;

public interface Protocol {
    String getNom();

    Response processingRequest(Request requete, Socket socket) throws EndConnexionException;
}
