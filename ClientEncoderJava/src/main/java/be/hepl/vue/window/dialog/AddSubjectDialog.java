package be.hepl.vue.window.dialog;

import be.hepl.entity.Subject;
import be.hepl.vue.window.MainWindow;

import javax.swing.*;
import java.awt.*;

public class AddSubjectDialog {
    public static Subject showAddSubjectDialog(MainWindow mainWindow) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JTextField nameField = new JTextField();

        panel.add(new JLabel("Nom :"));
        panel.add(nameField);

        int option = JOptionPane.showConfirmDialog(mainWindow, panel, "AddSubject", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            Subject subject = new Subject(nameField.getText());

            if (subject.getName().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez le nom du sujet.", "Erreur", JOptionPane.ERROR_MESSAGE);

                return subject;
            } else
                return null;
        }
        return null;
    }
}
