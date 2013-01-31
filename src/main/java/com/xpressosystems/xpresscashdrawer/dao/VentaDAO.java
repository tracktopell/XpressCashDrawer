/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import java.util.List;

/**
 *
 * @author alfredo
 */
public interface VentaDAO {
	List<Venta> getAll();
	List<DetalleVenta> getDetalleVenta(Integer ventaId)throws EntidadInexistenteException;

	Venta  getVenta(Integer ventaId);
	Venta  edit(Venta venta,List<DetalleVenta> detalleVentaList) throws EntidadInexistenteException;
	Venta  persist(Venta producto,List<DetalleVenta> detalleVentaList) throws EntidadExistenteException;
	Venta  delete(Venta venta) throws EntidadInexistenteException;
}
