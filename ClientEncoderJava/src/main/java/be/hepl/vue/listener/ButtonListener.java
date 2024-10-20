package be.hepl.vue.listener;

import be.hepl.Main;
import be.hepl.entity.Author;
import be.hepl.entity.Book;
import be.hepl.vue.window.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static be.hepl.myLibraries.protocole.ProtocoleC.*;
import static be.hepl.vue.window.dialog.AddAuthorDialog.showAddAuthorDialog;
import static be.hepl.vue.window.dialog.AddSubjectDialog.showAddSubjectDialog;
import static be.hepl.vue.window.dialog.LoginDialog.showLoginDialog;
import static be.hepl.vue.window.windowHelper.MainWindowHelper.ClearFieldsBook;

public class ButtonListener {
    final private MainWindow app;

    public ButtonListener(MainWindow appEncodage) {
        this.app = appEncodage;
    };

    public ActionListener getLogin() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog(app);
            }
        };
    }

    public ActionListener getLogout() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gérer le logout (par exemple, réinitialiser l'état de l'application)
                JOptionPane.showMessageDialog(app, "Vous êtes déconnecté.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
        };
    }

    public ActionListener getExit() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quitter l'application
                System.exit(0);
            }
        };
    }

    public ActionListener getEmptyValues() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClearFieldsBook(app);
            }
        };
    }

    public ActionListener getAddAuthor() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Author newAuthor = showAddAuthorDialog(app);
                ArrayList<String> response;
                ArrayList<String> allAuthors;

                if(newAuthor != null) {
                    try {
                        response = addAuthor(newAuthor.getLastName(), newAuthor.getFirstName(), newAuthor.getDateOfBirth(), Main.getSClient());

                        if(response.getFirst().equals("ok")) {
                            JOptionPane.showMessageDialog(app, response.get(1), "Ajouter auteur", JOptionPane.INFORMATION_MESSAGE);

                            app.getComboBoxAuteur().removeAllItems();
                            allAuthors = getAuthors(Main.getSClient());

                            for (String author : allAuthors) {
                                app.getComboBoxAuteur().addItem(author);
                            }
                        } else {
                            JOptionPane.showMessageDialog(app, response.get(1), "Ajouter auteur", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
    }

    public ActionListener getAddSubject() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newSubject = showAddSubjectDialog(app);
                ArrayList<String> response;
                ArrayList<String> allSubjects;

                if(newSubject != null) {
                    try {
                        response = addSubject(newSubject, Main.getSClient());

                        if(response.getFirst().equals("ok")) {
                            JOptionPane.showMessageDialog(app, response.get(1), "Ajouter sujet", JOptionPane.INFORMATION_MESSAGE);

                            app.getComboBoxSujet().removeAllItems();
                            allSubjects = getSubjects(Main.getSClient());

                            for (String subject : allSubjects) {
                                app.getComboBoxSujet().addItem(subject);
                            }
                        } else {
                            JOptionPane.showMessageDialog(app, response.get(1), "Ajouter sujet", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
    }

    public ActionListener getAddBook() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> newBook = new ArrayList<>();

                try {
                    String selectedAuthor = Objects.requireNonNull(app.getComboBoxAuteur().getSelectedItem()).toString();
                    String selectedSubject = Objects.requireNonNull(app.getComboBoxSujet().getSelectedItem()).toString();

                    newBook.add(selectedAuthor);
                    newBook.add(selectedSubject);

                    newBook.add(app.getTextFieldTitre().toString());
                    newBook.add(app.getTextFieldISBN().toString());
                    newBook.add(app.getSpinnerPages().toString());
                    newBook.add(app.getSpinnerStocks().toString());
                    newBook.add(app.getSpinnerPrix().toString());
                    newBook.add(app.getSpinnerAnneePublication().toString());


                    Integer id;
                    String title;
                    String author;
                    String subject;
                    String isbn;
                    Integer pages;
                    Integer year;
                    Float price;
                    Integer stock;

                    ArrayList<String> bookArray = addBook(newBook, Main.getSClient());

                    Book newBookRow = new Book(
                            Integer.parseInt(bookArray.get(2)),
                            app.getTextFieldTitre().getText(),
                            selectedAuthor,
                            selectedSubject,
                            app.getTextFieldISBN().getText(),
                            Integer.parseInt(app.getSpinnerPages().getValue().toString()),
                            Integer.parseInt(app.getSpinnerAnneePublication().getValue().toString()),
                            Float.parseFloat(app.getSpinnerPrix().getValue().toString()),
                            Integer.parseInt(app.getSpinnerStocks().getValue().toString())
                    );

                    if(bookArray.getFirst().equals("ok")) {
                        JOptionPane.showMessageDialog(app, bookArray.get(1), "Ajouter livre", JOptionPane.INFORMATION_MESSAGE);

                        ClearFieldsBook(app);
                    } else
                        JOptionPane.showMessageDialog(app, bookArray.get(1), "Ajouter livre", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    System.out.println("Aucun champ sélectionnné");
                }



                //app.getTableListeLivre().add(MonArray);
            }
        };
    }
}
