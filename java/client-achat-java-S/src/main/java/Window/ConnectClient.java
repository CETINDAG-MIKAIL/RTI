package Window;

import be.hepl.CertificateHelper;
import be.hepl.CryptoHelper;
import be.hepl.request.RequestClient;
import be.hepl.request.RequestSecure;
import be.hepl.response.ResponseSecure;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.MessageDigest;


public class ConnectClient extends JDialog {
    private Socket socket;
    private String login;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLastNameClient;
    private JTextField textFieldFirstNameClient;
    private JTextField textFieldIdClient;
    private Logger logger = MyLogger.getInstance();
    CertificateHelper clientHelper = new CertificateHelper("./client.jks", "PassClient", "client");


    public ConnectClient() throws IOException {
        MySocket myInstance = MySocket.getInstance();

        this.socket = myInstance.getMySocket();
        this.oos = myInstance.getOutputStream();
        this.ois = myInstance.getInputStream();
        setContentPane(contentPane);
        setTitle("Client");
        setSize(300, 250);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String loginLastName = textFieldLastNameClient.getText();
        String loginFirstName = textFieldFirstNameClient.getText();
        String IdClientText = textFieldIdClient.getText();
        boolean newClient = false;

        try {
            RequestClient requete = new RequestClient(loginLastName, loginFirstName, newClient);
            RequestSecure initialSecureRequest = new RequestSecure(requete, null, null, null, null, null);
            oos.writeObject(initialSecureRequest);

            ResponseSecure responseSecure = (ResponseSecure) ois.readObject();

            byte[] sel = responseSecure.getSel();

            if (sel == null) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réception du sel.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (IdClientText == null || IdClientText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un ID valide (entier).", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] clientNumber = IdClientText.getBytes(StandardCharsets.UTF_8);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(sel);
            byte[] clientDigest = digest.digest(clientNumber);

            X509Certificate clientCertificate = clientHelper.getCertificate(logger);
            if (clientCertificate == null) {
                JOptionPane.showMessageDialog(this, "Certificat du client introuvable ou invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RequestSecure secureRequest = new RequestSecure(null, null, clientDigest, clientCertificate, null, null);
            oos.writeObject(secureRequest);

            ResponseSecure finalResponse = (ResponseSecure) ois.readObject();
            byte[] sessionKey = finalResponse.getSessionsKey();

            if (sessionKey != null) {
                byte[] decryptedSessionKey = sessionKey;
                // je decrypte d'abord ensuite je lis la clé
                try {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, clientHelper.getPrivateKey(logger));
                    decryptedSessionKey = cipher.doFinal(sessionKey); // Décryptage de la clé

                } catch (GeneralSecurityException e) {
                    JOptionPane.showMessageDialog(this, "Erreur lors du déchiffrement de la clé de session : " + e.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }

                if (decryptedSessionKey.length == 16 || decryptedSessionKey.length == 24) {
                    SecretKey secretKey = new SecretKeySpec(decryptedSessionKey, "DESede");
                    dispose();
                    int IdClient = Integer.parseInt(IdClientText);
                    new MainWindow(IdClient, secretKey);
                } else {
                    // Si la taille de la clé est incorrecte, on affiche une erreur
                    JOptionPane.showMessageDialog(this, "La taille de la clé de session est invalide : " + decryptedSessionKey.length,
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erreur de login !", "Erreur...", JOptionPane.ERROR_MESSAGE);
                MySocket.closeSocket();
                MySocket myInstance = MySocket.getInstance();
                this.socket = myInstance.getMySocket();
                this.oos = myInstance.getOutputStream();
                this.ois = myInstance.getInputStream();
            }

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Problème de connexion !", "Erreur...", JOptionPane.ERROR_MESSAGE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    private void onCancel() {
        dispose();
    }
}
