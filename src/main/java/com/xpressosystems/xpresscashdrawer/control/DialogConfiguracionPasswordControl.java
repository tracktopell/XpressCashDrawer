/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.view.DialogConfiguracionPassword;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Softtek
 */
public class DialogConfiguracionPasswordControl implements ActionListener {

	DialogConfiguracionPassword dialogConfiguracionPassword;
	private static DialogConfiguracionPasswordControl instance;
	public static final int UPDATE_FOR_ADMIN = 1;
	public static final int UPDATE_FOR_USER = 2;
	private int updateMode = 0;

	public static DialogConfiguracionPasswordControl getInstance(JFrame parent, int updateMode) {
		if (instance == null) {
			instance = new DialogConfiguracionPasswordControl(parent);
		}
		instance.updateMode = updateMode;

		return instance;
	}

	private DialogConfiguracionPasswordControl(JFrame parent) {
		this.dialogConfiguracionPassword = new DialogConfiguracionPassword(parent);

		this.dialogConfiguracionPassword.getAceptar().addActionListener(this);
		this.dialogConfiguracionPassword.getCancelar().addActionListener(this);
	}

	public void estadoInicial() {
		if (updateMode == UPDATE_FOR_ADMIN) {
			dialogConfiguracionPassword.setTitle("Cambiar contraseña de Administrador");
		} else if (updateMode == UPDATE_FOR_ADMIN) {
			dialogConfiguracionPassword.setTitle("Cambiar contraseña de Usario de Caja");
		}

		dialogConfiguracionPassword.getActual().setText("");
		dialogConfiguracionPassword.getNuevo().setText("");
		dialogConfiguracionPassword.getRepetir().setText("");

		dialogConfiguracionPassword.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialogConfiguracionPassword.getAceptar()) {
			aceptar_actionPerformed();
		} else if (e.getSource() == dialogConfiguracionPassword.getCancelar()) {
			cancelar_actionPerformed();
		}
	}

	private void aceptar_actionPerformed() {
		System.err.println("->>aceptar_actionPerformed: ");
		try {
			validate();

			if (updateMode == UPDATE_FOR_ADMIN) {
				ApplicationLogic.getInstance().updateForAdmin(new String(dialogConfiguracionPassword.getNuevo().getPassword()));
			} else if (updateMode == UPDATE_FOR_ADMIN) {
				ApplicationLogic.getInstance().updateForUser(new String(dialogConfiguracionPassword.getNuevo().getPassword()));
			}
			System.err.println("->>aceptar_actionPerformed: Ok, updated !");

			dialogConfiguracionPassword.dispose();
		} catch (ValidatioException ve) {
			JOptionPane.showMessageDialog(dialogConfiguracionPassword, ve.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ve.getSource().requestFocus();
		}
	}

	private void cancelar_actionPerformed() {
		System.err.println("->>cancelar_actionPerformed");
		dialogConfiguracionPassword.dispose();
	}

	private void validate() throws ValidatioException {
		String actualValue = new String(dialogConfiguracionPassword.getActual().getPassword());
		String nuevoValue = new String(dialogConfiguracionPassword.getNuevo().getPassword());
		String repetirlValue = new String(dialogConfiguracionPassword.getRepetir().getPassword());

		if (actualValue.trim().length() < 2) {
			throw new ValidatioException("Escriba la contraseña actual", dialogConfiguracionPassword.getActual());
		}
		if (nuevoValue.trim().length() < 2) {
			throw new ValidatioException("Escriba la contraseña nueva", dialogConfiguracionPassword.getNuevo());
		}
		if (repetirlValue.trim().length() < 2) {
			throw new ValidatioException("Repita la contraseña nueva", dialogConfiguracionPassword.getRepetir());
		}

		if (updateMode == UPDATE_FOR_ADMIN) {
			if (!ApplicationLogic.getInstance().checkForAdmin(actualValue)) {
				throw new ValidatioException("Error al actualizar", dialogConfiguracionPassword.getActual());
			}
		} else if (updateMode == UPDATE_FOR_ADMIN) {
			if (!ApplicationLogic.getInstance().checkForUser(actualValue)) {
				throw new ValidatioException("Error al actualizar", dialogConfiguracionPassword.getActual());
			}
		}
		if (nuevoValue.equals(actualValue)) {
			throw new ValidatioException("La contraseña actual no pude ser la misma que la nueva", dialogConfiguracionPassword.getActual());
		}
		if (!nuevoValue.equals(repetirlValue)) {
			throw new ValidatioException("Las contraseñas no son identicas", dialogConfiguracionPassword.getActual());
		}

	}
}
