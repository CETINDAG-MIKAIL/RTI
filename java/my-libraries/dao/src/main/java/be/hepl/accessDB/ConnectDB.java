package be.hepl.accessDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectDB {
    private static Connection conn = null;

    private static String fileName = "settingsDB.txt";
    private static String sCon;
    private static String sUser;
    private static String sPwd;
    private static String forName;

    public ConnectDB() {
        if(ConnectDB.sCon == null || ConnectDB.sUser == null || ConnectDB.sPwd == null || ConnectDB.forName == null) {
            ConnectDB.sCon = getSettingString(fileName, "DB_S_CON");
            ConnectDB.sUser = getSettingString(fileName, "DB_S_USER");
            ConnectDB.sPwd = getSettingString(fileName, "DB_S_PWD");
            ConnectDB.forName = getSettingString(fileName, "DB_S_FOR_NAME");
        }

        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(sCon, sUser, sPwd);
            }
        }
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void close() {
        try {
            conn.close();
            System.out.println("Closing DB connection");
        }
        catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConn() {
        return conn;
    }

}
