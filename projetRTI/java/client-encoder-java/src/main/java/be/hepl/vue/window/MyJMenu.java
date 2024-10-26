package be.hepl.vue.window;

import javax.swing.*;

public class MyJMenu {
    private JMenuBar menuBar;
    private JMenu menuFichier;
    private JMenuItem loginItem;
    private JMenuItem logoutItem;
    private JMenuItem exitItem;

    public MyJMenu() {
        menuBar = new JMenuBar();

        menuFichier = new JMenu("Fichier");

        loginItem = new JMenuItem("Login");
        logoutItem = new JMenuItem("Logout");
        exitItem = new JMenuItem("Exit");

        menuFichier.add(loginItem);
        menuFichier.add(logoutItem);
        menuFichier.addSeparator();
        menuFichier.add(exitItem);

        menuBar.add(menuFichier);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JMenu getMenuFichier() {
        return menuFichier;
    }

    public JMenuItem getLoginItem() {
        return loginItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JMenuItem getLogoutItem() {
        return logoutItem;
    }
}
