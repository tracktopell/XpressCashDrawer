/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer;

import com.tracktopell.dbutil.DBInstaller;
import com.tracktopell.dbutil.DerbyDBInstaller;
import com.xpressosystems.xpresscashdrawer.control.FramePrincipalControl;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author alfredo
 */
public class Main {
	private static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Connection c = getConnection();
		
		FramePrincipalControl framePrincipalControl = FramePrincipalControl.getInstance();
		framePrincipalControl.estadoInicial();
	}
	
	public static Connection getConnection(){
		System.err.println("->getConnection():");

		DBInstaller dbi = null;
		try {
			dbi = new DerbyDBInstaller("classpath:/jdbc.properties",null);
		} catch (IOException ex) {
			//logger.error("Error:",ex);
			ex.printStackTrace(System.err);
			return null;
		}
		Connection connectionForInit = dbi.getExistDB();
		
		if (connectionForInit == null) {
			System.err.println("The DB does'nt exist -> installDBfromScratch !");
			connectionForInit = dbi.getInstallDBfromScratch();
		}
		
		System.err.println("<-getConnection():OK connectionForInit="+connectionForInit);
		
		return connectionForInit;

	}


}
