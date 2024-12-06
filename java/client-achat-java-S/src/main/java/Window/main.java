    package Window;

    import com.formdev.flatlaf.FlatDarkLaf;

    import javax.swing.*;
    import java.io.IOException;

    public class main {
        public static void main(String[] args) throws IOException {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            ConnectClient dialog = new ConnectClient();
            dialog.setVisible(true);
            dialog.pack();

        }
    }
