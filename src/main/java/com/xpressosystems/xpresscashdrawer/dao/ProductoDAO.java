/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.model.Producto;
import java.util.List;

/**
 *
 * @author alfredo
 */
public interface ProductoDAO {
	List<Producto> getAll();
	Producto  getProducto(String codigo);
	Producto  edit(Producto producto) throws EntidadInexistenteException;
	Producto  persist(Producto producto) throws EntidadExistenteException;
	Producto  delete(Producto producto) throws EntidadInexistenteException;
}
