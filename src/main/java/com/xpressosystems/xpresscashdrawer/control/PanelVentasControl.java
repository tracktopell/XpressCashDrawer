/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAO;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.FechaCellRender;
import com.xpressosystems.xpresscashdrawer.model.ImporteCellRender;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import com.xpressosystems.xpresscashdrawer.model.VentaTableModel;
import com.xpressosystems.xpresscashdrawer.view.PanelVentas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;
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
	private ImporteCellRender importeCellRender;
	private FechaCellRender fechaCellRender;
	public PanelVentasControl(PanelVentas panelVentas) {
		this.panelVentas = panelVentas;
		ventasTM = (VentaTableModel)this.panelVentas.getVentasJTable().getModel();
		ventasTM.addTableModelListener( this);
		panelVentas.getVentasJTable().addMouseListener(this);
		panelVentas.getVentasJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		System.err.println("->>table columns="+panelVentas.getVentasJTable().getColumnCount());
		
		importeCellRender = new ImporteCellRender();
		fechaCellRender = new FechaCellRender();
		
		importeCellRender.setHorizontalAlignment(SwingConstants.RIGHT);
		
		ventaDAO = VentaDAOFactory.getVentaDAO();
		
	}
	
	public void refrescar() {
		VentaDAO ventaDAO = VentaDAOFactory.getVentaDAO();
		List<Venta> ventaList  = ventaDAO.getAll(); 		
		Hashtable<Integer,Double> ventaImporteList = new Hashtable<Integer,Double>();
		for(Venta v: ventaList){
			double t=0.0;
			try {
				List<DetalleVenta> detalleVenta = ventaDAO.getDetalleVenta(v.getId());
				for(DetalleVenta dv: detalleVenta){
					t += dv.getPrecioVenta() * dv.getCantidad();
				}
			} catch (EntidadInexistenteException ex) {
				
			}
			
			ventaImporteList.put(v.getId(), t);
		}
		ventasTM = new VentaTableModel(ventaList, ventaImporteList);
		panelVentas.getVentasJTable().setModel(ventasTM);
		final int tw = panelVentas.getVentasJTable().getWidth();
		int[] cws = new int[]{10,60,30};
		int cw;
		System.err.println("->panelVentas.getVentasJTable().getSize()="+tw);
		int i=0;
		for(int cwi : cws) {
			cw = (tw * cwi)/100;
			System.err.println("->\tpanelVentas.getVentasJTable().column["+i+"] PreferredWidth:"+cw);
		
			panelVentas.getVentasJTable().getColumnModel().getColumn(i++).setPreferredWidth(cw);
		}
		
		panelVentas.getVentasJTable().getColumnModel().getColumn(1).setCellRenderer(fechaCellRender);		
		panelVentas.getVentasJTable().getColumnModel().getColumn(2).setCellRenderer(importeCellRender);		
		panelVentas.getVentasJTable().updateUI();
	}
	
	public void estadoInicial(){
		refrescar();
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
