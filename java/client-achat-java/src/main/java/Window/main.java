package Window;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        ConnectEmploye dialog = new ConnectEmploye();
        dialog.setVisible(true);
        dialog.pack();
    }
}
