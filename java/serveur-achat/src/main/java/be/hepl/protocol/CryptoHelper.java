package be.hepl.protocol;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;

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

    public static byte[] generateDigest(int clientNumber, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt); // Ajouter le sel
        digest.update(Integer.toString(clientNumber).getBytes()); // Ajouter le numéro de client
        return digest.digest(); // Calculer le hachage
    }
}
