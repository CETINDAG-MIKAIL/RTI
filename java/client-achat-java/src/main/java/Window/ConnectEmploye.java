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
            System.out.println("Tentative de connexion avec l'employé : " + loginEmployee);
            System.out.println("Mot de passe : " + passwordfield); // Ne jamais afficher le mot de passe en production
            // Simuler une réponse en créant un objet ResponseEmployee fictif
            // Pour l'exemple, on suppose que la connexion est réussie
            ResponseEmployee response = new ResponseEmployee();
            //response.setSuccess(true);  // Simuler une réponse réussie

            // Vérifier la réponse
            try {
                if (response.isSuccess()) {
                    buttonOK.setEnabled(false);
                    buttonCancel.setEnabled(true);
                    System.out.println("Connexion réussie pour l'employé : " + loginEmployee);
                    // Vous pouvez appeler ici une autre fenêtre si nécessaire, par exemple :
                    // ConnectClient connectClientDialog = new ConnectClient();
                    // connectClientDialog.setVisible(true);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur de login !", "Erreur...", JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception ex)
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
