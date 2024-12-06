package be.hepl;

import be.hepl.trace.Logger;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CertificateHelper {
    private static final String keystoreFile = "./server.jks";
    private static final String keystorePassword = "admin";
    private static final String certificateAlias = "server";

    public static synchronized X509Certificate getCertificate(Logger logger) {
        try {
            // Load the keystore
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(keystoreFile);
            ks.load(fis, keystorePassword.toCharArray());
            fis.close();

            // Check the aliases
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();

                if (alias.equals(certificateAlias)) {
                    return (X509Certificate) ks.getCertificate(alias);
                }
            }
        } catch (Exception e) {
            logger.trace("Error while getting the certificate");
        }
        return null;
    }

    public static PublicKey getPublicKey(Logger logger) {
        X509Certificate certificate = getCertificate(logger);
        if (certificate == null) {
            return null;
        }
        return certificate.getPublicKey();
    }

    public static synchronized PrivateKey getPrivateKey(Logger logger) {
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
                            PrivateKey privateKey = (PrivateKey) key;
                            return privateKey;
                        } else {
                            logger.trace("The alias does not correspond to a private key entry.");
                        }
                    } else {
                        logger.trace("The alias does not correspond to a key entry.");
                    }
                }
            }
        } catch (Exception e) {
            logger.trace("Error while getting the private key");
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            // Load the keystore
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(keystoreFile);
            ks.load(fis, keystorePassword.toCharArray());
            fis.close();

            // Check the aliases
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                System.out.println("Alias trouvé : " + alias);

                // Rechercher l'alias correspondant
                if (alias.equals(certificateAlias)) {
                    if (ks.isKeyEntry(alias))
                        System.out.println("[keyEntry] --> " + alias);

//                    if (ks.isCertificateEntry(alias)) {
//                        System.out.println("[trustedCertificateEntry] --> " + alias);

                        // Get the certificate
                    X509Certificate certif = (X509Certificate) ks.getCertificate(alias);
                    System.out.println("\tType de certificat : " + certif.getType());
                    System.out.println("\tNom du propriétaire du certificat : " + certif.getSubjectDN().getName());

                    // Get the public key
                    PublicKey clePublique = certif.getPublicKey();
                    System.out.println("\tClé publique récupérée = " + clePublique);
//                    } else {
//                        System.out.println("L'alias spécifié n'est pas un trustedCertificateEntry.");
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String args[])
//    {
////            Security.addProvider(new BouncyCastleProvider());
////            Provider prov[] = Security.getProviders();
////            for (int i=0; i<prov.length; i++)
////                System.out.println(prov[i].getName() + "/" + prov[i].getVersion());
////
//        Security.addProvider(new BouncyCastleProvider());
//
//        Provider prov[] = Security.getProviders();
//        for (int i=0; i<prov.length; i++)
//            System.out.println(prov[i].getName() + "/" + prov[i].getVersion());
//
//    }
}
