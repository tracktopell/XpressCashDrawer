/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.dao.memory.VentaDAOInMemory;


/**
 *
 * @author alfredo
 */
public class VentaDAOFactory {
	
	private static VentaDAO ventaDAO;
	
	public static VentaDAO getVentaDAO(){
		if(ventaDAO == null){
			ventaDAO = new VentaDAOInMemory(); 
		}
		return ventaDAO;
	}
}
