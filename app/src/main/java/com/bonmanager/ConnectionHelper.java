package com.bonmanager;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * ConnectionHelper class for connecting to SQL database
 */
public class ConnectionHelper {
    private Connection connection;
    private String username;
    private String password;
    private String ip;
    private String port;
    private String database;

    ConnectionHelper(final String username,
                     final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Creates connection with server
     * @return
     */
    public Connection connectionclass() {
        ip = "79.118.114.228";
        database = "BonManagerServer";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionUrl = null;

        try {
            System.out.println("c1");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port +
                    ";databasename=" + database +
                    ";user=" + username +
                    ";pass=" + password + ";";
            System.out.println("c2");
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("c3");
        } catch (Exception e) {
            System.out.println("c4");
            Log.e("Error", e.getMessage());
        }

        return connection;
    }
}
