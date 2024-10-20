package be.hepl.vue.window.dialog;

import be.hepl.entity.Employee;
import be.hepl.vue.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginDialog {
    public static Employee showLoginDialog(MainWindow mainWindow) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Nom d'utilisateur:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(mainWindow, panel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


        if (option == JOptionPane.OK_OPTION) {
            Employee employee = new Employee(usernameField.getText(), Arrays.toString(passwordField.getPassword()));

            if (employee.getLogin().trim().isEmpty() || employee.getPassword().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez entrer un nom d'utilisateur et un mot de passe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Bienvenue, " + employee.getLogin() + "!", "Login réussi", JOptionPane.INFORMATION_MESSAGE);
                return employee;
            }
        }
        return null;
    }
}
