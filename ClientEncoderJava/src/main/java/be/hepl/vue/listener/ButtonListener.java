package be.hepl.vue.listener;

import be.hepl.entity.Author;
import be.hepl.entity.Book;
import be.hepl.entity.Employee;
import be.hepl.entity.Subject;
import be.hepl.vue.window.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import static be.hepl.MyLibraries.protocole.ProtocoleC.*;
import static be.hepl.myLibraries.file.FileHelper.getSettingInt;
import static be.hepl.myLibraries.file.FileHelper.getSettingString;
import static be.hepl.myLibraries.socket.TCP.clientSocket;
import static be.hepl.vue.window.dialog.AddAuthorDialog.showAddAuthorDialog;
import static be.hepl.vue.window.dialog.AddSubjectDialog.showAddSubjectDialog;
import static be.hepl.vue.window.dialog.LoginDialog.showLoginDialog;
import static be.hepl.vue.window.windowHelper.MainWindowHelper.ClearFieldsBook;

public class ButtonListener {
    final private MainWindow app;
    final private Socket sClient;

    public ButtonListener(MainWindow appEncodage) {
        this.app = appEncodage;

        setComponentsEnabled(false);

        int portServeur = getSettingInt("../config.txt", "PORT_ENCODING");
        String ipServ = getSettingString("../config.txt", "IP_ENCODING");

        sClient = clientSocket(ipServ, portServeur);

        if(sClient == null) {
            System.out.println("Erreur de connexion");
        }
    }

    public ActionListener getLogin() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee newEmploye = showLoginDialog(app);

                ArrayList<String> response;
                ArrayList<String> allAuthors;
                ArrayList<String> allSubjects;

                if(newEmploye != null) {
                    try {
                        response = login(newEmploye.getLogin(), newEmploye.getPassword(), sClient);

                        if(createResponseMessage(response, "Login")) {
                            app.getComboBoxAuteur().removeAllItems();
                            app.getComboBoxSujet().removeAllItems();
                            allAuthors = getAuthors(sClient);
                            allSubjects = getSubjects(sClient);

                            for (String author : allAuthors) {
                                app.getComboBoxAuteur().addItem(author);
                            }

                            for (String subject : allSubjects) {
                                app.getComboBoxSujet().addItem(subject);
                            }

                            setComponentsEnabled(true);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
    }

    public ActionListener getLogout() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> response = logout(sClient);

                    if(createResponseMessage(response, "Déconnexion")) {
                        ClearFieldsBook(app);
                        app.clearBooksTable();
                        setComponentsEnabled(false);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    public ActionListener getExit() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                        response = addAuthor(newAuthor.getLastName(), newAuthor.getFirstName(), newAuthor.getDateOfBirth(), sClient);

                        if(createResponseMessage(response, "Ajouter auteur")) {
                            app.getComboBoxAuteur().removeAllItems();
                            allAuthors = getAuthors(sClient);

                            for (String author : allAuthors) {
                                app.getComboBoxAuteur().addItem(author);
                            }
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
                Subject newSubject = showAddSubjectDialog(app);
                ArrayList<String> response;
                ArrayList<String> allSubjects;

                if(newSubject != null) {
                    try {
                        response = addSubject(newSubject.getName(), sClient);

                        if(createResponseMessage(response, "Ajouter sujet")) {
                            app.getComboBoxSujet().removeAllItems();
                            allSubjects = getSubjects(sClient);

                            for (String subject : allSubjects) {
                                app.getComboBoxSujet().addItem(subject);
                            }
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
                    newBook.add(app.getTextFieldTitre().getText());
                    newBook.add(app.getTextFieldISBN().getText());
                    newBook.add(app.getSpinnerPages().getValue().toString());
                    newBook.add(app.getSpinnerStocks().getValue().toString());
                    newBook.add(app.getSpinnerPrix().getValue().toString());
                    newBook.add(app.getSpinnerAnneePublication().getValue().toString());

                    ArrayList<String> bookArray = addBook(newBook, sClient);

                    if(createResponseMessage(bookArray, "Ajouter livre")) {
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
                        app.addBookTable(newBookRow);

                        ClearFieldsBook(app);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    System.out.println("Aucun champ sélectionnné");
                }
            }
        };
    }

    private Boolean createResponseMessage(ArrayList<String> response, String title) {
        if(response.getFirst().equals("ok")) {
            JOptionPane.showMessageDialog(app, response.get(1), title, JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(app, response.get(1), title, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void setComponentsEnabled(boolean enabled) {
        // Boutons
        app.getAjouterButtonAuteur().setEnabled(enabled);
        app.getAjouterButtonSujet().setEnabled(enabled);
        app.getAjouterButtonNouveauLivre().setEnabled(enabled);
        app.getViderLesChampsButton().setEnabled(enabled);

        // ComboBox
        app.getComboBoxAuteur().setEnabled(enabled);
        app.getComboBoxSujet().setEnabled(enabled);

        // Spinners
        app.getSpinnerAnneePublication().setEnabled(enabled);
        app.getSpinnerPrix().setEnabled(enabled);
        app.getSpinnerPages().setEnabled(enabled);
        app.getSpinnerStocks().setEnabled(enabled);

        // TextFields
        app.getTextFieldISBN().setEnabled(enabled);
        app.getTextFieldTitre().setEnabled(enabled);
    }
}
