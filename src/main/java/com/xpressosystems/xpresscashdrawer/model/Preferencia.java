/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

/**
 *
 * @author Softtek
 */
public class Preferencia {
	private String id;
	private String valor;

	public Preferencia() {
		id="";
		valor="";
	}

	public Preferencia(String id, String valor) {
		this.id = id;
		this.valor = valor;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return id+"="+valor;
	}
	
	
}
