package be.hepl;

import be.hepl.trace.Logger;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CertificateHelper {
    private String keystoreFile;
    private String keystorePassword;
    private String certificateAlias;

    public CertificateHelper(String keystoreFile, String keystorePassword, String certificateAlias) {
        this.keystoreFile = keystoreFile;
        this.keystorePassword = keystorePassword;
        this.certificateAlias = certificateAlias;
    }

    public synchronized X509Certificate getCertificate(Logger logger) {
        try {
            // Charger le keystore
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(keystoreFile);
            ks.load(fis, keystorePassword.toCharArray());
            fis.close();

            // Vérifier les alias
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (alias.equals(certificateAlias)) {
                    return (X509Certificate) ks.getCertificate(alias);
                }
            }
        } catch (Exception e) {
            logger.trace("Erreur lors de la récupération du certificat : " + e.getMessage());
        }
        return null;
    }

    public PublicKey getPublicKey(Logger logger) {
        X509Certificate certificate = getCertificate(logger);
        if (certificate == null) {
            logger.trace("Impossible de récupérer la clé publique, le certificat est nul.");
            return null;
        }
        return certificate.getPublicKey();
    }

    public synchronized PrivateKey getPrivateKey(Logger logger) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(keystoreFile);
            ks.load(fis, keystorePassword.toCharArray());
            fis.close();

            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (alias.equals(certificateAlias)) {
                    if (ks.isKeyEntry(alias)) {
                        Key key = ks.getKey(alias, keystorePassword.toCharArray());
                        if (key instanceof PrivateKey) {
                            return (PrivateKey) key;
                        } else {
                            logger.trace("L'alias spécifié ne correspond pas à une clé privée.");
                        }
                    } else {
                        logger.trace("L'alias spécifié ne correspond pas à une entrée de clé.");
                    }
                }
            }
        } catch (Exception e) {
            logger.trace("Erreur lors de la récupération de la clé privée : " + e.getMessage());
        }
        return null;
    }
}
