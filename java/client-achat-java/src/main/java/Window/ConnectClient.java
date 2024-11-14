package Window;

import be.hepl.FileHelper;
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
    private final Socket socket; // Utilise le même socket
    private String login;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLastNameClient;
    private JTextField textFieldFirstNameClient;
    private JCheckBox nouveauClientCheckBox;

    public ConnectClient(Socket socket,ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
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
        String loginLastName = textFieldLastNameClient.getText();
        String loginFirstName = textFieldFirstNameClient.getText();
        boolean newClient = nouveauClientCheckBox.isSelected();


        try
        {
            if (socket.isClosed()) {
                JOptionPane.showMessageDialog(this, "Le socket est fermé !", "Erreur...", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RequestClient requete = new RequestClient(loginLastName,loginFirstName,newClient );


            oos.writeObject(requete);



            ResponseClient response = (ResponseClient) ois.readObject();

            if (response.isValide())
            {
                dispose();
                new MainWindow(socket, oos, ois);
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Erreur de login !","Erreur...",JOptionPane.ERROR_MESSAGE);
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
}
