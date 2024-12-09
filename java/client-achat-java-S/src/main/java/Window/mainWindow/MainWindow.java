package Window.mainWindow;

import Window.mainWindow.helper.ButtonManager;
import Window.mainWindow.helper.TableManager;
import Window.mainWindow.helper.buttonSearch;
import Window.mainWindow.helper.initClear;
import Window.table.BookModelAchatClient;
import Window.MySocket;
import Window.ConnectClient;
import be.hepl.CertificateHelper;
import be.hepl.CryptoHelper;
import be.hepl.model.entity.Book;
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
import java.net.Socket;
import java.security.PrivateKey;
import java.util.Arrays;

public class MainWindow extends JFrame{
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private JTable tableStock;
    private JTable tableBasket;
    private JButton ajouterButton;
    private JButton viderLePanierButton;
    private JButton supprimerButton;
    private JButton payerButton;
    private JButton annulerButton;
    private ButtonManager buttonManager;
    private JTextField textFieldTitle;
    private JTextField textFieldSubject;
    private JTextField textFieldLastNameAuthor;
    private JTextField textFieldFirstNameAuthor;
    private JSpinner spinnerMaxPrice;
    private JSpinner spinnerMaxPageCount;
    private JSpinner spinnerQuantite;
    private JButton rechercheButton;
    private JButton xButtonTitle;
    private JButton xButtonLastName;
    private JButton xButtonMaxPrice;
    private JButton xButtonSubject;
    private JButton xButtonFirstName;
    private JButton xButtonMaxPageCount;
    private JPanel MainPanel;
    private JLabel labelTitle;
    private JLabel labelLastName;
    private JLabel labelSubject;
    private JLabel labelFirstName;
    private JLabel labelPageCount;
    private JLabel labelMaxPrice;
    private SecretKey sessionKey;
    private TableManager tableManager;
    public MainWindow(Integer idClient, SecretKey sessionKey) throws IOException {

        MySocket myInstance = MySocket.getInstance();


        this.sessionKey = sessionKey;
        this.socket = myInstance.getMySocket();
        this.oos = myInstance.getOutputStream();
        this.ois = myInstance.getInputStream();
        CertificateHelper clientHelper = new CertificateHelper("./client.jks", "PassClient", "client");

        setContentPane(MainPanel);
        setTitle("MainWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        tableManager = new TableManager(oos, ois);
        initClear helper = new initClear(this);
        helper.initClearButton(xButtonTitle, textFieldTitle, labelTitle);
        helper.initClearButton(xButtonSubject, textFieldSubject, labelSubject);
        helper.initClearButton(xButtonLastName, textFieldLastNameAuthor, labelLastName);
        helper.initClearButton(xButtonFirstName, textFieldFirstNameAuthor, labelFirstName);
        helper.initClearButtonForSpinner(xButtonMaxPrice, spinnerMaxPrice, labelMaxPrice);
        helper.initClearButtonForSpinner(xButtonMaxPageCount, spinnerMaxPageCount, labelPageCount);
        tableManager.initTable(tableStock);
        tableManager.clearBooksTable(tableBasket);
        buttonManager = new ButtonManager();
        buttonVisibility();
        tableStock.setFillsViewportHeight(true);
        buttonSearch buttonSearch = new buttonSearch();
        buttonSearch.buttonSearch(rechercheButton, textFieldTitle, textFieldSubject, textFieldFirstNameAuthor, textFieldLastNameAuthor, spinnerMaxPageCount, spinnerMaxPrice, oos, ois, tableManager, tableStock);

        ajouterButton.addActionListener(new ActionListener() {
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


                    boolean stockUpdated = updateBookStock(selectedBook, selectedQuantity);
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
        });
        payerButton.addActionListener(new ActionListener() {
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
                            dispose();
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
        });

        annulerButton.addActionListener(new ActionListener() {
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
                                dispose();
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
        });

        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBasket.getSelectedRow();

                if (selectedRow != -1) {
                    BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
                    Book bookInBasket = basketModel.getBook(selectedRow);
                    int quantityInBasket = bookInBasket.getStockQuantity();
                    boolean stockRestored = restoreBookStock(bookInBasket, quantityInBasket);
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
        });

        viderLePanierButton.addActionListener(new ActionListener() {
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
        });

        setVisible(true);
    }
    private boolean updateBookStock(Book book, int quantity) {
        try {
            RequestAddCaddyItem request = new RequestAddCaddyItem(book.getId(), quantity);
            byte[] requestBytes = CryptoHelper.convertToBytes(request, null);
            if (requestBytes == null || requestBytes.length == 0) {System.out.println("requestBytes is null or empty");}
            byte[] Iv = CryptoHelper.generateIV();
            if (sessionKey == null) {System.out.println("sessionKey is null");}
            byte [] cryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);
            if (cryptedRequest == null || cryptedRequest.length == 0) {System.out.println("cryptedRequest is null or empty");}

            RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, null, Iv);
            oos.writeObject(initialSecureRequest);
            ResponseAddCaddyItem response = (ResponseAddCaddyItem) ois.readObject();

            if (response.isSuccess()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Échec de la mise à jour du stock : ", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException ex) {

            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du stock : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }
    private boolean restoreBookStock(Book book, int quantity) {
        try {
            RequestDeleteCaddyItem request = new RequestDeleteCaddyItem(book.getId(), quantity);
            byte[] requestBytes = CryptoHelper.convertToBytes(request, null);
            if (requestBytes == null || requestBytes.length == 0)
            {
                System.out.println("requestBytes is null or empty");
            }

            byte[] Iv = CryptoHelper.generateIV();
            byte [] cryptedRequest = CryptoHelper.CryptSymDESede(sessionKey, requestBytes, null, Iv);
            RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, null, Iv);
            oos.writeObject(initialSecureRequest);

            ResponseDeleteCaddyItem response = (ResponseDeleteCaddyItem) ois.readObject();

            return response.isSuccess();

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la restauration du stock : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
public void buttonVisibility() {
        buttonManager.buttonVisibility(
                textFieldTitle, xButtonTitle, labelTitle,
                textFieldSubject, xButtonSubject, labelSubject,
                textFieldLastNameAuthor, xButtonLastName, labelLastName,
                textFieldFirstNameAuthor, xButtonFirstName, labelFirstName,
                spinnerMaxPrice, xButtonMaxPrice, labelMaxPrice,
                spinnerMaxPageCount, xButtonMaxPageCount, labelPageCount
        );
    }
}