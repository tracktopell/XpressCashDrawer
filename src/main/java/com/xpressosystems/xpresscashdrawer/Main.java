/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer;

import com.xpressosystems.xpresscashdrawer.control.FramePrincipalControl;
import org.apache.log4j.Logger;

/**
 *
 * @author alfredo
 */
public class Main {
	private static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {		
		FramePrincipalControl framePrincipalControl = FramePrincipalControl.getInstance();
		framePrincipalControl.setNombreNegocio("XX");
		framePrincipalControl.estadoInicial();
	}
	
}
