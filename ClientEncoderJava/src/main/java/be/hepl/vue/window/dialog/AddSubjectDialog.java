package be.hepl.vue.window.dialog;

import be.hepl.vue.window.MainWindow;

import javax.swing.*;
import java.awt.*;

public class AddSubjectDialog {
    public static String showAddSubjectDialog(MainWindow mainWindow) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JTextField nameField = new JTextField();

        panel.add(new JLabel("Nom sujet:"));
        panel.add(nameField);

        int option = JOptionPane.showConfirmDialog(mainWindow, panel, "AddSubject", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String subject = nameField.getText();

            if (subject.trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez entrer les données du sujet.", "Erreur", JOptionPane.ERROR_MESSAGE);

                return subject;
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Sujet bien ajouté", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);

                return null;
            }
        }
        return null;
    }
}
