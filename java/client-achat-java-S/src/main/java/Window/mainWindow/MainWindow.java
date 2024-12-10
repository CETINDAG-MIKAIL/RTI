package Window.mainWindow;

import Window.mainWindow.mainWindowHelper.*;
import Window.MySocket;
import be.hepl.CertificateHelper;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainWindow extends JFrame{
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private JTable tableStock;
    private JTable tableBasket;
    private JButton ajouterButton;
    private JButton viderLePanierButton;
    private JButton supprimerButton;
    private JButton payerButton;
    private JButton annulerButton;
    private Window.mainWindow.mainWindowHelper.buttonManager buttonManager;
    private JTextField textFieldTitle;
    private JTextField textFieldSubject;
    private JTextField textFieldLastNameAuthor;
    private JTextField textFieldFirstNameAuthor;
    private JSpinner spinnerMaxPrice;
    private JSpinner spinnerMaxPageCount;
    private JSpinner spinnerQuantite;
    private JButton rechercheButton;
    private JButton xButtonTitle;
    private JButton xButtonLastName;
    private JButton xButtonMaxPrice;
    private JButton xButtonSubject;
    private JButton xButtonFirstName;
    private JButton xButtonMaxPageCount;
    private JPanel MainPanel;
    private JLabel labelTitle;
    private JLabel labelLastName;
    private JLabel labelSubject;
    private JLabel labelFirstName;
    private JLabel labelPageCount;
    private JLabel labelMaxPrice;
    public SecretKey sessionKey;
    public MainWindow mainWindow;

    public Window.mainWindow.mainWindowHelper.tableManager tableManager;
    public MainWindow(Integer idClient, SecretKey sessionKey) throws IOException {

        MySocket myInstance = MySocket.getInstance();


        this.sessionKey = sessionKey;
        this.socket = myInstance.getMySocket();
        this.oos = myInstance.getOutputStream();
        this.ois = myInstance.getInputStream();
        CertificateHelper clientHelper = new CertificateHelper("./client.jks", "PassClient", "client");

        setContentPane(MainPanel);
        setTitle("MainWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        tableManager = new tableManager(oos, ois);
        initClear helper = new initClear(this);
        helper.initClearButton(xButtonTitle, textFieldTitle, labelTitle);
        helper.initClearButton(xButtonSubject, textFieldSubject, labelSubject);
        helper.initClearButton(xButtonLastName, textFieldLastNameAuthor, labelLastName);
        helper.initClearButton(xButtonFirstName, textFieldFirstNameAuthor, labelFirstName);
        helper.initClearButtonForSpinner(xButtonMaxPrice, spinnerMaxPrice, labelMaxPrice);
        helper.initClearButtonForSpinner(xButtonMaxPageCount, spinnerMaxPageCount, labelPageCount);
        tableManager.initTable(tableStock);
        tableManager.clearBooksTable(tableBasket);
        buttonManager = new buttonManager();
        buttonVisibility();
        tableStock.setFillsViewportHeight(true);
        buttonSearch buttonSearch = new buttonSearch();
        buttonSearch.buttonSearch(rechercheButton, textFieldTitle, textFieldSubject, textFieldFirstNameAuthor, textFieldLastNameAuthor, spinnerMaxPageCount, spinnerMaxPrice, oos, ois, tableManager, tableStock);
        mainListeners listeners = new mainListeners(this, tableStock, tableBasket, spinnerQuantite, idClient, oos, ois);

        ajouterButton.addActionListener(listeners.ajouterButtonListener());
        payerButton.addActionListener(listeners.payerButtonListener());
        annulerButton.addActionListener(listeners.annulerButtonListener());
        supprimerButton.addActionListener(listeners.supprimerButton());
        viderLePanierButton.addActionListener(listeners.viderLePanierButtonListener());
        setVisible(true);
    }
public void buttonVisibility() {
        buttonManager.buttonVisibility(
                textFieldTitle, xButtonTitle, labelTitle,
                textFieldSubject, xButtonSubject, labelSubject,
                textFieldLastNameAuthor, xButtonLastName, labelLastName,
                textFieldFirstNameAuthor, xButtonFirstName, labelFirstName,
                spinnerMaxPrice, xButtonMaxPrice, labelMaxPrice,
                spinnerMaxPageCount, xButtonMaxPageCount, labelPageCount
        );
    }
}