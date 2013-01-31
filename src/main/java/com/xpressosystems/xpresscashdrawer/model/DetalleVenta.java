/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

/**
 *
 * @author alfredo
 */
public class DetalleVenta {
	private int id;
	private int ventaId;
	private String productoCodigo;
	private int cantidad;
	private Double precioVenta;

	public DetalleVenta(int id, int ventaId, String productoCodigo, int cantidad, Double precioVenta) {
		this.id = id;
		this.ventaId = ventaId;
		this.productoCodigo = productoCodigo;
		this.cantidad = cantidad;
		this.precioVenta = precioVenta;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the ventaId
	 */
	public int getVentaId() {
		return ventaId;
	}

	/**
	 * @param ventaId the ventaId to set
	 */
	public void setVentaId(int ventaId) {
		this.ventaId = ventaId;
	}

	/**
	 * @return the productoCodigo
	 */
	public String getProductoCodigo() {
		return productoCodigo;
	}

	/**
	 * @param productoCodigo the productoCodigo to set
	 */
	public void setProductoCodigo(String productoCodigo) {
		this.productoCodigo = productoCodigo;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the precioVenta
	 */
	public Double getPrecioVenta() {
		return precioVenta;
	}

	/**
	 * @param precioVenta the precioVenta to set
	 */
	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}

}
