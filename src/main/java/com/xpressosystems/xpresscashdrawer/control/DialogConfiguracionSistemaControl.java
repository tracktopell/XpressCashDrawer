/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.view.DialogConfiguracionSistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author Softtek
 */
public class DialogConfiguracionSistemaControl implements ActionListener {
	DialogConfiguracionSistema dialogConfiguracionSistema;
	
	private static DialogConfiguracionSistemaControl instance;

	public static DialogConfiguracionSistemaControl getInstance(JFrame parent) {
		if(instance == null){
			instance = new DialogConfiguracionSistemaControl(parent);
		}
		return instance;
	}
	
	private DialogConfiguracionSistemaControl(JFrame parent) {
		this.dialogConfiguracionSistema = new DialogConfiguracionSistema(parent);

		this.dialogConfiguracionSistema.getAceptar().addActionListener(this);
		this.dialogConfiguracionSistema.getCancelar().addActionListener(this);
	}

	public void estadoInicial() {
		dialogConfiguracionSistema.getNombreNegocio()	 .setText(ApplicationLogic.getInstance().getNombreNegocio());
		dialogConfiguracionSistema.getDireccion()		 .setText(ApplicationLogic.getInstance().getDireccion());
		dialogConfiguracionSistema.getTelefonos()		 .setText(ApplicationLogic.getInstance().getTelefonos());
		dialogConfiguracionSistema.getEmail()			 .setText(ApplicationLogic.getInstance().getEmail());
		dialogConfiguracionSistema.getClientePorDefault().setText(ApplicationLogic.getInstance().getCliente());
		
		//dialogConfiguracionSistema.getAceptar().setEnabled(false);
		//dialogConfiguracionSistema.getCancelar().setEnabled(false);
		
		dialogConfiguracionSistema.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialogConfiguracionSistema.getAceptar()) {
			aceptar_actionPerformed();
		} else if (e.getSource() == dialogConfiguracionSistema.getCancelar()) {
			cancelar_actionPerformed();
		}
	}
	
	private void aceptar_actionPerformed() {
		System.err.println("->>aceptar_actionPerformed: ");
		if(!validate()){
			return;
		}		
		ApplicationLogic.getInstance().setNombreNegocio(dialogConfiguracionSistema.getNombreNegocio().getText());
		ApplicationLogic.getInstance().setDireccion(dialogConfiguracionSistema.getDireccion().getText());
		ApplicationLogic.getInstance().setTelefonos(dialogConfiguracionSistema.getTelefonos().getText());
		ApplicationLogic.getInstance().setEmail(dialogConfiguracionSistema.getEmail().getText());
		ApplicationLogic.getInstance().setCliente(dialogConfiguracionSistema.getClientePorDefault().getText());

		dialogConfiguracionSistema.dispose();
	}

	private void cancelar_actionPerformed() {
		System.err.println("->>cancelar_actionPerformed");
		dialogConfiguracionSistema.dispose();
	}

	private boolean validate() {
		return true;		
	}

}
