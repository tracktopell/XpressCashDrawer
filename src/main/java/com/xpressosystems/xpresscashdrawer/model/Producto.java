/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

/**
 *
 * @author alfredo
 */
public class Producto {
	private String codigo;
	private String nombre;
	private String linea;
	private String marca;
	private Double costo;
	private Double precioVenta;
	private int    existencia;
	private int    piezasXCaja;	

	public Producto(){
		codigo = "";
		nombre = "";
		linea = "";
		marca = "";
		costo = 1.0;
		precioVenta= 2.0;
		existencia  = 1;
		piezasXCaja = 1;		
	}
	
	public Producto(String codigo, String nombre, String linea, String marca, Double costo, Double precioVenta, int existencia, int piezasXCaja) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.linea = linea;
		this.marca = marca;
		this.costo = costo;
		this.precioVenta = precioVenta;
		this.existencia = existencia;
		this.piezasXCaja = piezasXCaja;
	}

	
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the linea
	 */
	public String getLinea() {
		return linea;
	}

	/**
	 * @param linea the linea to set
	 */
	public void setLinea(String linea) {
		this.linea = linea;
	}

	/**
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * @param marca the marca to set
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * @return the costo
	 */
	public Double getCosto() {
		return costo;
	}

	/**
	 * @param costo the costo to set
	 */
	public void setCosto(Double costo) {
		this.costo = costo;
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

	/**
	 * @return the existencia
	 */
	public int getExistencia() {
		return existencia;
	}

	/**
	 * @param existencia the existencia to set
	 */
	public void setExistencia(int existencia) {
		this.existencia = existencia;
	}

	/**
	 * @return the piezasXCaja
	 */
	public int getPiezasXCaja() {
		return piezasXCaja;
	}

	/**
	 * @param piezasXCaja the piezasXCaja to set
	 */
	public void setPiezasXCaja(int piezasXCaja) {
		this.piezasXCaja = piezasXCaja;
	}
}
