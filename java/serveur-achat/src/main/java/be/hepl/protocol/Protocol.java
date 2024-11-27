package be.hepl.protocol;

import be.hepl.myException.EndConnexionException;
import be.hepl.request.Request;
import be.hepl.response.Response;

import java.net.Socket;
import java.util.HashMap;

public interface Protocol {
    String getNom();

    HashMap<Socket, Integer> getConnectedClients();

    Response processingRequest(Request request, Socket socket) throws EndConnexionException;
}
