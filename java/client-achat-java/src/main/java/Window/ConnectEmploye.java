package Window;

import be.hepl.FileHelper;
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

    private final Socket socket; // Utilise le même socket
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public ConnectEmploye(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
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
            RequestEmployee requete = new RequestEmployee(loginEmployee, passwordfield );

            oos.writeObject(requete);

            ResponseEmployee response = (ResponseEmployee) ois.readObject();

            if (response.isSuccess()) {
                dispose();
                System.out.println("Connexion réussie pour l'employé : " + loginEmployee);
                if (socket.isClosed()) {
                    JOptionPane.showMessageDialog(this, "Le socket est fermé !", "Erreur...", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Vous pouvez appeler ici une autre fenêtre si nécessaire, par exemple :
                ConnectClient connectClientDialog = new ConnectClient(socket, oos, ois);
                connectClientDialog.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Erreur de login !","Erreur...",JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Problème de connexion !","Connect employe",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}
