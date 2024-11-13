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

    public void addRow(Book book) {
        this.books.add(book);
        fireTableRowsInserted(books.size() - 1, books.size() - 1);
    }
}
