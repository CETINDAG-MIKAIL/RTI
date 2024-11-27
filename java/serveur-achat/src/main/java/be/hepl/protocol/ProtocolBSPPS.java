package be.hepl.protocol;

import be.hepl.myException.EndConnexionException;
import be.hepl.request.*;
import be.hepl.response.*;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ProtocolBSPPS implements Protocol {
    private final ProtocolBSPP pBSPP = new ProtocolBSPP();
    private final HashMap<Socket, byte[]> storedSalt = new HashMap<>();
    private final HashMap<Socket, SecretKey> storedSessionKey = new HashMap<>();
    private final HashMap<Socket, X509Certificate> storedCertificate = new HashMap<>();
    private Logger logger;
    

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

        if(request instanceof RequestSelectBook) {
            return ProcessingSelectBook((RequestSelectBook) request, socket);
        }


        Request requestInSecure;
        if(request instanceof RequestSecure) {
            if(((RequestSecure) request).getCertificate() != null && ((RequestSecure) request).getDigest() != null) {
                return ProcessingDigestAndSessionKey((RequestSecure) request, socket);
            }
            requestInSecure = ((RequestSecure) request).getRequest();
        } else {
            return null;
        }

        if(requestInSecure instanceof RequestClient) {
            return ProcessingClient((RequestSecure) request, socket);
        }

        Integer idClient = getConnectedClients().get(socket);

        if(requestInSecure instanceof RequestAddCaddyItem) {
            return ProcessingAddCaddyItem((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestDeleteCaddyItem) {
            return ProcessingDeleteCaddyItem((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestCancelCaddy) {
            return ProcessingCancelCaddy((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestPayCaddy) {
            return ProcessingPayCaddy((RequestSecure) request, socket);
        }
        if(requestInSecure instanceof RequestEmptyCaddy) {
            return ProcessingEmptyCaddy((RequestSecure) request, socket);
        }
        return null;
    }

    private synchronized Response ProcessingClient(RequestSecure request, Socket socket) throws EndConnexionException {
        ProtocolBSPP pBSPP = new ProtocolBSPP();

        RequestClient requestClient = (RequestClient)request.getRequest();

        ResponseClient response = (ResponseClient)pBSPP.processingRequest(requestClient, socket);

        if(!response.isValide()) {
            return new ResponseSecure(null, null, null, null, null, null);
        }

        byte[] salt = CryptoHelper.generateSalt();

        storedSalt.put(socket, salt);

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
                X509Certificate certificate = CertificateHelper.getCertificate(logger);
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

        byte [] decryptedRequest = CryptoHelper.DecryptSymDESede(storedSessionKey.get(socket), request.getRequestEncrypted(), logger);

        RequestAddCaddyItem requestAddCaddyItem = (RequestAddCaddyItem)CryptoHelper.deserializeObject(decryptedRequest, logger);

        return (ResponseAddCaddyItem)pBSPP.processingRequest(requestAddCaddyItem, socket);
    }

    private synchronized ResponseDeleteCaddyItem ProcessingDeleteCaddyItem(RequestSecure request, Socket socket) throws EndConnexionException {

        byte [] decryptedRequest = CryptoHelper.DecryptSymDESede(storedSessionKey.get(socket), request.getRequestEncrypted(), logger);

        RequestDeleteCaddyItem requestDeleteCaddyItem = (RequestDeleteCaddyItem)CryptoHelper.deserializeObject(decryptedRequest, logger);

        return (ResponseDeleteCaddyItem)pBSPP.processingRequest(requestDeleteCaddyItem, socket);
    }

    private synchronized Response ProcessingCancelCaddy(RequestSecure request, Socket socket) throws EndConnexionException {

        byte [] decryptedRequest = CryptoHelper.DecryptAsymRSA(CertificateHelper.getPrivateKey(logger), request.getRequestEncrypted(), logger);

        //byte[] decryptedDigest = CryptoHelper.DecryptAsymRSA(storedCertificate.get(socket).getPublicKey(), request.getSignature(), logger);

        //byte[] recalculatedDigest = CryptoHelper.generateDigest(decryptedRequest, logger);

        boolean isSignatureVerified = CryptoHelper.verifySignature(decryptedRequest, request.getSignature(), storedCertificate.get(socket).getPublicKey(), logger);

        // Check if the digest is valid, if they are the same, the signature is valid
        //boolean isSameDigest = Arrays.equals(recalculatedDigest, decryptedDigest);

        if (isSignatureVerified) {
            byte[] HMAC = CryptoHelper.createHMAC(decryptedRequest, storedSessionKey.get(socket), logger);

            RequestCancelCaddy requestCancelCaddy = (RequestCancelCaddy)CryptoHelper.deserializeObject(decryptedRequest, logger);
            ResponseCancelCaddy responseCancelCaddy = (ResponseCancelCaddy)pBSPP.processingRequest(requestCancelCaddy, socket);

            return new ResponseSecure(responseCancelCaddy, null, null, null, null, HMAC);
        }

        return new ResponseSecure(new ResponseCancelCaddy(false), null, null, null, null, null);
    }

    private synchronized Response ProcessingPayCaddy(RequestSecure request, Socket socket) throws EndConnexionException {
        byte [] decryptedRequest = CryptoHelper.DecryptAsymRSA(CertificateHelper.getPrivateKey(logger), request.getRequestEncrypted(), logger);

        boolean isSignatureVerified = CryptoHelper.verifySignature(decryptedRequest, request.getSignature(), storedCertificate.get(socket).getPublicKey(), logger);

        if (isSignatureVerified) {
            byte[] HMAC = CryptoHelper.createHMAC(decryptedRequest, storedSessionKey.get(socket), logger);

            RequestPayCaddy requestPayCaddy = (RequestPayCaddy)CryptoHelper.deserializeObject(decryptedRequest, logger);
            ResponsePayCaddy responsePayCaddy = (ResponsePayCaddy)pBSPP.processingRequest(requestPayCaddy, socket);

            return new ResponseSecure(responsePayCaddy, null, null, null, null, HMAC);
        }

        return new ResponseSecure(new ResponseCancelCaddy(false), null, null, null, null, null);
    }

    private synchronized ResponseEmptyCaddy ProcessingEmptyCaddy(RequestSecure request, Socket socket) {

        return null;
    }
}

