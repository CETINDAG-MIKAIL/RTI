package Window.Helper;

import javax.swing.*;
import java.awt.*;

public class windowHelper {
    private void buttonVisibility(JTextField textFieldTitle, JButton xButtonTitle, JLabel labelTitle,
                                  JTextField textFieldSubject, JButton xButtonSubject, JLabel labelSubject,
                                  JTextField textFieldLastNameAuthor, JButton xButtonLastName, JLabel labelLastName,
                                  JTextField textFieldFirstNameAuthor, JButton xButtonFirstName, JLabel labelFirstName,
                                  JSpinner spinnerMaxPrice, JButton xButtonMaxPrice, JLabel labelMaxPrice,
                                  JSpinner spinnerMaxPageCount, JButton xButtonMaxPageCount, JLabel labelPageCount) {


        updateButtonAndLabelVisibility(textFieldTitle, xButtonTitle, labelTitle);
        updateButtonAndLabelVisibility(textFieldSubject, xButtonSubject, labelSubject);
        updateButtonAndLabelVisibility(textFieldLastNameAuthor, xButtonLastName, labelLastName);
        updateButtonAndLabelVisibility(textFieldFirstNameAuthor, xButtonFirstName, labelFirstName);

        // Gérer la visibilité pour les spinners
        updateButtonAndLabelVisibilityForSpinner(spinnerMaxPrice, xButtonMaxPrice, labelMaxPrice);
        updateButtonAndLabelVisibilityForSpinner(spinnerMaxPageCount, xButtonMaxPageCount, labelPageCount);
    }

    // Gérer la visibilité pour les JTextField
    private static void updateButtonAndLabelVisibility(JTextField textField, JButton button, JLabel label) {
        boolean TextBool = !textField.getText().trim().isEmpty();
        button.setVisible(TextBool);
        button.setEnabled(TextBool);
        label.setForeground(TextBool ? Color.WHITE : Color.GRAY);
    }

    // Gérer la visibilité pour les JSpinner
    private static void updateButtonAndLabelVisibilityForSpinner(JSpinner spinner, JButton button, JLabel label) {
        boolean ValueBool = (Integer) spinner.getValue() != 0;
        button.setEnabled(ValueBool);
        label.setForeground(ValueBool ? Color.WHITE : Color.GRAY);
    }
}
