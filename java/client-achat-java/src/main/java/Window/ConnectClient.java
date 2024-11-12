package Window;

import be.hepl.request.RequestClient;
import be.hepl.response.ResponseClient;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ConnectClient extends JDialog {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private String login;


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLastNameClient;
    private JTextField textFieldFirstNameClient;
    private JCheckBox nouveauClientCheckBox;

    public ConnectClient() {
        setContentPane(contentPane);
        setTitle("Client");
        setSize(300, 200);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        //String ipServeur = jTextFieldIP.getText();
        //int portServeur = Integer.parseInt(jTextFieldPort.getText());
        String loginLastName = textFieldLastNameClient.getText();
        String loginFirstName = textFieldFirstNameClient.getText();
        boolean newClient = nouveauClientCheckBox.isSelected();


        try
        {
            socket = new Socket("192.168.68.51",50000);
            RequestClient requete = new RequestClient(loginLastName,loginFirstName,newClient );
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(requete);
            ResponseClient response = (ResponseClient) ois.readObject();
            if (response.isValide())
            {
                buttonOK.setEnabled(false);
                buttonCancel.setEnabled(true);
                this.login = login;
                dispose();
                new MainWindow();
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Erreur de login !","Erreur...",JOptionPane.ERROR_MESSAGE);
                socket.close();
            }
        }catch (IOException | ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(this,"Problème de connexion !","Erreur...",JOptionPane.ERROR_MESSAGE);
        }

    }


    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        ConnectClient dialog = new ConnectClient();
        dialog.pack();
        dialog.setVisible(true);
    }
}
