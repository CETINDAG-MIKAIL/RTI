package Window.mainWindow.mainWindowHelper;

import be.hepl.model.entity.*;
import be.hepl.model.viewmodel.*;
import be.hepl.request.*;
import be.hepl.response.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class buttonSearch {

    public void buttonSearch(JButton button, JTextField textFieldTitle, JTextField textFieldSubject, JTextField textFieldFirstNameAuthor, JTextField textFieldLastNameAuthor, JSpinner spinnerMaxPageCount, JSpinner spinnerMaxPrice, ObjectOutputStream oos, ObjectInputStream ois, tableManager tableManager, JTable tableStock) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BookSearchVM searchFilters = new BookSearchVM();

                    if (!textFieldTitle.getText().isEmpty()) {
                        searchFilters.setTitle(textFieldTitle.getText());
                    }
                    if (!textFieldSubject.getText().isEmpty()) {
                        Subject subject = new Subject();
                        subject.setName(textFieldSubject.getText());
                        searchFilters.setSubject(subject);
                    }
                    if (!textFieldFirstNameAuthor.getText().isEmpty() || !textFieldLastNameAuthor.getText().isEmpty()) {
                        Author author = new Author();
                        if (!textFieldFirstNameAuthor.getText().isEmpty()) {
                            author.setFirstName(textFieldFirstNameAuthor.getText());
                        }
                        if (!textFieldLastNameAuthor.getText().isEmpty()) {
                            author.setLastName(textFieldLastNameAuthor.getText());
                        }
                        searchFilters.setAuthor(author);
                    }
                    if ((Integer) spinnerMaxPageCount.getValue() != 0) {
                        searchFilters.setMaxPageCount((Integer) spinnerMaxPageCount.getValue());
                    }
                    if ((Integer) spinnerMaxPrice.getValue() != 0) {
                        searchFilters.setMaxPrice(((Number) spinnerMaxPrice.getValue()).floatValue());
                    }
                    RequestSelectBook request = new RequestSelectBook(searchFilters);
                    try {
                        oos.writeObject(request);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur de connexion : " + ex.getMessage(),
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.println("Requête envoyée : " + request);
                    ResponseSelectBook response = (ResponseSelectBook) ois.readObject();
                    if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                        System.out.println("Livres reçus : ");
                        for (Book book : response.getBooks()) {
                            System.out.println(book);
                        }
                        tableManager.updateSearch(tableStock, response);


                    } else {
                        tableManager.clearBooksTable(tableStock);

                        System.out.println("Aucun livre trouvé ou erreur de serveur.");
                    }

                } catch (IOException | ClassNotFoundException ex) {

                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la communication avec le serveur : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
