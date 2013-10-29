/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer;

import com.xpressosystems.xpresscashdrawer.control.ApplicationLogic;
import com.xpressosystems.xpresscashdrawer.control.FramePrincipalControl;
import com.xpressosystems.xpresscashdrawer.dao.jdbc.DataSourceAdaptor;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
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
		FramePrincipalControl framePrincipalControl = null;
		
		isSingleInstanceRunning();
		
		ApplicationLogic.getInstance().needsUpdateApplciation();
		
		try {
			framePrincipalControl = FramePrincipalControl.getInstance();
			
			framePrincipalControl.setNombreNegocio("xpresscashdrawer store");
			
			framePrincipalControl.estadoInicial();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private static void isSingleInstanceRunning(){
		Connection conn = null;
		try{
			conn = DataSourceAdaptor.getConnection();			
		} catch(Exception ex){
			ex.printStackTrace(System.err);
		} finally {
			if(conn == null) {
				System.err.println("-->> Another instance is running !");
				
				JOptionPane.showMessageDialog(null, "Ya esta corriendo la Aplicación en otra ventana", "Iniciar", JOptionPane.ERROR_MESSAGE);
				
				System.exit(1);
			}
		}
	}
}
