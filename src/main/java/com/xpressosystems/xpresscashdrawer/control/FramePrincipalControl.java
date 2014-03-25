/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.view.DialogConfiguracionBTImpresora;
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
		
		framePrincipal.setTitle("XpressCashdrawer ["+ApplicationLogic.getInstance().getVersion()+"]- "+ApplicationLogic.getInstance().getNombreNegocio());
		
		panelVentaControl  = new PanelVentaControl ((PanelVenta)framePrincipal.getPanelVenta());
		
		panelProductosControl = new PanelProductosControl((PanelProductos)framePrincipal.getPanelProductos()) ;

		panelVentasControl = new PanelVentasControl((PanelVentas)framePrincipal.getPanelVentas()) ;

		framePrincipal.getProductosMenu().addActionListener(this);
		
		framePrincipal.getVentasMenu().addActionListener(this);
		
		framePrincipal.getSalirMenu().addActionListener(this);
		
		//---------------------------------------------------
		
		framePrincipal.getVentaActualMenu().addActionListener(this);
		
		framePrincipal.getVentaTerminarMenu().addActionListener(this);
		
		framePrincipal.getVentaeliminarProdMenu().addActionListener(this);
		
		framePrincipal.getVentaCancelarMenu().addActionListener(this);
		//---------------------------------------------------
		
		framePrincipal.getNegocioConfigMenu().addActionListener(this);

		framePrincipal.getUsuarioAdminMenu().addActionListener(this);

		framePrincipal.getUsuarioCajaMenu().addActionListener(this);

		framePrincipal.getImpresoraBTMenu().addActionListener(this);
		//---------------------------------------------------
		
		framePrincipal.setDefaultCloseOperation(framePrincipal.EXIT_ON_CLOSE);
		
		framePrincipal.getConfigMenu().setEnabled(false);
		
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
		} else if(e.getSource() == framePrincipal.getVentaActualMenu()){
			ventaActualMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaTerminarMenu()){
			ventaTerminar_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaeliminarProdMenu()){
			ventaeliminarProdMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getVentaCancelarMenu()){
			ventaCancelarMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getNegocioConfigMenu()){
			negocioConfigMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getUsuarioAdminMenu()){
			usuarioAdminMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getUsuarioCajaMenu()){
			usuarioCajaMenu_actionPerformed();
		} else if(e.getSource() == framePrincipal.getImpresoraBTMenu()){
			impresoraBTMenu_actionPerformed();
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
		panelProductosControl.estadoInicial();
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

	private void ventaActualMenu_actionPerformed() {
		((CardLayout)framePrincipal.getPanels().getLayout()).show(framePrincipal.getPanels(), "panelVenta");
		panelVentaControl.verVentaActual();
	}

	private void ventaTerminar_actionPerformed() {
		((CardLayout)framePrincipal.getPanels().getLayout()).show(framePrincipal.getPanels(), "panelVenta");
	}	
	
	private void negocioConfigMenu_actionPerformed() {
		DialogConfiguracionSistemaControl.getInstance(framePrincipal).estadoInicial();
	}
	
	private void impresoraBTMenu_actionPerformed() {
		DialogConfiguracionBTImpresoraControl.getInstance(framePrincipal).estadoInicial();		
	}

	private void usuarioAdminMenu_actionPerformed() {
		DialogConfiguracionPasswordControl.getInstance(framePrincipal,
				DialogConfiguracionPasswordControl.UPDATE_FOR_ADMIN).estadoInicial();
	}

	private void usuarioCajaMenu_actionPerformed() {
		DialogConfiguracionPasswordControl.getInstance(framePrincipal,
				DialogConfiguracionPasswordControl.UPDATE_FOR_USER).estadoInicial();
	}	

	public void enableAndDisableAdminControls() {
		final PanelProductos pp = (PanelProductos)framePrincipal.getPanelProductos();
		if(ApplicationLogic.getInstance().isAdminLogedIn()){
			System.err.println("--->> ADMIN HABILITANDO");
			framePrincipal.getConfigMenu().setEnabled(true);
			
			pp.getEditar().setEnabled(true);
			pp.getEliminar().setEnabled(true);
			pp.getNuevo().setEnabled(true);
		}else{					
			System.err.println("--->> NORMAL HABILITANDO");
			framePrincipal.getConfigMenu().setEnabled(false);
			
			pp.getEditar().setEnabled(false);
			pp.getEliminar().setEnabled(false);
			pp.getNuevo().setEnabled(false);
		}
		
	}

	public void setEnabledVentasMenus(boolean e){
		
		framePrincipal.getVentaTerminarMenu().setEnabled(e);
		
		framePrincipal.getVentaeliminarProdMenu().setEnabled(e);
		
		framePrincipal.getVentaCancelarMenu().setEnabled(e);
	}
}
