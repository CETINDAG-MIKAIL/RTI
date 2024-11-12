package Window;

import be.hepl.request.RequestClient;
import be.hepl.request.RequestEmployee;
import be.hepl.response.ResponseClient;
import be.hepl.response.ResponseEmployee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectEmploye extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLogin;
    private JPasswordField passwordField;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private String login;



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

        String loginEmployee = textFieldLogin.getText();
        String passwordfield = passwordField.getText();

        try
        {
            socket = new Socket("192.168.68.51",50000);
            RequestEmployee requete = new RequestEmployee(loginEmployee,passwordfield);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(requete);
            ResponseEmployee response = (ResponseEmployee) ois.readObject();
            if (response.isSuccess())
            {
                buttonOK.setEnabled(false);
                buttonCancel.setEnabled(true);
                this.login = loginEmployee;
                dispose();
                ConnectClient connectClientDialog =  new ConnectClient();
                connectClientDialog.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Erreur de login !","Erreur...",JOptionPane.ERROR_MESSAGE);
                socket.close();
            }
        }catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Problème de connexion !","Erreur...",JOptionPane.ERROR_MESSAGE);
        }























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
