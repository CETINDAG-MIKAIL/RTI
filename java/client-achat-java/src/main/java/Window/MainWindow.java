package Window;

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
import java.util.ArrayList;

import Window.table.BookColumnModelAchatClient;
import Window.table.BookModelAchatClient;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Subject;

import be.hepl.model.entity.Book;
import be.hepl.model.viewmodel.BookSearchVM;
import be.hepl.request.*;
import be.hepl.response.*;


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


    public MainWindow(Socket socket, ObjectOutputStream oos, ObjectInputStream ois,Integer idClient) throws IOException {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;

        setContentPane(MainPanel);
        setTitle("MainWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);

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

                    if (selectedQuantity > selectedBook.getStockQuantity()) {
                        JOptionPane.showMessageDialog(null,
                                "Quantité demandée supérieure au stock disponible (" + selectedBook.getStockQuantity() + " en stock).",
                                "Quantité non disponible", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Tentative de diminution du stock côté serveur
                    boolean stockUpdated = updateBookStock(selectedBook, selectedQuantity);
                    if (stockUpdated) {
                        // Mise à jour du modèle du panier
                        BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
                        basketModel.addRow(selectedBook, selectedQuantity);

                        // Diminuer la quantité dans tableStock pour refléter le nouveau stock
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

                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Paiement réalisé ?",
                        "Confirmation de paiement",
                        JOptionPane.YES_NO_OPTION
                );

                // Si l'utilisateur clique sur "Oui", envoie la requête
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        // Envoyer une requête au serveur pour restaurer le stock
                        RequestPayCaddy request = new RequestPayCaddy(idClient);
                        oos.writeObject(request);

                        // Recevoir la réponse du serveur
                        ResponsePayCaddy response = (ResponsePayCaddy) ois.readObject();
                        // Vérifie si le paiement a été traité avec succès
                        if (response.isSuccess()) { // Assurez-vous que votre classe RequestPayCaddy a cette méthode
                            JOptionPane.showMessageDialog(null, "Paiement effectué avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            ConnectClient connectClientDialog = new ConnectClient(socket, oos, ois);
                            connectClientDialog.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors du paiement. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors de la restauration du stock : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Si "Non" est sélectionné, rien ne se passe
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

                // Si l'utilisateur clique sur "Oui", envoie la requête
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        // Envoyer une requête au serveur pour restaurer le stock
                        RequestCancelCaddy request = new RequestCancelCaddy(idClient);
                        oos.writeObject(request);

                        // Recevoir la réponse du serveur
                        ResponseCancelCaddy response = (ResponseCancelCaddy) ois.readObject();
                        // Vérifie si le paiement a été traité avec succès
                        if (response.isSuccess()) { // Assurez-vous que votre classe RequestPayCaddy a cette méthode
                            JOptionPane.showMessageDialog(null, "Déconnexion effectué avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            dispose();

                            ConnectClient connectClientDialog = new ConnectClient(socket, oos, ois);
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
                    // Si "Non" est sélectionné, rien ne se passe
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

                    // Supposons que `getQuantity` puisse directement obtenir la quantité pour l'élément en cours
                    int quantityInBasket = bookInBasket.getStockQuantity();

                    // Restaurer le stock sur le serveur
                    boolean stockRestored = restoreBookStock(bookInBasket, quantityInBasket);
                    if (stockRestored) {
                        // Supprimer du panier et actualiser la quantité dans tableStock
                        basketModel.removeRow(selectedRow);

                        // Actualiser la quantité dans tableStock
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
                // Demander confirmation à l'utilisateur
                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez-vous vraiment vider le panier ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        // Créer et envoyer une requête pour vider le panier
                        RequestEmptyCaddy request = new RequestEmptyCaddy(idClient);
                        oos.writeObject(request);

                        // Lire la réponse du serveur
                        ResponseEmptyCaddy response = (ResponseEmptyCaddy) ois.readObject();

                        // Vérifier si l'opération a réussi
                        if (response.isSuccess()) {
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




//    public static void main(String[] args) throws IOException {
//        try {
//            UIManager.setLookAndFeel(new FlatDarkLaf());
//        } catch (UnsupportedLookAndFeelException e) {
//            throw new RuntimeException(e);
//        }
//        new MainWindow(socket);
//    }

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
            // Créer une requête personnalisée pour mettre à jour le stock
            RequestAddCaddyItem request = new RequestAddCaddyItem(book.getId(), quantity);
            oos.writeObject(request);

            // Recevoir la réponse du serveur
            ResponseAddCaddyItem response = (ResponseAddCaddyItem) ois.readObject();

            if (response.isSuccess()) {
                return true;
            } else {
                // Si la réponse est négative, afficher un message d'erreur plus détaillé
                JOptionPane.showMessageDialog(null,
                        "Échec de la mise à jour du stock : ",
                        "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException ex) {
            // Gérer les erreurs de communication ou de sérialisation
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du stock : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }


    private boolean restoreBookStock(Book book, int quantity) {
        try {
            // Envoyer une requête au serveur pour restaurer le stock
            RequestDeleteCaddyItem request = new RequestDeleteCaddyItem(book.getId(), quantity);
            oos.writeObject(request);

            // Recevoir la réponse du serveur
            ResponseDeleteCaddyItem response = (ResponseDeleteCaddyItem) ois.readObject();
            return response.isSuccess(); // isSuccess() retourne true si la restauration a réussi

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


                // Ouvrir la connexion avec le serveur une seule fois au lieu de chaque clic
                try {

                    // Préparation des filtres de recherche
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

                    // Créer la requête avec les filtres
                    RequestSelectBook request = new RequestSelectBook(searchFilters);

                    // Envoyer la requête au serveur
                    try {
                        oos.writeObject(request);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur de connexion : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }

                    System.out.println("Requête envoyée : " + request);

                    // Lire la réponse du serveur
                    ResponseSelectBook response = (ResponseSelectBook) ois.readObject();

                    // Vérifier si la réponse est valide
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
                    // Gestion des erreurs réseau ou des erreurs de classe
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la communication avec le serveur : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    private void addBookToBasket(Book book, int quantity) {
        // Vérifier si le livre existe déjà dans le panier
        BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
        boolean bookExistsInBasket = false;

        // Parcours des livres déjà dans le panier pour vérifier si le livre est déjà là
        for (int i = 0; i < basketModel.getRowCount(); i++) {
            Book existingBook = basketModel.getBook(i);
            if (existingBook.getId() == book.getId()) {
                // Si le livre existe déjà, ajouter la quantité choisie à la quantité existante
                existingBook.setStockQuantity(existingBook.getStockQuantity() + quantity);
                basketModel.fireTableRowsUpdated(i, i);  // Actualiser la ligne existante
                bookExistsInBasket = true;
                break;
            }
        }

        // Si le livre n'existe pas encore dans le panier, on l'ajoute avec la quantité sélectionnée
        if (!bookExistsInBasket) {
            // Créer une copie du livre et définir la quantité
            Book bookCopy = new Book(book);
            bookCopy.setStockQuantity(quantity);

            // Ajouter le livre au panier
            basketModel.addRow(bookCopy, quantity);
        }
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

        for (be.hepl.model.entity.Book book : response.getBooks()) {
            model.addRow(book, 1);
        }

    }

    public void clearBooksTable( JTable tableStock ) {
        BookModelAchatClient model = new BookModelAchatClient(new ArrayList<>());
        tableStock.setModel(model);
        tableStock.setColumnModel(new BookColumnModelAchatClient());
    }
    public void addBookTable(Book book) {
        BookModelAchatClient model = (BookModelAchatClient) tableStock.getModel();
        model.addRow(book, 1);
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

            // Préparation des filtres de recherche
            BookSearchVM searchFilters = new BookSearchVM();
            RequestSelectBook request = new RequestSelectBook(searchFilters);

            // Envoyer la requête au serveur
            try {
                oos.writeObject(request);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur de connexion : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            System.out.println("Requête envoyée : " + request);

            // Lire la réponse du serveur
            ResponseSelectBook response = (ResponseSelectBook) ois.readObject();

            // Vérifier si la réponse est valide
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
            // Gestion des erreurs réseau ou des erreurs de classe
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la communication avec le serveur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}