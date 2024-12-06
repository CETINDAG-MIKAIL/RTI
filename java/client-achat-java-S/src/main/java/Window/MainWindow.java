package Window;

import Window.table.BookColumnModelAchatClient;
import Window.table.BookModelAchatClient;
import be.hepl.CertificateHelper;
import be.hepl.CryptoHelper;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.BookSearchVM;
import be.hepl.request.*;
import be.hepl.response.*;
import be.hepl.trace.Logger;
import be.hepl.trace.MyLogger;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


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

    private final HashMap<Socket, SecretKey> storedSessionKey = new HashMap<>();
    private Logger logger = MyLogger.getInstance();
    public MainWindow(Integer idClient) throws IOException {
        MySocket myInstance = MySocket.getInstance();

        this.socket = myInstance.getMySocket();
        this.oos = myInstance.getOutputStream();
        this.ois = myInstance.getInputStream();
        CertificateHelper clientHelper = new CertificateHelper("./client.jks", "PassClient", "client");


        setContentPane(MainPanel);
        setTitle("MainWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        initClearButton(xButtonTitle, textFieldTitle, labelTitle);
        initClearButton(xButtonSubject, textFieldSubject, labelSubject);
        initClearButton(xButtonLastName, textFieldLastNameAuthor, labelLastName);
        initClearButton(xButtonFirstName, textFieldFirstNameAuthor, labelFirstName);
        initClearButtonForSpinner(xButtonMaxPrice, spinnerMaxPrice, labelMaxPrice);
        initClearButtonForSpinner(xButtonMaxPageCount, spinnerMaxPageCount, labelPageCount);
        initTable();
        buttonSearch(rechercheButton);
        clearBooksTable( tableBasket );
        tableStock.setFillsViewportHeight(true);

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
                        byte[] requestBytes = CryptoHelper.convertToBytes(request, logger);

                        if (requestBytes == null || requestBytes.length == 0) {
                            System.out.println("Erreur : La conversion de la requête en octets a échoué.");
                            return;
                        }
                        byte[] Iv = CryptoHelper.generateIV();
                        byte[] encryptedRequest = CryptoHelper.CryptSymDESede(storedSessionKey.get(socket), requestBytes, logger, Iv);
                        if (encryptedRequest == null) {
                            System.out.println("Erreur : Chiffrement de la requête échoué.");
                            return;
                        }

                        PrivateKey privateKey = clientHelper.getPrivateKey(logger);
                        byte[] signature = CryptoHelper.signData(requestBytes, privateKey, logger);
                        if (signature == null) {
                            System.out.println("Erreur : La signature des données a échoué.");
                            return;
                        }

                        RequestSecure secureRequest = new RequestSecure(request, encryptedRequest, null, null, signature, null);

                        oos.writeObject(secureRequest);

                        ResponseSecure responseSecure = (ResponseSecure) ois.readObject();
                        ResponsePayCaddy response = (ResponsePayCaddy) responseSecure.getResponse();
                        byte[] serverHMAC = responseSecure.getHmac();
                        byte[] recalculatedHMAC = CryptoHelper.createHMAC(CryptoHelper.convertToBytes(response, logger), storedSessionKey.get(socket), logger);
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
                        PrivateKey privateKey = clientHelper.getPrivateKey(logger); // Pour signer

                        RequestCancelCaddy request = new RequestCancelCaddy(idClient);
                        byte[] requestBytes = CryptoHelper.convertToBytes(request, logger);

                        if (requestBytes == null || requestBytes.length == 0) {System.out.println("requestBytes is null or empty");}

                        System.out.println("requestBytes length " + requestBytes.length);
                        byte[] signature = CryptoHelper.signData(requestBytes, privateKey, logger);

                        byte[] Iv = CryptoHelper.generateIV();
                        byte [] cryptedRequest = CryptoHelper.CryptSymDESede(storedSessionKey.get(socket), requestBytes, logger, Iv);

                        RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, signature, null);

                        oos.writeObject(initialSecureRequest);
                        ResponseSecure responseSecure = (ResponseSecure) ois.readObject();
                        ResponseCancelCaddy response = (ResponseCancelCaddy) responseSecure.getResponse();

                        byte[] serverHMAC = responseSecure.getHmac();
                        byte[] recalculatedHMAC = CryptoHelper.createHMAC(CryptoHelper.convertToBytes(response, logger), storedSessionKey.get(socket), logger);
                        boolean isHMACValid = Arrays.equals(serverHMAC, recalculatedHMAC);


                        if (isHMACValid) {
                            JOptionPane.showMessageDialog(null, "Déconnexion effectué avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            dispose();

                            MySocket.closeSocket();

                            ConnectClient connectClientDialog = new ConnectClient();
                            connectClientDialog.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la déconnexion. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
                        byte[] requestBytes = CryptoHelper.convertToBytes(request, logger);
                        if (requestBytes == null || requestBytes.length == 0) {
                            System.out.println("Erreur : La conversion de la requête en octets a échoué.");
                            return;
                        }

                        byte[] Iv = CryptoHelper.generateIV();
                        byte[] encryptedRequest = CryptoHelper.CryptSymDESede(storedSessionKey.get(socket), requestBytes, logger, Iv);
                        if (encryptedRequest == null) {
                            System.out.println("Erreur : Chiffrement de la requête échoué.");
                            return;
                        }

                        RequestSecure secureRequest = new RequestSecure(request, encryptedRequest, null, null, null, null);

                        oos.writeObject(secureRequest);


                        ResponseSecure responseSecure = (ResponseSecure) ois.readObject();
                        ResponseEmptyCaddy response = (ResponseEmptyCaddy) responseSecure.getResponse();
                        byte[] serverHMAC = responseSecure.getHmac();
                        byte[] recalculatedHMAC = CryptoHelper.createHMAC(CryptoHelper.convertToBytes(response, logger), storedSessionKey.get(socket), logger);
                        boolean isHMACValid = Arrays.equals(serverHMAC, recalculatedHMAC);


                        if (isHMACValid) {
                            clearBooksTable(tableBasket);
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

    private void initClearButton(JButton button, JTextField textField, JLabel label) {
        button.setVisible(false);
        button.setEnabled(false);
        addFilterListener(textField, button, label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                buttonVisibility();
            }
        });
    }
    private boolean updateBookStock(Book book, int quantity) {
        try {

            RequestAddCaddyItem request = new RequestAddCaddyItem(book.getId(), quantity);
            byte[] requestBytes = CryptoHelper.convertToBytes(request, logger);
            if (requestBytes == null || requestBytes.length == 0) {System.out.println("requestBytes is null or empty");}
            System.out.println("requestBytes length " + requestBytes.length);

            byte[] Iv = CryptoHelper.generateIV();
            byte [] cryptedRequest = CryptoHelper.CryptSymDESede(storedSessionKey.get(socket), requestBytes, logger, Iv);

            RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, null, null);
            oos.writeObject(initialSecureRequest);


            ResponseAddCaddyItem response = (ResponseAddCaddyItem) ois.readObject();

            if (response.isSuccess()) {
                return true;
            } else {

                JOptionPane.showMessageDialog(null,
                        "Échec de la mise à jour du stock : ",
                        "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
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
            byte[] requestBytes = CryptoHelper.convertToBytes(request, logger);
            if (requestBytes == null || requestBytes.length == 0)
            {
                System.out.println("requestBytes is null or empty");
            }
            System.out.println("requestBytes length " + requestBytes.length);

            byte[] Iv = CryptoHelper.generateIV();
            byte [] cryptedRequest = CryptoHelper.CryptSymDESede(storedSessionKey.get(socket), requestBytes, logger, Iv);
            RequestSecure initialSecureRequest  = new RequestSecure(request, cryptedRequest, null, null, null, null);
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
    private void buttonSearch(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BookSearchVM searchFilters = new BookSearchVM();

                    if (!textFieldTitle.getText().isEmpty()) {
                        searchFilters.setTitle(textFieldTitle.getText());
                    }
                    if (!textFieldSubject.getText().isEmpty()) {
                        Subject subject = new Subject();
                        subject.setName(textFieldSubject.getText());
                        searchFilters.setSubject(subject);
                    }
                    if (!textFieldFirstNameAuthor.getText().isEmpty() || !textFieldLastNameAuthor.getText().isEmpty()) {
                        Author author = new Author();
                        if (!textFieldFirstNameAuthor.getText().isEmpty()) {
                            author.setFirstName(textFieldFirstNameAuthor.getText());
                        }
                        if (!textFieldLastNameAuthor.getText().isEmpty()) {
                            author.setLastName(textFieldLastNameAuthor.getText());
                        }
                        searchFilters.setAuthor(author);
                    }
                    if ((Integer) spinnerMaxPageCount.getValue() != 0) {
                        searchFilters.setMaxPageCount((Integer) spinnerMaxPageCount.getValue());
                    }
                    if ((Integer) spinnerMaxPrice.getValue() != 0) {
                        searchFilters.setMaxPrice(((Number) spinnerMaxPrice.getValue()).floatValue());
                    }
                    RequestSelectBook request = new RequestSelectBook(searchFilters);
                    try {
                        oos.writeObject(request);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur de connexion : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.println("Requête envoyée : " + request);
                    ResponseSelectBook response = (ResponseSelectBook) ois.readObject();
                    if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                        System.out.println("Livres reçus : ");
                        for (Book book : response.getBooks()) {
                            System.out.println(book);
                        }
                        UpdateSearch(tableStock, response);

                    } else {
                        clearBooksTable(tableStock);
                        System.out.println("Aucun livre trouvé ou erreur de serveur.");
                    }

                } catch (IOException | ClassNotFoundException ex) {

                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la communication avec le serveur : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    private void initClearButtonForSpinner(JButton button, JSpinner spinner, JLabel label) {
        button.setEnabled(false);
        spinner.addChangeListener(e -> buttonVisibility());

        button.addActionListener(e -> {
            spinner.setValue(0);
            buttonVisibility();
        });
    }
    private void UpdateSearch(JTable tableStock, ResponseSelectBook response) {
        clearBooksTable(tableStock);
        BookModelAchatClient model = (BookModelAchatClient) tableStock.getModel();

        for (Book book : response.getBooks()) {
            model.addRow(book, book.getStockQuantity());
        }

    }

    public void clearBooksTable( JTable tableStock ) {
        BookModelAchatClient model = new BookModelAchatClient(new ArrayList<>());
        tableStock.setModel(model);
        tableStock.setColumnModel(new BookColumnModelAchatClient());
    }

    private void addFilterListener(JTextField textField, JButton button, JLabel label) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buttonVisibility();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buttonVisibility();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buttonVisibility();
            }
        });
    }

    private void buttonVisibility() {
        updateButtonAndLabelVisibility(textFieldTitle, xButtonTitle, labelTitle);
        updateButtonAndLabelVisibility(textFieldSubject, xButtonSubject, labelSubject);
        updateButtonAndLabelVisibility(textFieldLastNameAuthor, xButtonLastName, labelLastName);
        updateButtonAndLabelVisibility(textFieldFirstNameAuthor, xButtonFirstName, labelFirstName);

        //Spinners
        updateButtonAndLabelVisibilityForSpinner(spinnerMaxPrice, xButtonMaxPrice, labelMaxPrice);
        updateButtonAndLabelVisibilityForSpinner(spinnerMaxPageCount, xButtonMaxPageCount, labelPageCount);
    }

    private void updateButtonAndLabelVisibility(JTextField textField, JButton button, JLabel label) {
        boolean TextBool = !textField.getText().trim().isEmpty();
        button.setVisible(TextBool);
        button.setEnabled(TextBool);
        label.setForeground(TextBool ? Color.WHITE : Color.GRAY);
    }

    private void updateButtonAndLabelVisibilityForSpinner(JSpinner spinner, JButton button, JLabel label) {
        boolean ValueBool = (Integer) spinner.getValue() != 0;
        button.setEnabled(ValueBool);
        label.setForeground(ValueBool ? Color.WHITE : Color.GRAY);
    }
    private void initTable() {
        try {
            BookSearchVM searchFilters = new BookSearchVM();
            RequestSelectBook request = new RequestSelectBook(searchFilters);
            try {
                oos.writeObject(request);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur de connexion : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("Requête envoyée : " + request);
            ResponseSelectBook response = (ResponseSelectBook) ois.readObject();

            if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                System.out.println("Livres reçus : ");
                for (Book book : response.getBooks()) {
                    System.out.println(book);
                }
                UpdateSearch(tableStock, response);

            } else {
                clearBooksTable(tableStock);
                System.out.println("Aucun livre trouvé ou erreur de serveur.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la communication avec le serveur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}