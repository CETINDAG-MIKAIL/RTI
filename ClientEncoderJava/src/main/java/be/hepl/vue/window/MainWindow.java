package be.hepl.vue.window;

import be.hepl.entity.Author;
import be.hepl.entity.Book;
import be.hepl.vue.listener.ButtonListener;
import be.hepl.vue.table.BookTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    MyJMenu myJMenu;

    private JTextField textFieldTitre;

    private JComboBox<String> comboBoxAuteur;
    private JComboBox<String> comboBoxSujet;
    private JButton ajouterButtonAuteur;
    private JButton ajouterButtonSujet;
    private JButton viderLesChampsButton;
    private JButton ajouterButtonNouveauLivre;



    private JTable tableListeLivre;
    private JSpinner spinnerPages;
    private JSpinner spinnerPrix;
    private JSpinner spinnerAnneePublication;
    private JSpinner spinnerStocks;


    private JTextField textFieldISBN;
    private JPanel JPanelMain;
    private JPanel JPanelTitre;
    private JPanel JPanelAuteur;
    private JPanel JPanelSujet;
    private JPanel JpanelBoutonViderChamps;
    private JPanel JpanelBoutonAjouterlivre;
    private JPanel JpanelSpinner;
    private JPanel JpanelISBN;

    // Constructeur
    public MainWindow() {
        // Initialisation de la fenêtre principale
        setContentPane(JPanelMain);
        setTitle("Application Encodage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 500);

        MyJMenu myJMenu = new MyJMenu();
        this.myJMenu = myJMenu;
        setJMenuBar(myJMenu.getMenuBar());

        setVisible(true);

        ButtonListener buttonListener = new ButtonListener(this);
        viderLesChampsButton.addActionListener(buttonListener.getEmptyValues());
        ajouterButtonAuteur.addActionListener(buttonListener.getAddAuthor());
        ajouterButtonSujet.addActionListener(buttonListener.getAddSubject());
        ajouterButtonNouveauLivre.addActionListener(buttonListener.getAddBook());



        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        spinnerPages.setModel(model);

        model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 0.5);
        spinnerPrix.setModel(model);

        model = new SpinnerNumberModel(2000, 1000, Integer.MAX_VALUE, 1);
        spinnerAnneePublication.setModel(model);

        model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 5);
        spinnerStocks.setModel(model);
    }


    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }

    public void addPlayer(Book player) {
        BookTableModel model = (BookTableModel) tableListeLivre.getModel();
        model.addRow(player);
    }


    public MyJMenu getMyJMenu() {
        return myJMenu;
    }

    public JComboBox<String> getComboBoxAuteur() {
        return comboBoxAuteur;
    }

    public JComboBox<String> getComboBoxSujet() {
        return comboBoxSujet;
    }


    public JTextField getTextFieldTitre() {
        return textFieldTitre;
    }

    public JTextField getTextFieldISBN() {
        return textFieldISBN;
    }

    public JSpinner getSpinnerStocks() {
        return spinnerStocks;
    }

    public JSpinner getSpinnerAnneePublication() {
        return spinnerAnneePublication;
    }

    public JSpinner getSpinnerPrix() {
        return spinnerPrix;
    }

    public JSpinner getSpinnerPages() {
        return spinnerPages;
    }
    public JTable getTableListeLivre() {
        return tableListeLivre;
    }
}
