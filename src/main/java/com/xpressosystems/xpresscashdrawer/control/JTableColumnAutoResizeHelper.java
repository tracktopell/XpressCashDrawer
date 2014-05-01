/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JTable;

/**
 *
 * @author Softtek
 */
public class JTableColumnAutoResizeHelper extends ComponentAdapter {

	private int[] ws;

	public JTableColumnAutoResizeHelper(int[] ws) {
		this.ws = ws;
	}

	public void componentResized(ComponentEvent e) {
		JTable tbl = (JTable) e.getComponent();

		int w = (int) tbl.getSize().getWidth();
		int tc = tbl.getColumnCount();
		int wc = ws.length;
		int wi = 0;
		for (int i = 0; i < wc && i < tc; i++) {
			wi = (ws[i] * w) / 100;
			tbl.getColumnModel().getColumn(i).setPreferredWidth(wi);
		}
		tbl.updateUI();
	}
}
