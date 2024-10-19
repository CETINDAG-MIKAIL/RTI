package be.hepl.vue.listener;

import be.hepl.Main;
import be.hepl.vue.ApplicationEncodage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static be.hepl.myLibraries.protocole.ProtocoleC.*;

public class ButtonListener {
    final private ApplicationEncodage app;

    public ButtonListener(ApplicationEncodage appEncodage) {
        this.app = appEncodage;
    };

    public ActionListener getEmptyValues() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Vider les valeurs des spinners existants
                app.getSpinnerPages().setValue(0);
                app.getSpinnerPrix().setValue(0.0);
                app.getSpinnerAnneePublication().setValue(2000);
                app.getSpinnerStocks().setValue(0);

                app.getTextFieldISBN().setText("");
                app.getTextFieldTitre().setText("");
            }
        };
    }

    public ActionListener getAddAuthor() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showAddAuthorDialog();


            }
        };
    }

    public ActionListener getAddSubject() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showAddSubjectDialog();
            }
        };
    }

    public ActionListener getAddBook() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> MonArray = new ArrayList<>();

                try {
                    MonArray.add(Objects.requireNonNull(app.getComboBoxAuteur().getSelectedItem()).toString());
                    MonArray.add(Objects.requireNonNull(app.getComboBoxSujet().getSelectedItem()).toString());
                } catch (Exception ex) {
                    System.out.println("Aucun champ sélectionnné");
                }

                MonArray.add(app.getTextFieldTitre().toString());
                MonArray.add(app.getTextFieldISBN().toString());
                MonArray.add(app.getSpinnerPages().toString());
                MonArray.add(app.getSpinnerStocks().toString());
                MonArray.add(app.getSpinnerPrix().toString());
                MonArray.add(app.getSpinnerAnneePublication().toString());
                try {
                    ArrayList<String> livres = addBook(MonArray, Main.getSClient());

                    if(livres.getFirst().equals("ok")) {
                        JOptionPane.showMessageDialog(app, livres.get(1), "Ajouter livre", JOptionPane.INFORMATION_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(app, livres.get(1), "Ajouter livre", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }



                //app.getTableListeLivre().add(MonArray);
            }
        };
    }
}
