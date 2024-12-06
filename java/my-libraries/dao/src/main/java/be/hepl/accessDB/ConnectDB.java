package be.hepl.accessDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.hepl.FileHelper.getSettingString;

public class ConnectDB {
    private static Connection conn = null;
    private static ConnectDB instance = null;

    private static String fileName = "./java/settingsDB.properties";
    private static String sCon;
    private static String sUser;
    private static String sPwd;
    private static String forName;

    private ConnectDB() {
        if (sCon == null || sUser == null || sPwd == null || forName == null) {
            sCon = getSettingString(fileName, "DB_S_CON");
            sUser = getSettingString(fileName, "DB_S_USER");
            sPwd = getSettingString(fileName, "DB_S_PWD");
            forName = getSettingString(fileName, "DB_S_FOR_NAME");
        }

        try {
            Class.forName(forName);
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(sCon, sUser, sPwd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ConnectDB getInstance() {
        if (instance == null) {
            instance = new ConnectDB();
        }
        return instance;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Closing DB connection");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConn() {
        return conn;
    }
}
