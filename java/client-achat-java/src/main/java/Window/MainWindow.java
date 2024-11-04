package Window;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.FlatDarkLaf;


public class MainWindow extends JFrame{
    private JTable table1;
    private JTable table2;
    private JButton ajouterButton;
    private JButton viderLePanierButton;
    private JButton supprimerButton;
    private JButton payerButton;
    private JButton annulerButton;
    private JTextField textFieldTitle;
    private JTextField textFieldSubject;
    private JTextField textFieldLastNameAuthor;
    private JTextField textFieldFirstNameAuthor;
    private JSpinner spinnerMaxPrice;
    private JSpinner spinnerMaxPageCount;
    private JButton rechercheButton;
    private JButton xButtonTitle;
    private JButton xButtonLastName;
    private JButton xButtonMaxPrice;
    private JButton xButtonSubject;
    private JButton xButtonFirstName;
    private JButton xButtonMaxPageCount;
    private JPanel MainPanel;
    private JLabel labelTitle;
    private JLabel labelLastName;
    private JLabel labelSubject;
    private JLabel labelFirstName;
    private JLabel labelPageCount;
    private JLabel labelMaxPrice;
    public MainWindow() {
        setContentPane(MainPanel);
        setTitle("MainWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);

        initClearButton(xButtonTitle, textFieldTitle, labelTitle);
        initClearButton(xButtonSubject, textFieldSubject, labelSubject);
        initClearButton(xButtonLastName, textFieldLastNameAuthor, labelLastName);
        initClearButton(xButtonFirstName, textFieldFirstNameAuthor, labelFirstName);
        initClearButtonForSpinner(xButtonMaxPrice, spinnerMaxPrice, labelMaxPrice);
        initClearButtonForSpinner(xButtonMaxPageCount, spinnerMaxPageCount, labelPageCount);

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        new MainWindow();
    }

    private void initClearButton(JButton button, JTextField textField, JLabel label) {
        button.setVisible(false);
        button.setEnabled(false);
        addFilterListener(textField, button, label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                buttonVisibility();
            }
        });
    }

    private void initClearButtonForSpinner(JButton button, JSpinner spinner, JLabel label) {
        button.setEnabled(false);
        spinner.addChangeListener(e -> buttonVisibility());

        button.addActionListener(e -> {
            spinner.setValue(0);
            buttonVisibility();
        });
    }

    private void addFilterListener(JTextField textField, JButton button, JLabel label) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buttonVisibility();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buttonVisibility();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buttonVisibility();
            }
        });
    }

    private void buttonVisibility() {
        updateButtonAndLabelVisibility(textFieldTitle, xButtonTitle, labelTitle);
        updateButtonAndLabelVisibility(textFieldSubject, xButtonSubject, labelSubject);
        updateButtonAndLabelVisibility(textFieldLastNameAuthor, xButtonLastName, labelLastName);
        updateButtonAndLabelVisibility(textFieldFirstNameAuthor, xButtonFirstName, labelFirstName);

        //Spinners
        updateButtonAndLabelVisibilityForSpinner(spinnerMaxPrice, xButtonMaxPrice, labelMaxPrice);
        updateButtonAndLabelVisibilityForSpinner(spinnerMaxPageCount, xButtonMaxPageCount, labelPageCount);
    }

    private void updateButtonAndLabelVisibility(JTextField textField, JButton button, JLabel label) {
        boolean TextBool = !textField.getText().trim().isEmpty();
        button.setVisible(TextBool);
        button.setEnabled(TextBool);
        label.setForeground(TextBool ? Color.WHITE : Color.GRAY);
    }

    private void updateButtonAndLabelVisibilityForSpinner(JSpinner spinner, JButton button, JLabel label) {
        boolean ValueBool = (int) spinner.getValue() != 0;
        button.setEnabled(ValueBool);
        label.setForeground(ValueBool ? Color.WHITE : Color.GRAY);
    }
}