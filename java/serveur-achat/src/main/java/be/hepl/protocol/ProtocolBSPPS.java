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
    private final HashMap<Socket, Integer> connectedClients = new HashMap<>();
    private final HashMap<Socket, byte[]> storedSalt = new HashMap<>();
    private Logger logger;

    @Override
    public String getNom() {
        return "ProtocolBSPPS";
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

        Integer idClient = connectedClients.get(socket);

        if(requestInSecure instanceof RequestAddCaddyItem) {
            return ProcessingAddCaddyItem((RequestSecure) request, idClient);
        }
        if(requestInSecure instanceof RequestDeleteCaddyItem) {
            return ProcessingDeleteCaddyItem((RequestSecure) request);
        }
        if(requestInSecure instanceof RequestCancelCaddy) {
            return ProcessingCancelCaddy((RequestSecure) request);
        }
        if(requestInSecure instanceof RequestPayCaddy) {
            return ProcessingPayCaddy((RequestSecure) request);
        }
        if(requestInSecure instanceof RequestEmptyCaddy) {
            return ProcessingEmptyCaddy((RequestSecure) request);
        }
        return null;
    }

    private synchronized Response ProcessingClient(RequestSecure request, Socket socket) throws EndConnexionException {
        ProtocolBSPP pBSPP = new ProtocolBSPP();

        RequestClient requestClient = (RequestClient)request.getRequest();

        ResponseClient response = (ResponseClient)pBSPP.processingRequest(requestClient, socket);

        if(!response.isValide()) {
            return response;
        }

        byte[] salt = CryptoHelper.generateSalt();

        storedSalt.put(socket, salt);

        return new ResponseSecure(response, null, salt, null, null);
    }

    private synchronized Response ProcessingDigestAndSessionKey(RequestSecure request, Socket socket) throws EndConnexionException {

        byte[] recalculatedDigest = CryptoHelper.generateDigest(connectedClients.get(socket), storedSalt.get(socket), logger);

        // Check if the digest is valid
        boolean isValid = Arrays.equals(recalculatedDigest, request.getDigest());

        if (isValid) {
            // Create the session key
            KeyGenerator genKey = null;
            try {
                genKey = KeyGenerator.getInstance("DESede","BC");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                logger.trace("Error while creating the key generator");
            }
            if (genKey != null) {
                genKey.init(new SecureRandom());
                SecretKey sessionKey = genKey.generateKey();

                byte[] cleSessionCrypte = CryptoHelper.CryptAsymRSA(CertificateHelper.getPublicKey(logger), sessionKey.getEncoded(), logger);

                ResponseSuccess responseValid = new ResponseSuccess(true);
                return new ResponseSecure(responseValid, null, null, cleSessionCrypte, null);
            }

        }

        ResponseSuccess responseNoValid = new ResponseSuccess(false);
        return new ResponseSecure(responseNoValid, null, null, null, null);
    }

    private synchronized ResponseSelectBook ProcessingSelectBook(RequestSelectBook request, Socket socket) throws EndConnexionException {
        ProtocolBSPP pBSPP = new ProtocolBSPP();

        return (ResponseSelectBook)pBSPP.processingRequest(request, socket);
    }

    private synchronized ResponseAddCaddyItem ProcessingAddCaddyItem(RequestSecure request, Integer clientId) {

        return null;
    }

    private synchronized ResponseDeleteCaddyItem ProcessingDeleteCaddyItem(RequestSecure request) {

        return null;
    }

    private synchronized ResponseCancelCaddy ProcessingCancelCaddy(RequestSecure request) throws EndConnexionException {

        return null;
    }

    private synchronized ResponsePayCaddy ProcessingPayCaddy(RequestSecure request) {

        return null;
    }

    private synchronized ResponseEmptyCaddy ProcessingEmptyCaddy(RequestSecure request) {

        return null;
    }
}

