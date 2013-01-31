/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.view.FramePrincipal;
import com.xpressosystems.xpresscashdrawer.view.PanelProductos;
import com.xpressosystems.xpresscashdrawer.view.PanelVenta;
import com.xpressosystems.xpresscashdrawer.view.PanelVentas;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author alfredo
 */
public class FramePrincipalControl implements ActionListener{

	FramePrincipal    framePrincipal;
	PanelVentaControl panelVentaControl;
	PanelProductosControl panelProductosControl;
	PanelVentasControl panelVentasControl;
	
	private static FramePrincipalControl instance;
	/**
	 * @return the instance
	 */
	public static FramePrincipalControl getInstance() {
		if(instance == null){
			instance = new FramePrincipalControl();
		}
		return instance;
	}
	
	private FramePrincipalControl() {
		
		framePrincipal = new FramePrincipal();
		
		panelVentaControl  = new PanelVentaControl ((PanelVenta)framePrincipal.getPanelVenta());
		
		panelProductosControl = new PanelProductosControl((PanelProductos)framePrincipal.getPanelProductos()) ;

		panelVentasControl = new PanelVentasControl((PanelVentas)framePrincipal.getPanelVentas()) ;

		framePrincipal.getProductosMenu().addActionListener(this);
		
		framePrincipal.getVentasMenu().addActionListener(this);
		
		framePrincipal.getSalirMenu().addActionListener(this);
		
		//---------------------------------------------------
		
		framePrincipal.getVentaNuevaMenu().addActionListener(this);
		
		framePrincipal.getVentaTerminarMenu().addActionListener(this);
		
		framePrincipal.getVentaeliminarProdMenu().addActionListener(this);
		
		framePrincipal.getVentaCancelarMenu().addActionListener(this);
		//---------------------------------------------------
		
		framePrincipal.setDefaultCloseOperation(framePrincipal.EXIT_ON_CLOSE);
		
	}
	
	public void estadoInicial() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				framePrincipal.setVisible(true);
				panelVentaControl.estadoInicial();
			}
		});
	}

	public FramePrincipal getFramePrincipal() {
		return framePrincipal;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == framePrincipal.getProductosMenu()){
			productosMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentasMenu()){
			ventasMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getSalirMenu()){
			salirMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaNuevaMenu()){
			ventaNuevaMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaTerminarMenu()){
			ventaTerminar_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaeliminarProdMenu()){
			ventaeliminarProdMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaCancelarMenu()){
			ventaCancelarMenu_actionPerformed();
		} 
	}

	private void ventaeliminarProdMenu_actionPerformed() {
		panelVentaControl.ventaeliminarProdMenu();
	}

	private void ventaCancelarMenu_actionPerformed() {
		int r = JOptionPane.showConfirmDialog(framePrincipal, "¿Realmente desea Cancelar la Venta ?", "Venta",JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(r == JOptionPane.OK_OPTION){
			panelVentaControl.estadoInicial();
		}
	}	

	private void productosMenu_actionPerformed() {
		((CardLayout)framePrincipal.getPanels().getLayout()).show(framePrincipal.getPanels(), "panelProductos");
	}

	private void ventasMenu_actionPerformed() {
		panelVentasControl.estadoInicial();
		((CardLayout)framePrincipal.getPanels().getLayout()).show(framePrincipal.getPanels(), "panelVentas");
	}

	private void salirMenu_actionPerformed() {
		framePrincipal.dispose();
		System.exit(0);
	}

	private void ventaNuevaMenu_actionPerformed() {
		((CardLayout)framePrincipal.getPanels().getLayout()).show(framePrincipal.getPanels(), "panelVenta");
		panelVentaControl.ventaNueva();
	}

	private void ventaTerminar_actionPerformed() {
		((CardLayout)framePrincipal.getPanels().getLayout()).show(framePrincipal.getPanels(), "panelVenta");
	}	
}
