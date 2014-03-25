/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.dao.jdbc.DataSourceAdaptor;
import com.xpressosystems.xpresscashdrawer.dao.jdbc.VentaDAOJDBC;
import java.sql.SQLException;

/**
 *
 * @author alfredo
 */
public class VentaDAOFactory {

	private static VentaDAO ventaDAO;

	public static VentaDAO getVentaDAO() {
		if (ventaDAO == null) {
			try {
				ventaDAO = new VentaDAOJDBC(DataSourceAdaptor.getConnection());
			} catch (SQLException ex) {
			}
		}
		return ventaDAO;
	}
}
