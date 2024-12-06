package be.hepl;

import be.hepl.trace.Logger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.*;
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

    public static byte[] generateDigest(int clientNumber, byte[] salt, Logger logger) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.trace("Error while creating the digest");
        }
        if (digest != null) {
            digest.update(salt); // Ajouter le sel
            digest.update(Integer.toString(clientNumber).getBytes()); // Ajouter le numéro de client
            return digest.digest(); // Calculer le hachage
        }
        return null;
    }

    public static byte[] generateDigest(byte[] data, Logger logger) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.trace("Error while creating the digest");
        }
        if (digest != null) {
            return digest.digest(data); // Calculer le hachage
        }
        return null;
    }

    public static byte[] CryptSymDESede(SecretKey cle, byte[] data, Logger logger, byte[] iv)
    {
        Cipher chiffrementE = null;
        try {
            chiffrementE = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle, ivSpec);

            return chiffrementE.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            logger.trace("Error while creating the cipher");
        }
        return null;
    }

    public static byte[] DecryptSymDESede(SecretKey cle, byte[] data, Logger logger, byte[] iv)
    {
        Cipher chiffrementD = null;
        try {
            chiffrementD = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            chiffrementD.init(Cipher.DECRYPT_MODE, cle, ivSpec);

            return chiffrementD.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
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

    public static byte[] convertToBytes(Object obj, Logger logger) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = null;
        try {
            objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(obj);
            objStream.flush();
            return byteStream.toByteArray();
        } catch (IOException e) {
            logger.trace("Error while converting the object to bytes");
        }
        return null;
    }

    public static byte[] signData(byte[] data, PrivateKey privateKey, Logger logger) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA", "BC");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            logger.trace("Error while signing the data");
        }
        return null;
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}