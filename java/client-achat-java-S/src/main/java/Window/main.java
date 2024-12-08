    package Window;

    import com.formdev.flatlaf.FlatDarkLaf;
    import org.bouncycastle.jce.provider.BouncyCastleProvider;

    import javax.swing.*;
    import java.io.IOException;
    import java.security.Security;

    public class main {
        public static void main(String[] args) throws IOException {
            Security.addProvider(new BouncyCastleProvider());
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
