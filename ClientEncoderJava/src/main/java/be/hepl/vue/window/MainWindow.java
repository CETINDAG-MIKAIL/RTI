package be.hepl.vue.window;

import be.hepl.entity.Author;
import be.hepl.entity.Book;
import be.hepl.vue.listener.ButtonListener;
import be.hepl.vue.table.BookTableColumnModel;
import be.hepl.vue.table.BookTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private MyJMenu myJMenu;

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


    public MainWindow() {
        setContentPane(JPanelMain);
        setTitle("Application Encodage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 500);

        this.myJMenu = new MyJMenu();
        setJMenuBar(myJMenu.getMenuBar());

        initializeListeners();
        clearBooksTable();
        tableListeLivre.setFillsViewportHeight(true);
        setSpinners();

        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }

    public void addBookTable(Book player) {
        BookTableModel model = (BookTableModel) tableListeLivre.getModel();
        model.addRow(player);
    }

    public void clearBooksTable() {
        tableListeLivre.setModel(new BookTableModel(new ArrayList<Book>()));
        tableListeLivre.setColumnModel(new BookTableColumnModel());
    }

    private void setSpinners() {
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        spinnerPages.setModel(model);

        model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 0.5);
        spinnerPrix.setModel(model);

        model = new SpinnerNumberModel(2000, 1000, Integer.MAX_VALUE, 1);
        spinnerAnneePublication.setModel(model);

        model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 5);
        spinnerStocks.setModel(model);
    }

    private void initializeListeners() {
        ButtonListener buttonListener = new ButtonListener(this);
        this.myJMenu.getLoginItem().addActionListener(buttonListener.getLogin());
        this.myJMenu.getLogoutItem().addActionListener(buttonListener.getLogout());
        this.myJMenu.getExitItem().addActionListener(buttonListener.getExit());
        viderLesChampsButton.addActionListener(buttonListener.getEmptyValues());
        ajouterButtonAuteur.addActionListener(buttonListener.getAddAuthor());
        ajouterButtonSujet.addActionListener(buttonListener.getAddSubject());
        ajouterButtonNouveauLivre.addActionListener(buttonListener.getAddBook());
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

    public JButton getAjouterButtonAuteur() {return ajouterButtonAuteur;}

    public JButton getAjouterButtonSujet() {return ajouterButtonSujet;}

    public JButton getViderLesChampsButton() {return viderLesChampsButton;}

    public JButton getAjouterButtonNouveauLivre() {return ajouterButtonNouveauLivre;}
}
