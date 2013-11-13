/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import javax.swing.JComponent;

/**
 *
 * @author Softtek
 */
class ValidatioException extends Exception {
	JComponent source;

	public ValidatioException(String message,JComponent source) {
		super(message);
		this.source = source;
	}
	
	public JComponent getSource() {
		return source;
	}

}
