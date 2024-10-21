package be.hepl.vue.window.dialog;

import be.hepl.entity.Author;
import be.hepl.vue.window.MainWindow;

import javax.swing.*;
import java.awt.*;

public class AddAuthorDialog {
    public static Author showAddAuthorDialog(MainWindow mainWindow) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JTextField firstnameField = new JTextField();
        JTextField lastnameField = new JTextField();
        JTextField birthDate = new JTextField();

        panel.add(new JLabel("Prénom :"));
        panel.add(firstnameField);
        panel.add(new JLabel("Nom :"));
        panel.add(lastnameField);
        panel.add(new JLabel("Date de naissance format YYYY-MM-DD :"));
        panel.add(birthDate);

        int option = JOptionPane.showConfirmDialog(mainWindow, panel, "AddAuthor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            Author author = new Author(firstnameField.getText(), lastnameField.getText(), birthDate.getText());

            if (author.getFirstName().trim().isEmpty() || author.getLastName().trim().isEmpty() ||  author.getDateOfBirth().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainWindow, "Tous les champs sont obligatoires, auteur non ajouté.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            } else
                return author;
        }
        return null;
    }
}
