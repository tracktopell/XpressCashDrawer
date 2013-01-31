/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author alfredo
 */
public class CantidadCellRender extends DefaultTableCellRenderer {
	private DecimalFormat df;
	public CantidadCellRender() {
		df = new DecimalFormat("#######0");		
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(
				table, df.format(value), isSelected, hasFocus, row, column);
		
		return cell;
	}
}
