/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.VentaDAO;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.FechaCellRender;
import com.xpressosystems.xpresscashdrawer.model.ImporteCellRender;
import com.xpressosystems.xpresscashdrawer.model.VentaTableModel;
import com.xpressosystems.xpresscashdrawer.view.PanelVentas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author alfredo
 */
public class PanelVentasControl implements ActionListener,TableModelListener,MouseListener{

	private PanelVentas panelVentas;
	private VentaDAO ventaDAO;
	private DecimalFormat df;
	VentaTableModel ventasTM;
	public PanelVentasControl(PanelVentas panelVentas) {
		this.panelVentas = panelVentas;
		ventasTM = (VentaTableModel)this.panelVentas.getVentasJTable().getModel();
		ventasTM.addTableModelListener( this);
		panelVentas.getVentasJTable().addMouseListener(this);
		panelVentas.getVentasJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		System.err.println("->>table columns="+panelVentas.getVentasJTable().getColumnCount());
		
		final ImporteCellRender importeCellRender = new ImporteCellRender();
		final FechaCellRender fechaCellRender = new FechaCellRender();
		
		importeCellRender.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panelVentas.getVentasJTable().getColumnModel().getColumn(1).setCellRenderer(fechaCellRender);		
		panelVentas.getVentasJTable().getColumnModel().getColumn(2).setCellRenderer(importeCellRender);
		
		ventaDAO = VentaDAOFactory.getVentaDAO();
		
	}
	
	public void estadoInicial(){
		//ventasTM.refrescar();
		panelVentas.getVentasJTable().updateUI();		
		panelVentas.getVentasJTable().getSelectionModel().clearSelection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		panelVentas.getVentasJTable().updateUI();		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}	
}