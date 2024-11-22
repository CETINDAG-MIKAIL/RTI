package Window.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class BookColumnModelAchatClient extends DefaultTableColumnModel {
    public BookColumnModelAchatClient() {
        super();
        int[] widths = {5, 20, 18, 18, 15, 10, 10, 10 ,10};
        String[] names = {"Id", "Title", "Auteur", "Sujet", "ISBN", "Pages", "Année", "Prix", "Stock"};

        for (int i=0; i<widths.length; i++) {
            TableColumn column = new TableColumn(i, widths[i]);
            column.setHeaderValue(names[i]);
            addColumn(column);
        }
    }
}
