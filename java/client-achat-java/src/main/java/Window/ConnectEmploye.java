package Window;

import javax.swing.*;

public class ConnectEmploye extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;

    public ConnectEmploye() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }
}
