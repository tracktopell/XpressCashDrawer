/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.PreferenciaDAO;
import com.xpressosystems.xpresscashdrawer.dao.PreferenciaDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.Preferencia;
import java.util.List;

/**
 *
 * @author Softtek
 */
public class ApplicationLogic {
	private static ApplicationLogic instance;
	private static PreferenciaDAO preferenciaDAO;
	private ApplicationLogic(){
	
	}

	public String getNombreNegocio() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("NOMBRE_NEGOCIO");
		return preferencia.getValor();
	}

	public String getDireccion() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("DIRECCION_NEGOCIO");
		return preferencia.getValor();
	}

	public String getTelefonos() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("TELEFONOS_NEGOCIO");
		return preferencia.getValor();
	}

	public String getCliente() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("CLIENTE_DEFAULT");
		return preferencia.getValor();
	}

	public String getBTImpresora() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
			List<Preferencia> all = preferenciaDAO.getAll();
			for(Preferencia p: all){
				System.err.println("\t->Preferencia:"+p);
			}
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("BLUETOOTHADDRESS_PRINTER");
		System.err.println("->getBTImpresora():"+preferencia.getValor());
		return preferencia.getValor();
	}
	
	public void setBTImpresora(String btaImpresora) {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = new Preferencia("BLUETOOTHADDRESS_PRINTER",btaImpresora);
		try {
			preferenciaDAO.edit(preferencia);
		} catch (EntidadInexistenteException ex) {
			
		}
	}

	/**
	 * @return the instance
	 */
	public static ApplicationLogic getInstance() {
		if(instance == null){
			instance =  new ApplicationLogic();
		}
		return instance;
	}
	
}
