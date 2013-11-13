/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.view.DialogLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Softtek
 */
public class DialogLoginControl implements ActionListener{
	DialogLogin dialogLogin;
	private boolean leggedIn;

	public DialogLoginControl(DialogLogin dialogLogin) {
		this.dialogLogin = dialogLogin;
		this.dialogLogin.getAceptar().addActionListener(this);
		this.dialogLogin.getPassword().addActionListener(this);
	}
	
	public void estadoInicial(){
		this.dialogLogin.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialogLogin.getAceptar()) {
			aceptar_ActionPerformed();
		} else if (e.getSource() == dialogLogin.getPassword()) {
			aceptar_ActionPerformed();
		}		
	}
	
	final int MAX_INTENTOS = 3;
	int intentos;
	
	private void aceptar_ActionPerformed(){
		
		if(!validate()){
			dialogLogin.getPassword().setText("");
			JOptionPane.showMessageDialog(dialogLogin, "Contraseña incorrecta", "Entrar", JOptionPane.ERROR_MESSAGE);
			dialogLogin.getPassword().requestFocus();
			intentos++;
			if(intentos>=3){
				leggedIn = false;
				dialogLogin.dispose();
			}
		} else {
			leggedIn = true;
			JOptionPane.showMessageDialog(dialogLogin, "¡ Bienvenido al Sistema !", "Entrar", JOptionPane.INFORMATION_MESSAGE);			
			dialogLogin.dispose();
		}
	}
	
	private boolean validate(){
		
		String passwordValue = new String(dialogLogin.getPassword().getPassword());
		if(passwordValue.trim().length()<1){
			return false;
		}
		boolean isAdmin = ApplicationLogic.getInstance().checkForAdmin(passwordValue);
		if(! isAdmin){
			return ApplicationLogic.getInstance().checkForUser(passwordValue);
		} else {
			return true;
		}
		
	}

	public boolean isLoggedIn() {
		return leggedIn;
	}
}
