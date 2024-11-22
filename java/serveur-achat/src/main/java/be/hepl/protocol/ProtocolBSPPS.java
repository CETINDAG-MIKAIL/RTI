package be.hepl.protocol;

import be.hepl.myException.EndConnexionException;
import be.hepl.request.*;
import be.hepl.response.*;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;

import java.net.Socket;
import java.util.Arrays;


public class ProtocolBSPPS implements Protocol {
    private Logger logger;

    @Override
    public String getNom() {
        return "ProtocolBSPPS";
    }

    @Override
    public Response processingRequest(Request request, Socket socket) throws EndConnexionException {
//        logger = MyLogger.getInstance();
//
//        if(request instanceof RequestEmployee) {
//            return ProcessingEmployee((RequestEmployee) request);
//        }
//        if(request instanceof RequestClient) {
//            return ProcessingClient((RequestClient) request, socket);
//        }
//        if(request instanceof RequestDigest) {
//            return ProcessingDigest((RequestDigest) request, socket)
//        }
//        if(request instanceof RequestSelectBook) {
//            return ProcessingSelectBook((RequestSelectBook) request);
//        }
//
//        if(request instanceof RequestAddCaddyItem) {
//            return ProcessingAddCaddyItem(null);
//        }
//        if(request instanceof RequestDeleteCaddyItem) {
//            return ProcessingDeleteCaddyItem((RequestDeleteCaddyItem) request);
//        }
//        if(request instanceof RequestCancelCaddy) {
//            return ProcessingCancelCaddy((RequestCancelCaddy) request);
//        }
//        if(request instanceof RequestPayCaddy) {
//            return ProcessingPayCaddy((RequestPayCaddy) request);
//        }
//        if(request instanceof RequestEmptyCaddy) {
//            return ProcessingEmptyCaddy((RequestEmptyCaddy) request);
//        }
        return null;
    }


//    private synchronized ResponseEmployee ProcessingEmployee(RequestEmployee request) {
//
//        return new ResponseEmployee(true);
//    }
//
//    private synchronized Response ProcessingClient(RequestClient request, Socket socket) throws EndConnexionException {
//        ProtocolBSPP pBSPP = new ProtocolBSPP();
//
//        ResponseClient response = (ResponseClient)pBSPP.processingRequest(request, socket);
//
//        if(!response.isValide()) {
//            return response;
//        }
//
//        byte[] salt = CryptoHelper.generateSalt();
//
//        return new ResponseSecure(response, salt);
//    }
//
//    private synchronized Response ProcessingDigest(RequestDigest request, Socket socket) throws EndConnexionException {
//        byte[] recalculatedDigest = CryptoHelper.generateDigest(request.getClientNumber(), storedSalt);
//
//        // Vérifier si les deux digests correspondent
//        boolean isValid = Arrays.equals(recalculatedDigest, request.getDigest());
//
//        // Créer une réponse
//        if (isValid) {
//            return new ResponseSuccess(true);
//        }
//
//        return new ResponseSuccess(false);
//    }
//
//    private synchronized ResponseSelectBook ProcessingSelectBook(RequestSelectBook request) {
//
//        return null;
//    }
//
//    private synchronized ResponseAddCaddyItem ProcessingAddCaddyItem(RequestAddCaddyItem request, Integer clientId) {
//
//        return null;
//    }
//
//    private synchronized ResponseDeleteCaddyItem ProcessingDeleteCaddyItem(RequestDeleteCaddyItem request) {
//
//        return null;
//    }
//
//    private synchronized ResponseCancelCaddy ProcessingCancelCaddy(RequestCancelCaddy request) throws EndConnexionException {
//
//        return null;
//    }
//
//    private synchronized ResponsePayCaddy ProcessingPayCaddy(RequestPayCaddy request) {
//
//        return null;
//    }
//
//    private synchronized ResponseEmptyCaddy ProcessingEmptyCaddy(RequestEmptyCaddy request) {
//
//        return null;
//    }
}

