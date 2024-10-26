package be.hepl.vue.window.windowHelper;

import be.hepl.vue.window.MainWindow;

public class MainWindowHelper {
    public static void ClearFieldsBook(MainWindow mainWindow) {
        mainWindow.getSpinnerPages().setValue(0);
        mainWindow.getSpinnerPrix().setValue(0.0);
        mainWindow.getSpinnerAnneePublication().setValue(2000);
        mainWindow.getSpinnerStocks().setValue(0);

        mainWindow.getTextFieldISBN().setText("");
        mainWindow.getTextFieldTitre().setText("");
    }
}
