package com.tracktopell.dbutil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author Alfredo Estrada
 */
public class MySQLDBInstaller extends DBInstaller {

    public MySQLDBInstaller(String proConfigLocation) throws IOException {
        super(proConfigLocation,null);
    }

    protected Properties preparePropertiesForCreateConnection() {
        Properties prop4ForCreateConnection = preparePropertiesForConnection();
        Enumeration<String> propertyNames = (Enumeration<String>) parameters4CreateAndExecute.propertyNames();

        prop4ForCreateConnection.put("user"    , parameters4CreateAndExecute.getProperty("jdbc.username.root"));
        prop4ForCreateConnection.put("username", parameters4CreateAndExecute.getProperty("jdbc.username.root"));
        prop4ForCreateConnection.put("password", parameters4CreateAndExecute.getProperty("jdbc.password.root"));


        logger.debug("preparePropertiesForCreateConnection: prop4Connection=" + prop4ForCreateConnection);

        return prop4ForCreateConnection;
    }

    protected Connection createConnectionForCreate() throws IllegalStateException, SQLException {
        Connection conn = null;
        try {
            logger.debug("createConnectionForCreate: ...try get Connection for Create DB.");
            Class.forName(parameters4CreateAndExecute.getProperty(PARAM_CONNECTION_JDBC_CLASS_DRIVER)).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex.getMessage());
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex.getMessage());
        }

        logger.debug("createConnectionForCreate:Ok, Loaded JDBC Driver.");
        String urlConnection = parameters4CreateAndExecute.getProperty("jdbc.url.root");
        logger.debug("createConnectionForCreate:urlConnection=" + urlConnection);
        
        conn = DriverManager.getConnection(urlConnection, preparePropertiesForCreateConnection());
        logger.debug("createConnectionForCreate:Connected to DB.");
        return conn;
    }

    public static void main(String args[]) {
        DBInstaller dbInstaller;
        try {
            dbInstaller = new MySQLDBInstaller(args[0]);
            dbInstaller.shellDB();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
