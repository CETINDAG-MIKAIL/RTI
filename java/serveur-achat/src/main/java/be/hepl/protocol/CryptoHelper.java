package be.hepl.protocol;

import be.hepl.trace.Logger;

import javax.crypto.*;
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

    public static byte[] CryptSymDES(SecretKey cle, byte[] data, Logger logger)
    {
        Cipher chiffrementE = null;
        try {
            chiffrementE = Cipher.getInstance("DES/ECB/PKCS5Padding", "BC");
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle);

            return chiffrementE.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.trace("Error while creating the cipher");
        }
        return null;
    }

    public static byte[] DecryptSymDES(SecretKey cle, byte[] data, Logger logger)
    {
        try {
            Cipher chiffrementD = Cipher.getInstance("DES/ECB/PKCS5Padding", "BC");
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
}