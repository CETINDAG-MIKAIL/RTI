package Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectEmploye extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;



    public ConnectEmploye() {
        setTitle("Employée");
        setContentPane(contentPane);
        setModal(true);
        setSize(300, 200);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }
    private void onOK() {
        dispose();
        ConnectClient connectClientDialog =  new ConnectClient();
        connectClientDialog.setVisible(true);
    }

    private void onCancel() {
        dispose();
    }
    public static void main(String[] args) {
        ConnectEmploye dialog = new ConnectEmploye();
        dialog.setVisible(true);
        dialog.pack();
    }
}
