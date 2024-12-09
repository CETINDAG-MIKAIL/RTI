package Window.mainWindow.mainWindowHelper;

import Window.ConnectClient;
import Window.MySocket;
import Window.mainWindow.MainWindow;
import Window.table.*;
import be.hepl.CertificateHelper;
import be.hepl.CryptoHelper;
import be.hepl.model.entity.*;
import be.hepl.request.*;
import be.hepl.response.*;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.util.Arrays;

public class mainListeners {
    private MainWindow mainWindow;
    private MySocket mySocket;
    private SecretKey sessionKey;
    private CertificateHelper clientHelper;
    private JTable tableStock;
    private JTable tableBasket;
    private JSpinner spinnerQuantite;
    private int idClient;
    public Window.mainWindow.mainWindowHelper.tableManager tableManager;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Window.mainWindow.mainWindowHelper.stockHelper stockHelper;


    public mainListeners(MainWindow mainWindow, JTable tableStock, JTable tableBasket, JSpinner spinnerQuantite, int idClient, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        this.mainWindow = mainWindow;
        this.mySocket = MySocket.getInstance();
        this.sessionKey = mainWindow.sessionKey;
        this.clientHelper = new CertificateHelper("./client.jks", "PassClient", "client");
        this.tableStock = tableStock;
        this.tableBasket = tableBasket;
        this.spinnerQuantite = spinnerQuantite;
        this.idClient = idClient;
        this.oos = oos;
        this.ois = ois;
        tableManager = new tableManager(oos, ois);
        this.stockHelper = new stockHelper(sessionKey, mySocket, oos, ois);

    }

