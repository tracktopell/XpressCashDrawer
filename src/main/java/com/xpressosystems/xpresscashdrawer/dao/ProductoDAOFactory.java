/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.dao.memory.ProductoDAOInMemory;

/**
 *
 * @author alfredo
 */
public class ProductoDAOFactory {
	
	private static ProductoDAO productoDAO;
	
	public static ProductoDAO getProductoDAO(){
		if(productoDAO == null){
			productoDAO = new ProductoDAOInMemory(); 
		}
		return productoDAO;
	}
}
