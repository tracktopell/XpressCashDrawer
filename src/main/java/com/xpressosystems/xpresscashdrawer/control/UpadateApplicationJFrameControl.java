/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.view.UpadateApplicationJFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author alfredo
 */
public class UpadateApplicationJFrameControl implements UpdateApplicationListener, ActionListener{
	
	private UpadateApplicationJFrame ua;	

	public UpadateApplicationJFrameControl(UpadateApplicationJFrame ua) {
		this.ua = ua;
		this.ua.getProgressUpdate().setValue(0);
		this.ua.getCancelar().addActionListener(this);
	}
	
	public void estadoInicial(){
		if(! ApplicationLogic.getInstance().canDownlaodUpdateApplication()){
			JOptionPane.showMessageDialog(null, "No se puede descargar la Actualización", "Actualizar", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ua.setVisible(true);
		ApplicationLogic.getInstance().updateApplication(this);
	}
	
	public void updateProgress(int percentAdvance){
		ua.getProgressUpdate().setValue(percentAdvance);
		if(percentAdvance > 99){
			this.ua.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == ua.getCancelar()){
			cancelar_actionPerformed();
		}
	}

	private void cancelar_actionPerformed() {
		ApplicationLogic.getInstance().cacellUpdateApplication();
		//this.ua.getCancelar().setEnabled(false);
	}	
}
