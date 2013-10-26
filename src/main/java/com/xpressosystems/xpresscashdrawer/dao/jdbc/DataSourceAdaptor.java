/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao.jdbc;

import com.tracktopell.dbutil.DBInstaller;
import com.tracktopell.dbutil.DerbyDBInstaller;
import com.xpressosystems.xpresscashdrawer.Main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Softtek
 */
public class DataSourceAdaptor {
	private static Logger logger = Logger.getLogger(DataSourceAdaptor.class);
	private static Connection conn = null;
	
	public static Connection getConnection() throws SQLException{
		logger.info("->getConnection():");
		if(conn == null) {
			DBInstaller dbi = null;
			try {
				dbi = new DerbyDBInstaller("classpath:/jdbc.properties", null);
			} catch (IOException ex) {
				logger.error("Error:",ex);
				ex.printStackTrace(System.err);
				return null;
			}
			Connection connectionForInit = dbi.getExistDB();

			if (connectionForInit == null) {
				logger.info("The DB does'nt exist -> installDBfromScratch !");
				connectionForInit = dbi.getInstallDBfromScratch();
			}

			logger.info("<-getConnection():OK connectionForInit=" + connectionForInit);
			conn = connectionForInit;
		}
		return conn;
	}
}
