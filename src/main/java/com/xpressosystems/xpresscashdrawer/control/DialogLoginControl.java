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
	final int MAX_INTENTOS = 3;
	int intentos;
	boolean isAdmin;

	private static DialogLoginControl instance;

	public static DialogLoginControl getInstance(DialogLogin dialogLogin) {
		if(instance == null){
			instance = new DialogLoginControl(dialogLogin);
		}
		return instance;
	}
	
	
	
	private DialogLoginControl(DialogLogin dialogLogin) {
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
			
			ApplicationLogic.getInstance().setAdminLogedIn(isAdmin);					
			if(isAdmin) {
				JOptionPane.showMessageDialog(dialogLogin, "¡ Bienvenido al Sistema Administrador!", "Entrar", JOptionPane.INFORMATION_MESSAGE);			
			} else {
				JOptionPane.showMessageDialog(dialogLogin, "¡ Bienvenido al Sistema Usuario!", "Entrar", JOptionPane.INFORMATION_MESSAGE);						
			}
			
			dialogLogin.dispose();
		}
	}
	
	private boolean validate(){
		
		String passwordValue = new String(dialogLogin.getPassword().getPassword());
		if(passwordValue.trim().length()<1){
			return false;
		}
		isAdmin = ApplicationLogic.getInstance().checkForAdmin(passwordValue);
		if(! isAdmin){
			return ApplicationLogic.getInstance().checkForUser(passwordValue);			
		} else {
			return isAdmin;
		}
		
	}

	public boolean isLoggedIn() {
		return leggedIn;
	}

	public boolean isAdminLogedIn() {
		return isAdmin;
	}
}
