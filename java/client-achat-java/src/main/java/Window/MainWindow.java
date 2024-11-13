package Window;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import be.hepl.FileHelper;
import be.hepl.model.entity.Author;
import be.hepl.model.entity.Subject;

import be.hepl.model.entity.Book;
import be.hepl.model.viewmodel.BookSearchVM;
import be.hepl.request.RequestSelectBook;
import be.hepl.response.ResponseSelectBook;
import com.formdev.flatlaf.FlatDarkLaf;


public class MainWindow extends JFrame{
    private Socket socket;

    private String login;


    private ObjectOutputStream oos;
    private ObjectInputStream ois;


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

    public MainWindow() {
        oos = null;
        ois = null;
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
        buttonSearch(rechercheButton);
        clearBooksTable( tableStock );
        clearBooksTable( tableBasket );
        tableStock.setFillsViewportHeight(true);

        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableStock.getSelectedRow();

                // Vérifie si une ligne est bien sélectionnée dans tableStock
                if (selectedRow != -1) {
                    // Récupère le livre de la ligne sélectionnée
                    Book selectedBook = ((BookModelAchatClient) tableStock.getModel()).getBook(selectedRow);
                    // Ajoute le livre au panier
                    addBookToBasket(selectedBook);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un livre à ajouter au panier.",
                            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        new MainWindow();
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

    private void buttonSearch(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String ipServeur = FileHelper.getSettingString("./config.txt", "IP_SERVER");
                int portServeur = FileHelper.getSettingInt("./config.txt", "PORT_PURCHASE");
                // Ouvrir la connexion avec le serveur une seule fois au lieu de chaque clic
                try (Socket socket = new Socket(ipServeur, portServeur);
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

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
                    oos.writeObject(request);
                    System.out.println("Requête envoyée : " + request);

                    // Lire la réponse du serveur
                    ResponseSelectBook response = (ResponseSelectBook) ois.readObject();

                    // Vérifier si la réponse est valide
                    if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
//                        System.out.println("Livres reçus : ");
//                        for (Book book : response.getBooks()) {
//                            System.out.println(book);
//                        }
                        UpdateSearch(tableStock, response);

                    } else {
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
    private void addBookToBasket(Book book) {
        // Ajoutez le livre au modèle de données de tableBasket
        BookModelAchatClient basketModel = (BookModelAchatClient) tableBasket.getModel();
        basketModel.addRow(book);
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
        BookModelAchatClient model = (BookModelAchatClient) tableStock.getModel();
        for (be.hepl.model.entity.Book book : response.getBooks()) {
            model.addRow(book);
        }

    }

    public void clearBooksTable( JTable tableStock ) {
        BookModelAchatClient model = new BookModelAchatClient(new ArrayList<>());
        tableStock.setModel(model);
        tableStock.setColumnModel(new BookColumnModelAchatClient());
    }
    public void addBookTable(Book book) {
        BookModelAchatClient model = (BookModelAchatClient) tableStock.getModel();
        model.addRow(book);
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
}