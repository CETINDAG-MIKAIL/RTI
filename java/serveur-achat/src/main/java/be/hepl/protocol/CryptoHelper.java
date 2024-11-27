package be.hepl.protocol;

import be.hepl.trace.Logger;
import com.mysql.cj.log.Log;

import javax.crypto.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.*;
import java.util.Date;

import static com.mysql.cj.conf.PropertyKey.logger;

public class CryptoHelper {
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // add the current date
        long currentDate = new Date().getTime();
        byte[] dateBytes = Long.toString(currentDate).getBytes();

        // merge tables
        byte[] finalSalt = new byte[salt.length + dateBytes.length];
        System.arraycopy(salt, 0, finalSalt, 0, salt.length);
        System.arraycopy(dateBytes, 0, finalSalt, salt.length, dateBytes.length);

        return finalSalt;
    }

    public static byte[] generateDigest(int clientNumber, byte[] salt, Logger logger) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.trace("Error while creating the digest");
        }
        digest.update(salt); // Ajouter le sel
        digest.update(Integer.toString(clientNumber).getBytes()); // Ajouter le numéro de client
        return digest.digest(); // Calculer le hachage
    }

    public static byte[] generateDigest(byte[] data, Logger logger) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.trace("Error while creating the digest");
        }
        return digest.digest(data); // Calculer le hachage
    }

    public static byte[] CryptSymDESede(SecretKey cle, byte[] data, Logger logger)
    {
        Cipher chiffrementE = null;
        try {
            chiffrementE = Cipher.getInstance("DESede/ECB/PKCS5Padding", "BC");
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle);

            return chiffrementE.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.trace("Error while creating the cipher");
        }
        return null;
    }

    public static byte[] DecryptSymDESede(SecretKey cle, byte[] data, Logger logger)
    {
        try {
            Cipher chiffrementD = Cipher.getInstance("DESede/ECB/PKCS5Padding", "BC");
            chiffrementD.init(Cipher.DECRYPT_MODE, cle);
            return chiffrementD.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.trace("Error while creating the cipher");
        }
        return null;
    }

    public static byte[] CryptAsymRSA(PublicKey cle, byte[] data, Logger logger)
    {
        try {
        Cipher chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.trace("Error while creating the cipher");
        }
        return null;
    }

    public static byte[] DecryptAsymRSA(PrivateKey cle, byte[] data, Logger logger)
    {
        try {
        Cipher chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.trace("Error while creating the cipher");
        }
        return null;
    }

    public static Object deserializeObject(byte[] data, Logger logger) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.trace("Error while deserializing the object");
        }
        return null;
    }

    public static byte[] createHMAC(byte[] data, SecretKey key, Logger logger) {
        try {
            Mac mac = Mac.getInstance("HMAC-MD5", "BC");
            mac.init(key);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
            logger.trace("Error while creating the HMAC");
        }
        return null;
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey, Logger logger) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA", "BC");
            sign.initVerify(publicKey);
            sign.update(data);
            return sign.verify(signature);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            logger.trace("Error while verifying the signature");
        }
        return false;
    }
}