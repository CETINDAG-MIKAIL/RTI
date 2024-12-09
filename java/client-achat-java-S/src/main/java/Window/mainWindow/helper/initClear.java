package Window.mainWindow.helper;

import Window.mainWindow.MainWindow;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class initClear {

    private final MainWindow mainInstance; // Remplacer par le nom réel de la classe contenant buttonVisibility()

    public initClear(MainWindow mainInstance) { // Injecter l'instance de la classe principale
        this.mainInstance = mainInstance;
    }

    public void initClearButton(JButton button, JTextField textField, JLabel label) {
        button.setVisible(false);
        button.setEnabled(false);
        addFilterListener(textField, button, label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                mainInstance.buttonVisibility(); // Appel de la méthode via l'instance
            }
        });
    }

    public void initClearButtonForSpinner(JButton button, JSpinner spinner, JLabel label) {
        button.setEnabled(false);
        spinner.addChangeListener(e -> mainInstance.buttonVisibility()); // Appel de la méthode via l'instance

        button.addActionListener(e -> {
            spinner.setValue(0);
            mainInstance.buttonVisibility(); // Appel de la méthode via l'instance
        });
    }

    private void addFilterListener(JTextField textField, JButton button, JLabel label) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mainInstance.buttonVisibility(); // Appel de la méthode via l'instance
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mainInstance.buttonVisibility(); // Appel de la méthode via l'instance
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mainInstance.buttonVisibility(); // Appel de la méthode via l'instance
            }
        });
    }
}
