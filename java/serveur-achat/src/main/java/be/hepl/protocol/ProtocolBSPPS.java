package be.hepl.protocol;

import be.hepl.CertificateHelper;
import be.hepl.CryptoHelper;
import be.hepl.myException.EndConnexionException;
import be.hepl.request.*;
import be.hepl.response.*;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.Socket;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;

import static be.hepl.CryptoHelper.convertToBytes;


public class ProtocolBSPPS implements Protocol {
    private final ProtocolBSPP pBSPP = new ProtocolBSPP();
    private final HashMap<Socket, byte[]> storedSalt = new HashMap<>();
    private final HashMap<Socket, SecretKey> storedSessionKey = new HashMap<>();
    private final HashMap<Socket, X509Certificate> storedCertificate = new HashMap<>();
    private Logger logger;
    CertificateHelper certHelper = new CertificateHelper("./server.jks", "admin", "server");


    @Override
    public String getNom() {
        return "ProtocolBSPPS";
    }

    @Override
    public HashMap<Socket, Integer> getConnectedClients() {
        return pBSPP.getConnectedClients();
    }

    @Override
    public synchronized Response processingRequest(Request request, Socket socket) throws EndConnexionException {
        logger = MyLogger.getInstance();
        System.out.println("Type réel de la requête : " + request.getClass().getName());

        if(request instanceof RequestSelectBook) {
            System.out.println("RequestSelectBook");
            return ProcessingSelectBook((RequestSelectBook) request, socket);
        }

        Request requestInSecure;
        if(request instanceof RequestSecure) {
            System.out.println("RequestSecure");
            if(((RequestSecure) request).getCertificate() != null && ((RequestSecure) request).getDigest() != null) {

                System.out.println("processingRequest 3");
                return ProcessingDigestAndSessionKey((RequestSecure) request, socket);
            }

            System.out.println("processingRequest 4");
            requestInSecure = ((RequestSecure) request).getRequest();
        } else {

            System.out.println("processingRequest 5");
            return null;
        }

        System.out.println("processingRequest 6");
        if(requestInSecure instanceof RequestClient) {
            System.out.println("ResquestClient");
            return ProcessingClient((RequestSecure) request, socket);
        }

        if(requestInSecure instanceof RequestAddCaddyItem) {
            System.out.println("RequestAddCaddyItem");
            return ProcessingAddCaddyItem((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestDeleteCaddyItem) {
            System.out.println("RequestDeleteCaddyItem");
            return ProcessingDeleteCaddyItem((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestCancelCaddy) {
            System.out.println("RequestCancelCaddy");
            return ProcessingCancelCaddy((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestPayCaddyWithVisa) {
            System.out.println("RequestPayCaddyWithVisa");
            return ProcessingPayCaddy((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestEmptyCaddy) {
            System.out.println("RequestEmptyCaddy");
            return ProcessingEmptyCaddy((RequestSecure) request, socket);
        }
        System.out.println("SERVEUR JE NE PASSE NULL PART DONC JE RETOURNE NULL ");
        return null;
    }

    private synchronized Response ProcessingClient(RequestSecure request, Socket socket) throws EndConnexionException {
        System.out.println("ProcessingClient 0 ");
        RequestClient requestClient = (RequestClient)request.getRequest();
        System.out.println("ProcessingClient 1");

        System.out.println("pBSPP : "+ pBSPP);
        System.out.println("requestClient : " + requestClient);
        System.out.println("socket : " + socket);
        ResponseClient response = (ResponseClient)pBSPP.processingRequest(requestClient, socket);
        System.out.println("ProcessingClient 2");

        if(!response.isValide()) {
            System.out.println("ProcessingClient 3 ");
            return new ResponseSecure(null, null, null, null, null, null);
        }
        System.out.println("ProcessingClient 4");

        byte[] salt = CryptoHelper.generateSalt();

        storedSalt.put(socket, salt);

        System.out.println("ProcessingClient 5 ");
        return new ResponseSecure(null, null, salt, null, null, null);
    }

    private synchronized Response ProcessingDigestAndSessionKey(RequestSecure request, Socket socket) throws EndConnexionException {

        byte[] recalculatedDigest = CryptoHelper.generateDigest(getConnectedClients().get(socket), storedSalt.get(socket), logger);

        // Check if the digest is valid
        boolean isSameDigest = Arrays.equals(recalculatedDigest, request.getDigest());

        if (isSameDigest) {
            // Create the session key
            KeyGenerator genKey = null;
            try {
                genKey = KeyGenerator.getInstance("DESede","BC");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                logger.trace("Error while creating the session key");
            }
            if (genKey != null) {
                genKey.init(new SecureRandom());
                SecretKey sessionKey = genKey.generateKey();

                storedSessionKey.put(socket, sessionKey);
                storedCertificate.put(socket, request.getCertificate());

                byte[] cleSessionCrypte = CryptoHelper.CryptAsymRSA(request.getCertificate().getPublicKey(), sessionKey.getEncoded(), logger);

                ResponseSuccess responseValid = new ResponseSuccess(true);
                CertificateHelper serverHelper = new CertificateHelper("./server.jks", "admin", "server");
                X509Certificate certificate = serverHelper.getCertificate(logger);
                return new ResponseSecure(responseValid, null, null, cleSessionCrypte, certificate, null);
            }

        }

        ResponseSuccess responseNoValid = new ResponseSuccess(false);
        return new ResponseSecure(responseNoValid, null, null, null, null, null);
    }

    private synchronized ResponseSelectBook ProcessingSelectBook(RequestSelectBook request, Socket socket) throws EndConnexionException {

        return (ResponseSelectBook)pBSPP.processingRequest(request, socket);
    }

    private synchronized ResponseAddCaddyItem ProcessingAddCaddyItem(RequestSecure request, Socket socket) throws EndConnexionException {
        System.out.println("ProcessingAddCaddyItem 0 ");
        if (request.getRequestEncrypted() == null || request.getRequestEncrypted().length == 0) {
            System.out.println("request.getRequestEncrypted() is null or empty");
        }
        if (storedSessionKey.get(socket) == null)
            System.out.println("storedSessionKey.get(socket) == null ");
        byte [] decryptedRequest = CryptoHelper.DecryptSymDESede(storedSessionKey.get(socket), request.getRequestEncrypted(), logger);
        System.out.println("ProcessingAddCaddyItem 1");
        RequestAddCaddyItem requestAddCaddyItem = (RequestAddCaddyItem)CryptoHelper.deserializeObject(decryptedRequest, logger);

        System.out.println("ProcessingAddCaddyItem 2");
        return (ResponseAddCaddyItem)pBSPP.processingRequest(requestAddCaddyItem, socket);
    }

    private synchronized ResponseDeleteCaddyItem ProcessingDeleteCaddyItem(RequestSecure request, Socket socket) throws EndConnexionException {

        byte [] decryptedRequest = CryptoHelper.DecryptSymDESede(storedSessionKey.get(socket), request.getRequestEncrypted(), logger);

        RequestDeleteCaddyItem requestDeleteCaddyItem = (RequestDeleteCaddyItem)CryptoHelper.deserializeObject(decryptedRequest, logger);

        return (ResponseDeleteCaddyItem)pBSPP.processingRequest(requestDeleteCaddyItem, socket);
    }

    private synchronized Response ProcessingCancelCaddy(RequestSecure request, Socket socket) throws EndConnexionException {
        System.out.println("ProcessingCancelCaddy 0 ");
        byte[] decryptedRequest = CryptoHelper.DecryptAsymRSA(certHelper.getPrivateKey(logger), request.getRequestEncrypted(), logger);


        System.out.println("ProcessingCancelCaddy 1");
        boolean isSignatureVerified = CryptoHelper.verifySignature(decryptedRequest, request.getSignature(), storedCertificate.get(socket).getPublicKey(), logger);
        System.out.println("ProcessingCancelCaddy 2");
        // Check if the digest is valid, if they are the same, the signature is valid
        //boolean isSameDigest = Arrays.equals(recalculatedDigest, decryptedDigest);

        if (isSignatureVerified) {
            System.out.println("ProcessingCancelCaddy 3");
            RequestCancelCaddy requestCancelCaddy = (RequestCancelCaddy)CryptoHelper.deserializeObject(decryptedRequest, logger);
            System.out.println("ProcessingCancelCaddy 4");
            ResponseCancelCaddy responseCancelCaddy = (ResponseCancelCaddy)pBSPP.processingRequest(requestCancelCaddy, socket);
            System.out.println("ProcessingCancelCaddy 5");
            byte[] HMAC = CryptoHelper.createHMAC(convertToBytes(responseCancelCaddy, logger), storedSessionKey.get(socket), logger);
            System.out.println("ProcessingCancelCaddy 6");
            return new ResponseSecure(responseCancelCaddy, null, null, null, null, HMAC);
        }
        System.out.println("ProcessingCancelCaddy 7");
        return new ResponseSecure(new ResponseCancelCaddy(false), null, null, null, null, null);
    }

    private synchronized Response ProcessingPayCaddy(RequestSecure request, Socket socket) throws EndConnexionException {
        byte[] decryptedRequest = CryptoHelper.DecryptAsymRSA(certHelper.getPrivateKey(logger), request.getRequestEncrypted(), logger);

        boolean isSignatureVerified = CryptoHelper.verifySignature(decryptedRequest, request.getSignature(), storedCertificate.get(socket).getPublicKey(), logger);

        if (isSignatureVerified) {
            RequestPayCaddy requestPayCaddy = (RequestPayCaddy)CryptoHelper.deserializeObject(decryptedRequest, logger);
            ResponsePayCaddy responsePayCaddy = (ResponsePayCaddy)pBSPP.processingRequest(requestPayCaddy, socket);

            byte[] HMAC = CryptoHelper.createHMAC(convertToBytes(responsePayCaddy, logger), storedSessionKey.get(socket), logger);


            return new ResponseSecure(responsePayCaddy, null, null, null, null, HMAC);
        }

        return new ResponseSecure(new ResponseCancelCaddy(false), null, null, null, null, null);
    }

    private synchronized ResponseSecure ProcessingEmptyCaddy(RequestSecure request, Socket socket) throws EndConnexionException {

        byte [] decryptedRequest = CryptoHelper.DecryptSymDESede(storedSessionKey.get(socket), request.getRequestEncrypted(), logger);

        RequestEmptyCaddy requestEmptyCaddy = (RequestEmptyCaddy)CryptoHelper.deserializeObject(decryptedRequest, logger);

        ResponseEmptyCaddy requestDeleteCaddyItem = (ResponseEmptyCaddy)pBSPP.processingRequest(requestEmptyCaddy, socket);

        byte[] HMAC = CryptoHelper.createHMAC(convertToBytes(requestDeleteCaddyItem, logger), storedSessionKey.get(socket), logger);

        return new ResponseSecure(requestDeleteCaddyItem, null, null, null, null, HMAC);
    }
}

