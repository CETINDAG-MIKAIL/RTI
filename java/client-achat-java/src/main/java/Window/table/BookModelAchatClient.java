package Window.table;

import be.hepl.model.entity.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class BookModelAchatClient extends AbstractTableModel {
    private ArrayList<Book> books;

    public BookModelAchatClient(ArrayList<Book> books) {
        this.books = books;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0) return Integer.class;
        if(columnIndex == 1) return String.class;
        if(columnIndex == 2) return String.class;
        if(columnIndex == 3) return String.class;
        if(columnIndex == 4) return String.class;
        if(columnIndex == 5) return Integer.class;
        if(columnIndex == 6) return Integer.class;
        if(columnIndex == 7) return Float.class;
        if(columnIndex == 8) return Integer.class;

        return null;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return book.getId();
            case 1:
                return book.getTitle();
            case 2:
                return book.getAuthor().getFirstName()+ " " + book.getAuthor().getLastName();
            case 3:
                return book.getSubject().getName();
            case 4:
                return book.getIsbn();
            case 5:
                return book.getPageCount();
            case 6:
                return book.getPublishYear();
            case 7:
                return book.getPrice();
            case 8:
                return book.getStockQuantity();

            default:
                throw new IllegalStateException("Unexpected value: " + columnIndex);
        }
    }

    public Book getBook(int rowIndex) {
        return books.get(rowIndex);
    }

    public void addRow(Book book, int quantityToAdd) {
        boolean bookExists = false;

        // Parcours des livres dans le panier pour vérifier si le livre existe déjà
        for (int i = 0; i < books.size(); i++) {
            Book existingBook = books.get(i);

            // Si le livre est déjà dans le panier, on augmente sa quantité
            if (existingBook.getId() == book.getId()) {
                existingBook.setStockQuantity(existingBook.getStockQuantity() + quantityToAdd);
                fireTableRowsUpdated(i, i); // Actualise l'affichage de la ligne modifiée
                bookExists = true;
                break;
            }
        }

        // Si le livre n'existe pas dans le panier, on l'ajoute comme nouvelle ligne
        if (!bookExists) {
            Book bookCopy = new Book(book); // Copier le livre pour éviter de modifier l'original
            bookCopy.setStockQuantity(quantityToAdd);
            books.add(bookCopy);
            fireTableRowsInserted(books.size() - 1, books.size() - 1); // Actualise l'affichage pour la nouvelle ligne
        }
    }
    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < books.size()) {
            books.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex); // Actualise l'affichage
        }
    }
    public void clearData() {
        books.clear();
        fireTableDataChanged(); // Notifie la table que les données ont été modifiées
    }
}