    public ActionListener ajouterButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableStock.getSelectedRow();
                if (selectedRow != -1) {
                    Book selectedBook = ((BookModelAchatClient) tableStock.getModel()).getBook(selectedRow);
                    int selectedQuantity = (Integer) spinnerQuantite.getValue();

                    if(selectedQuantity < 1) {
                        JOptionPane.showMessageDialog(null,
                                "Aucune quantité mise.",
                                "Quantité non disponible", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if(selectedBook.getStockQuantity() < 1) {
                        JOptionPane.showMessageDialog(null,
                                "Aucune quantité disponible en stock (" + selectedBook.getStockQuantity() + " en stock).",
                                "Quantité non disponible", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (selectedQuantity > selectedBook.getStockQuantity()) {
                        JOptionPane.showMessageDialog(null,
                                "Quantité demandée supérieure au stock disponible (" + selectedBook.getStockQuantity() + " en stock).",
                                "Quantité non disponible", JOptionPane.WARNING_MESSAGE);
                        return;
                    }


                    boolean stockUpdated = stockHelper.updateBookStock(selectedBook, selectedQuantity);
                    if (stockUpdated) {

                        BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
                        basketModel.addRow(selectedBook, selectedQuantity);


                        selectedBook.setStockQuantity(selectedBook.getStockQuantity() - selectedQuantity);
                        ((AbstractTableModel) tableStock.getModel()).fireTableRowsUpdated(selectedRow, selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Impossible de mettre à jour le stock. Veuillez réessayer.",
                                "Erreur de stock", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un livre à ajouter au panier.",
                            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
    }

    public ActionListener payerButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
                float mountTotal = 0;
                for (int i = 0; i < basketModel.getRowCount(); i++) {
                    Book bookInBasket = basketModel.getBook(i);
                    mountTotal += bookInBasket.getStockQuantity() * bookInBasket.getPrice();
                }

                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Paiement de " + mountTotal + "€ réalisé ?",
                        "Confirmation de paiement",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    try {

                        String cardName = JOptionPane.showInputDialog("Entrez le nom de la carte VISA :");
                        if (cardName == null || cardName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Le nom de la carte est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String cardNumberString = JOptionPane.showInputDialog("Entrez le numéro de la carte VISA :");
                        if (cardNumberString == null || cardNumberString.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Le numéro de carte est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        int cardNumber;
                        try {
                            cardNumber = Integer.parseInt(cardNumberString);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Le numéro de carte doit être un entier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        RequestPayCaddyWithVisa request = new RequestPayCaddyWithVisa(idClient, cardNumber, cardName);
                        byte[] requestBytes = CryptoHelper.convertToBytes(request, null);

                        if (requestBytes == null || requestBytes.length == 0) {
                            System.out.println("Erreur : La conversion de la requête en octets a échoué.");
                            return;
                        }
                        byte[] Iv = CryptoHelper.generateIV();
                        byte[] encryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);
                        if (encryptedRequest == null) {
                            System.out.println("Erreur : Chiffrement de la requête échoué.");
                            return;
                        }

                        PrivateKey privateKey = clientHelper.getPrivateKey(null);
                        byte[] signature = CryptoHelper.signData(requestBytes, privateKey, null);
                        if (signature == null) {
                            System.out.println("Erreur : La signature des données a échoué.");
                            return;
                        }
                        RequestSecure secureRequest = new RequestSecure(request, encryptedRequest, null, null, signature, Iv);

                        oos.writeObject(secureRequest);

                        ResponseSecure responseSecure = (ResponseSecure) ois.readObject();
                        ResponsePayCaddy response = (ResponsePayCaddy) responseSecure.getResponse();
                        byte[] serverHMAC = responseSecure.getHmac();
                        byte[] recalculatedHMAC = CryptoHelper.createHMAC(CryptoHelper.convertToBytes(response, null), sessionKey, null);
                        boolean isHMACValid = Arrays.equals(serverHMAC, recalculatedHMAC);

                        if (isHMACValid) {
                            JOptionPane.showMessageDialog(null, "Paiement effectué avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            mainWindow.dispose();
                            MySocket.closeSocket();
                            ConnectClient connectClientDialog = new ConnectClient();
                            connectClientDialog.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors du paiement. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors du paiement : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Paiement annulé.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
    }

    public ActionListener annulerButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Demande de deconnexion du client ?",
                        "Deconnexion",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        PrivateKey privateKey = clientHelper.getPrivateKey(null); // Pour signer

                        RequestCancelCaddy request = new RequestCancelCaddy(idClient);
                        byte[] requestBytes = CryptoHelper.convertToBytes(request, null);

                        if (requestBytes == null || requestBytes.length == 0) {System.out.println("requestBytes is null or empty");}

                        byte[] signature = CryptoHelper.signData(requestBytes, privateKey, null);

                        byte[] Iv = CryptoHelper.generateIV();
                        byte [] cryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);

                        RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, signature, Iv);

                        oos.writeObject(initialSecureRequest);
                        Object object = ois.readObject();
                        ResponseSecure responseSecure = (ResponseSecure) object;
                        if (responseSecure.getResponse() instanceof ResponseCancelCaddy) {
                            ResponseCancelCaddy responseCancelCaddy = (ResponseCancelCaddy) responseSecure.getResponse();

                            byte[] serverHMAC = responseSecure.getHmac();
                            byte[] recalculatedHMAC = CryptoHelper.createHMAC(CryptoHelper.convertToBytes(responseCancelCaddy, null), sessionKey, null);
                            boolean isHMACValid = Arrays.equals(serverHMAC, recalculatedHMAC);

                            if (isHMACValid) {
                                JOptionPane.showMessageDialog(null, "Déconnexion effectuée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                                mainWindow.dispose();
                                MySocket.closeSocket();
                                ConnectClient connectClientDialog = new ConnectClient();
                                connectClientDialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erreur lors de la déconnexion. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Réponse invalide reçue du serveur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur le la déconnexion : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Déconnexion annulé.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
    }
    public ActionListener supprimerButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBasket.getSelectedRow();

                if (selectedRow != -1) {
                    BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
                    Book bookInBasket = basketModel.getBook(selectedRow);
                    int quantityInBasket = bookInBasket.getStockQuantity();
                    boolean stockRestored = stockHelper.restoreBookStock(bookInBasket, quantityInBasket);
                    if (stockRestored) {
                        basketModel.removeRow(selectedRow);
                        for (int i = 0; i < tableStock.getRowCount(); i++) {
                            Book bookInStock = ((BookModelAchatClient) tableStock.getModel()).getBook(i);
                            if (bookInStock.getId() == bookInBasket.getId()) {
                                bookInStock.setStockQuantity(bookInStock.getStockQuantity() + quantityInBasket);
                                ((AbstractTableModel) tableStock.getModel()).fireTableRowsUpdated(i, i);
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    public ActionListener viderLePanierButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez-vous vraiment vider le panier ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
                        for (int i = 0; i < basketModel.getRowCount(); i++) {
                            Book bookInBasket = basketModel.getBook(i);
                            int quantityInBasket = bookInBasket.getStockQuantity();

                            for (int j = 0; j < tableStock.getRowCount(); j++) {
                                Book bookInStock = ((BookModelAchatClient) tableStock.getModel()).getBook(j);
                                if (bookInStock.getId() == bookInBasket.getId()) {
                                    bookInStock.setStockQuantity(bookInStock.getStockQuantity() + quantityInBasket);
                                    ((AbstractTableModel) tableStock.getModel()).fireTableRowsUpdated(j, j); // permet de notifier a la table qu'il a du changement et on mets a jour a l'index j
                                    break;
                                }
                            }
                        }

                        RequestEmptyCaddy request = new RequestEmptyCaddy(idClient);
                        byte[] requestBytes = CryptoHelper.convertToBytes(request, null);
                        if (requestBytes == null || requestBytes.length == 0) {
                            System.out.println("Erreur : La conversion de la requête en octets a échoué.");
                            return;
                        }

                        byte[] Iv = CryptoHelper.generateIV();
                        byte[] encryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);
                        if (encryptedRequest == null) {
                            System.out.println("Erreur : Chiffrement de la requête échoué.");
                            return;
                        }

                        RequestSecure secureRequest = new RequestSecure(request, encryptedRequest, null, null, null, Iv);
                        oos.writeObject(secureRequest);
                        ResponseSecure responseSecure = (ResponseSecure) ois.readObject();
                        ResponseEmptyCaddy response = (ResponseEmptyCaddy) responseSecure.getResponse();
                        byte[] serverHMAC = responseSecure.getHmac();
                        byte[] recalculatedHMAC = CryptoHelper.createHMAC(CryptoHelper.convertToBytes(response, null), sessionKey, null);
                        boolean isHMACValid = Arrays.equals(serverHMAC, recalculatedHMAC);

                        if (isHMACValid) {
                            tableManager.clearBooksTable(tableBasket);

                            JOptionPane.showMessageDialog(null, "Le panier a été vidé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors du vidage du panier. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur de communication avec le serveur : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
    }
}
