/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

/**
 *
 * @author alfredo
 */
public class DetalleVentaTableItem {
	private Producto producto;
	private DetalleVenta detalleVenta;

	public DetalleVentaTableItem(Producto producto, DetalleVenta detalleVenta) {
		this.producto = producto;
		this.detalleVenta = detalleVenta;
		this.detalleVenta.setCantidad(1);
	}
	
	
	public int getCantidad() {
		return detalleVenta.getCantidad();
	}

	public void setCantidad(int cantidad) {
		this.detalleVenta.setCantidad(cantidad);
	}
	
	public String getCodigo() {
		return producto.getCodigo();
	}

	public String getNombre() {
		return producto.getNombre();
	}
	
	public String getLinea() {
		return producto.getLinea();
	}
	
	public String getMarca() {
		return producto.getMarca();
	}

	public Double getPrecioVenta() {
		return producto.getPrecioVenta();
	}
	
	public Double getImporete() {
		return detalleVenta.getCantidad() * producto.getPrecioVenta();
	}

	/**
	 * @return the producto
	 */
	public Producto getProducto() {
		return producto;
	}

	/**
	 * @return the detalleVenta
	 */
	public DetalleVenta getDetalleVenta() {
		return detalleVenta;
	}

}
