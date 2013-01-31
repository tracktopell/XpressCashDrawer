/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author alfredo
 */
public class FechaCellRender extends DefaultTableCellRenderer {
	private SimpleDateFormat df;
	public FechaCellRender() {
		df = new SimpleDateFormat("dd/MMM/yyyy hh:mm");		
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(
				table, df.format(value), isSelected, hasFocus, row, column);
		
		return cell;
	}
}
