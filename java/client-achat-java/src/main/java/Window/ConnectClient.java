package Window;

import be.hepl.request.RequestClient;
import be.hepl.response.ResponseClient;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ConnectClient extends JDialog {
    private Socket socket;
    private String login;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLastNameClient;
    private JTextField textFieldFirstNameClient;
    private JTextField textFieldIdClient;
    private JCheckBox nouveauClientCheckBox;
    private JButton buttonBackEmployee;

    public ConnectClient() throws IOException {
        MySocket myInstance = MySocket.getInstance();

        this.socket = myInstance.getMySocket();
        this.oos = myInstance.getOutputStream();
        this.ois = myInstance.getInputStream();
        setContentPane(contentPane);
        setTitle("Client");
        setSize(300, 250);
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
        buttonBackEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ConnectEmploye connectClientDialog = null;
                try {
                    MySocket.closeSocket();
                    connectClientDialog = new ConnectEmploye();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                connectClientDialog.setVisible(true);
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
        String loginLastName = textFieldLastNameClient.getText();
        String loginFirstName = textFieldFirstNameClient.getText();
        boolean newClient = nouveauClientCheckBox.isSelected();


        try
        {
            RequestClient requete = new RequestClient(loginLastName,loginFirstName,newClient );
            oos.writeObject(requete);

            ResponseClient response = (ResponseClient) ois.readObject();


            if (response.isValide())
            {
                dispose();
                new MainWindow(response.getId());
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Erreur de login !","Erreur...",JOptionPane.ERROR_MESSAGE);
                MySocket.closeSocket();
                MySocket myInstance = MySocket.getInstance();

                this.socket = myInstance.getMySocket();
                this.oos = myInstance.getOutputStream();
                this.ois = myInstance.getInputStream();
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Problème de connexion !","Erreur...",JOptionPane.ERROR_MESSAGE);
        }

    }


    private void onCancel() {
        dispose();
    }
}
