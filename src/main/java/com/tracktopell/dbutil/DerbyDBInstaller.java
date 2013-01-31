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
public class DerbyDBInstaller extends DBInstaller {

    public DerbyDBInstaller(String proConfigLocation,String masterHost) throws IOException {
        super(proConfigLocation,masterHost);
        //setDBSystemDir();
    }

    private void setDBSystemDir() {
        // Decide on the db system directory: <userhome>/.addressbook/
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + File.separator+"."+parameters4CreateAndExecute.getProperty("app.dataDirectory","app.dataDirectory");

        // Set the db system directory.
        System.setProperty("derby.system.home", systemDir);
        logger.debug("setDBSystemDir: derby.system.home="+systemDir);
    }
	
    protected Properties preparePropertiesForCreateConnection() {
        Properties prop4ForCreateConnection = preparePropertiesForConnection();
        Enumeration<String> propertyNames = (Enumeration<String>) parameters4CreateAndExecute.propertyNames();

        while (propertyNames.hasMoreElements()) {
            String propName = propertyNames.nextElement();
            if (propName.startsWith("jdbc.urlcreateconn.create")) {
                prop4ForCreateConnection.put(propName, parameters4CreateAndExecute.getProperty(propName));
            }
        }

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
        String urlConnection = parameters4CreateAndExecute.getProperty("jdbc.url");
        logger.debug("createConnectionForCreate:urlConnection=" + urlConnection);

        if (urlConnection.contains("${db.name}")) {
            urlConnection = urlConnection.replace("${db.name}", parameters4CreateAndExecute.getProperty("db.name"));
            logger.debug("createConnectionForCreate:replacement for variable db.name, now urlConnection=" + urlConnection);
        }

        if (parameters4CreateAndExecute.containsKey("jdbc.urlcreateconn.create")) {
            urlConnection = urlConnection + parameters4CreateAndExecute.getProperty("jdbc.urlcreateconn.create");
            logger.debug("createConnectionForCreate:add crerate attribute, now urlConnection=" + urlConnection);
        }

        conn = DriverManager.getConnection(urlConnection, preparePropertiesForCreateConnection());
        logger.debug("createConnectionForCreate:Connected to DB.");
        return conn;
    }

    public static void main(String args[]) {
        DBInstaller dbInstaller;
        try {
            dbInstaller = new DerbyDBInstaller(args[0],args.length==2?args[0].trim():null);
            dbInstaller.shellDB();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
