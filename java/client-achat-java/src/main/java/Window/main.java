    package Window;

    import be.hepl.FileHelper;
    import com.formdev.flatlaf.FlatDarkLaf;

    import javax.swing.*;
    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.net.Socket;

    public class main {
        private static Socket socket;
        public static void main(String[] args) throws IOException {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            String ipServeur = FileHelper.getSettingString("./config.txt", "IP_SERVER");
            int portServeur = FileHelper.getSettingInt("./config.txt", "PORT_PURCHASE");

            socket = new Socket(ipServeur, portServeur);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            ConnectEmploye dialog = new ConnectEmploye(socket, oos, ois);
            dialog.setVisible(true);
            dialog.pack();
        }
    }
