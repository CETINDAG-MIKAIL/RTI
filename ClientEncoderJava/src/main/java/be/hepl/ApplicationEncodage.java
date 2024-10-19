package be.hepl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationEncodage extends JFrame {
    private JTextField textFieldTitre;
    private JComboBox<String> comboBoxAuteur;
    private JComboBox<String> comboBoxSujet;
    private JButton ajouterButton;
    private JButton ajouterButton1;
    private JButton viderLesChampsButton;
    private JButton ajouterNouveauLivreButton;
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
        setSize(1000, 500);
        createMenuBar();
        setVisible(true);
    }

    // Initialisation de la JTable
    private void initializeTable() {
        // Exemple de données pour la JTable
        String[] columnNames = {"ISBN", "Titre", "Auteur", "Sujet", "Prix", "Pages", "Année", "Stocks"};
        Object[][] data = {
                {"123456789", "Titre 1", "Auteur 1", "Sujet 1", 20, 200, 2020, 10},
                {"987654321", "Titre 2", "Auteur 2", "Sujet 2", 30, 300, 2019, 15},
                {"111213141", "Titre 3", "Auteur 3", "Sujet 3", 25, 250, 2021, 8}
        };

        // Initialiser la JTable avec des données
        tableListeLivre = new JTable(data, columnNames);
        tableListeLivre.setFillsViewportHeight(true);  // Pour que la table remplisse l'espace disponible
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
}
