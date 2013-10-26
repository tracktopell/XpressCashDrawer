/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao;

import com.xpressosystems.xpresscashdrawer.model.Preferencia;
import java.util.List;

/**
 *
 * @author alfredo
 */
public interface PreferenciaDAO {
	List<Preferencia> getAll();
	Preferencia  getPreferencia(String id);
	Preferencia  edit(Preferencia preferencia) throws EntidadInexistenteException;
	Preferencia  persist(Preferencia preferencia) throws EntidadExistenteException;
	Preferencia  delete(Preferencia preferencia) throws EntidadInexistenteException;
}
