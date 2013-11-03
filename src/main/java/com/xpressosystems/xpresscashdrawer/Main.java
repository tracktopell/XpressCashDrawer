/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer;

import com.xpressosystems.xpresscashdrawer.control.ApplicationLogic;
import com.xpressosystems.xpresscashdrawer.control.FramePrincipalControl;
import com.xpressosystems.xpresscashdrawer.control.UpadateApplicationJFrameControl;
import com.xpressosystems.xpresscashdrawer.dao.jdbc.DataSourceAdaptor;
import com.xpressosystems.xpresscashdrawer.view.UpadateApplicationJFrame;
import java.sql.Connection;
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
		
		if( ApplicationLogic.getInstance().needsUpdateApplciation()) {
			UpadateApplicationJFrame uaf = new UpadateApplicationJFrame();
			UpadateApplicationJFrameControl uafc = new UpadateApplicationJFrameControl(uaf);
			uafc.estadoInicial();
		}
		
		try {
			framePrincipalControl = FramePrincipalControl.getInstance();
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
