package be.hepl.vue;

import be.hepl.vue.listener.ButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationEncodage extends JFrame {
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
    public ApplicationEncodage() {
        // Initialisation de la fenêtre principale
        setContentPane(JPanelMain);
        setTitle("Application Encodage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 500);
        createMenuBar();
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

    // Méthode pour créer la barre de menu
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Créer le menu "Fichier"
        JMenu menuFichier = new JMenu("Fichier");

        // Créer les éléments de menu
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Ajouter des écouteurs pour les éléments de menu
        loginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gérer le logout (par exemple, réinitialiser l'état de l'application)
                JOptionPane.showMessageDialog(ApplicationEncodage.this, "Vous êtes déconnecté.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quitter l'application
                System.exit(0);
            }
        });

        // Ajouter les éléments de menu au menu "Fichier"
        menuFichier.add(loginItem);
        menuFichier.add(logoutItem);
        menuFichier.addSeparator(); // Ajoute une ligne de séparation
        menuFichier.add(exitItem);

        // Ajouter le menu "Fichier" à la barre de menu
        menuBar.add(menuFichier);

        // Ajouter la barre de menu à la fenêtre principale
        setJMenuBar(menuBar);
    }

    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ApplicationEncodage();
            }
        });
    }
    // Méthode pour afficher la boîte de dialogue de connexion
    private void showLoginDialog() {
        // Créer un panel pour les champs de texte
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2)); // Deux lignes, deux colonnes

        // Ajouter les champs de texte
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Nom d'utilisateur:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(passwordField);

        // Afficher la boîte de dialogue
        int option = JOptionPane.showConfirmDialog(this, panel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Vérifier si l'utilisateur a cliqué sur OK
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword()); // Obtenir le mot de passe

            // Ici, vous pouvez ajouter la logique pour vérifier le nom d'utilisateur et le mot de passe
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom d'utilisateur et un mot de passe.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Bienvenue, " + username + "!", "Login réussi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void showAddAuthorDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JTextField firstnameField = new JTextField();
        JTextField lastnameField = new JTextField();
        JTextField birthDate = new JTextField();

        panel.add(new JLabel("Prénom auteur:"));
        panel.add(firstnameField);
        panel.add(new JLabel("Nom auteur:"));
        panel.add(lastnameField);
        panel.add(new JLabel("Date de naissance:"));
        panel.add(birthDate);

        int option = JOptionPane.showConfirmDialog(this, panel, "AddAuthor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String firstnameFieldText = firstnameField.getText();
            String lastnameFieldText = lastnameField.getText();
            String birthDateFieldText = birthDate.getText();

            //créer une classe Author et retourne ça --------------------------------------------------------------

            if (lastnameFieldText.trim().isEmpty() || firstnameFieldText.trim().isEmpty() || birthDateFieldText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer les données de l'auteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Auteur bien ajouté", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void showAddSubjectDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JTextField nameField = new JTextField();

        panel.add(new JLabel("Nom sujet:"));
        panel.add(nameField);

        int option = JOptionPane.showConfirmDialog(this, panel, "AddSubject", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String nameFieldText = nameField.getText();

            if (nameFieldText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer les données du sujet.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Sujet bien ajouté", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
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
