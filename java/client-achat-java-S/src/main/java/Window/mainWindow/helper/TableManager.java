package Window.mainWindow.helper;

import Window.table.*;
import be.hepl.model.entity.Book;
import be.hepl.model.viewmodel.*;
import be.hepl.request.*;
import be.hepl.response.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TableManager {

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public TableManager(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }

    public void initTable(JTable tableStock) {
        try {
            BookSearchVM searchFilters = new BookSearchVM();
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
                updateSearch(tableStock, response);

            } else {
                clearBooksTable(tableStock);
                System.out.println("Aucun livre trouvé ou erreur de serveur.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la communication avec le serveur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateSearch(JTable tableStock, ResponseSelectBook response) {
        clearBooksTable(tableStock);
        BookModelAchatClient model = (BookModelAchatClient) tableStock.getModel();

        for (Book book : response.getBooks()) {
            model.addRow(book, book.getStockQuantity());
        }
    }

    public void clearBooksTable(JTable tableStock) {
        BookModelAchatClient model = new BookModelAchatClient(new ArrayList<>());
        tableStock.setModel(model);
        tableStock.setColumnModel(new BookColumnModelAchatClient());
    }
}
