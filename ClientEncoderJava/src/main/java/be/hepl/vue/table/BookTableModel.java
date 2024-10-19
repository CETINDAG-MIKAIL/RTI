package be.hepl.vue.table;

import javax.swing.table.AbstractTableModel;

public class BookTableModel extends AbstractTableModel {
    // Faire une classe Book ! --------------------------------------------------------------
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
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
