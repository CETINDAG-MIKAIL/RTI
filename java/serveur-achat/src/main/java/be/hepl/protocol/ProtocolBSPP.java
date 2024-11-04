package be.hepl.protocol;

import be.hepl.myException.EndConnexionException;
import com.sun.net.httpserver.Request;

import java.net.Socket;

public class ProtocolBSPP implements Protocol {

    @Override
    public String getNom() {
        return "BSPP";
    }

    Request ProcessingRequest(Request request, Socket socket) throws EndConnexionException {

        if(request.getType() == null) {
            return new Request("unknown", "Error : the type is null");
        }
        if(request.getMessage() == null) {
            return new Request("unknown", "Error : the message is null");
        }

        if(request.getType() == "CLIENT") {

        }

        if(request.getType() == "SELECT_BOOK") {

        }

        if(request.getType() == "ADD_CADDY_ITEM") {

        }

        if(request.getType() == "DEL_CADDY_ITEM") {

        }

        if(request.getType() == "CANCEL_CADDY") {

        }

        if(request.getType() == "PAY_CADDY") {

        }

    }
}
