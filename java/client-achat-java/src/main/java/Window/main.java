    package Window;

    import be.hepl.FileHelper;
    import com.formdev.flatlaf.FlatDarkLaf;

    import javax.swing.*;
    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.net.Socket;

    import static Window.MySocket.*;

    public class main {
        public static void main(String[] args) throws IOException {
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
