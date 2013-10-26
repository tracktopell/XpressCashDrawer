/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.dao.jdbc.DataSourceAdaptor;
import com.xpressosystems.xpresscashdrawer.dao.jdbc.ProductoDAOJDBC;
import java.sql.SQLException;

/**
 *
 * @author alfredo
 */
public class ProductoDAOFactory {

	private static ProductoDAO productoDAO;

	public static ProductoDAO getProductoDAO() {
		if (productoDAO == null) {
			try {
				productoDAO = new ProductoDAOJDBC(DataSourceAdaptor.getConnection());
			} catch (SQLException ex) {
			}
		}
		return productoDAO;
	}
}
