/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAO;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAOFactory;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAO;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.DetalleVentaTableItem;
import com.xpressosystems.xpresscashdrawer.model.DetalleVentaTableModel;
import com.xpressosystems.xpresscashdrawer.model.ImporteCellRender;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import com.xpressosystems.xpresscashdrawer.view.PanelVenta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author alfredo
 */
public class PanelVentaControl implements ActionListener,TableModelListener,MouseListener{

	private PanelVenta panelVenta;
	private List<DetalleVentaTableItem> detalleVentaTableItemList;
	private ProductoDAO productoDAO;
	private VentaDAO    ventaDAO;
	private DecimalFormat df;
	
	public PanelVentaControl(PanelVenta panelVenta) {
		this.panelVenta = panelVenta;
		DetalleVentaTableModel x = (DetalleVentaTableModel)this.panelVenta.getDetalleVentaJTable().getModel();
		x.addTableModelListener( this);
		panelVenta.getDetalleVentaJTable().addMouseListener(this);
		panelVenta.getDetalleVentaJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		System.err.println("->>table columns="+panelVenta.getDetalleVentaJTable().getColumnCount());
		
		final ImporteCellRender importeCellRender = new ImporteCellRender();
		
		importeCellRender.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panelVenta.getDetalleVentaJTable().getColumnModel().getColumn(5).setCellRenderer(importeCellRender);
		panelVenta.getDetalleVentaJTable().getColumnModel().getColumn(6).setCellRenderer(importeCellRender);
		
		//panelVenta.getDetalleVentaJTable().getColumn(5).setCellRenderer(new ImporteCellRender());
		//panelVenta.getDetalleVentaJTable().getColumn(6).setCellRenderer(new ImporteCellRender());
		
		detalleVentaTableItemList = (x).getDetalleVentaTableItemList();
		productoDAO = ProductoDAOFactory.getProductoDAO();
		ventaDAO    = VentaDAOFactory.getVentaDAO();
		
		this.panelVenta.getCodigoBuscar().addActionListener(this);
		this.panelVenta.getTerminar().addActionListener(this);
		this.panelVenta.getCancelar().addActionListener(this);
		
		df = new DecimalFormat("$ ###,###,###,##0.00");
	}
	
	public void estadoInicial(){
		if(detalleVentaTableItemList.size()>0){
			detalleVentaTableItemList.clear();
		}
		panelVenta.getDetalleVentaJTable().updateUI();
		renderTotal();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panelVenta.getCodigoBuscar()){
			codigoBuscar_ActionPerformed();
		} else if(e.getSource() == this.panelVenta.getTerminar()){
			terminar_ActionPerformed();
		}else if(e.getSource() == this.panelVenta.getCancelar()){
			cancelar_ActionPerformed();
		}
		
	}

	private void codigoBuscar_ActionPerformed() {
		String codigoBuscar = panelVenta.getCodigoBuscar().getText().trim();
		Producto productoEncontrado = productoDAO.getProducto(codigoBuscar);
		if(productoEncontrado != null) {
			DetalleVenta detalleVenta = new DetalleVenta(-1, -1, productoEncontrado.getCodigo(), 1, productoEncontrado.getPrecioVenta());
			
			DetalleVentaTableItem detalleVentaTableItemNuevo = null;
			
			for(DetalleVentaTableItem dvti: detalleVentaTableItemList){
				if(dvti.getCodigo().equals(codigoBuscar)){
					detalleVentaTableItemNuevo = dvti;
					break;
				}
			}
			
			if(detalleVentaTableItemNuevo == null){
				detalleVentaTableItemNuevo = new DetalleVentaTableItem(productoEncontrado, detalleVenta);
				detalleVentaTableItemList.add(detalleVentaTableItemNuevo);
			} else {
				detalleVentaTableItemNuevo.setCantidad(detalleVentaTableItemNuevo.getCantidad() + 1);
			}
			
			panelVenta.getCodigoBuscar().setText("");
			panelVenta.getDetalleVentaJTable().updateUI();
			renderTotal();
		}
	}
	
	private void terminar_ActionPerformed(){		
		try {
			Venta venta =  new Venta(0, new Date());
			List<DetalleVenta> detalleVentaList =  new ArrayList<DetalleVenta>();
			
			for(DetalleVentaTableItem dvil: detalleVentaTableItemList){
				detalleVentaList.add(new DetalleVenta(0, 0, dvil.getCodigo(), dvil.getCantidad(), dvil.getPrecioVenta()));
			}

			ventaDAO.persist(venta, detalleVentaList);
			
			JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(), "Se guardo Correctamente", "Venta", JOptionPane.INFORMATION_MESSAGE);
			
			estadoInicial();
			panelVenta.getCodigoBuscar().requestFocus();
			
		} catch (EntidadExistenteException ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(), ex.getMessage(), "Venta", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelar_ActionPerformed(){				
		int r = JOptionPane.showConfirmDialog(FramePrincipalControl.getInstance().getFramePrincipal(), "¿Cancelar la Venta Actual?", "Venta", JOptionPane.YES_NO_OPTION);
		if(r == JOptionPane.YES_OPTION){
			estadoInicial();
			panelVenta.getCodigoBuscar().requestFocus();
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		panelVenta.getDetalleVentaJTable().updateUI();
		renderTotal();
	}

	private void renderTotal() {
		double total = 0.0;
		for(DetalleVentaTableItem dvti: detalleVentaTableItemList){
			total += dvti.getImporete();
		}
		panelVenta.getTotal().setText(df.format(total));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==panelVenta.getDetalleVentaJTable()){
			//System.err.println("->>mouseClicked:"+e.getClickCount()+" "+panelVenta.getDetalleVentaJTable().getSelectedRow()+","+panelVenta.getDetalleVentaJTable().getSelectedColumn());
			if(e.getClickCount()==2 && panelVenta.getDetalleVentaJTable().getSelectedColumn()==0){
				editarCantidad(panelVenta.getDetalleVentaJTable().getSelectedRow());
			}
		}
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

	private void editarCantidad(int selectedRow) {
		int cantidad = detalleVentaTableItemList.get(selectedRow).getCantidad();
		Object result = JOptionPane.showInputDialog(panelVenta, "Cambiar la Cantidad:",cantidad);
		if(result != null) {
			Integer nuevaCantidad = new Integer(result.toString().trim());
			
			if(nuevaCantidad>0){
				detalleVentaTableItemList.get(selectedRow).setCantidad(nuevaCantidad);
				panelVenta.getDetalleVentaJTable().updateUI();
				renderTotal();
			}			
		}
		panelVenta.getCodigoBuscar().requestFocus();
		
	}

	void ventaeliminarProdMenu() {
		int selectedRow = panelVenta.getDetalleVentaJTable().getSelectedRow();
		if(selectedRow>=0){
			detalleVentaTableItemList.remove(selectedRow);
			panelVenta.getDetalleVentaJTable().updateUI();
			renderTotal();
			
			panelVenta.getDetalleVentaJTable().getSelectionModel().clearSelection();
			panelVenta.getCodigoBuscar().requestFocus();
		}
	}

	void ventaNueva() {
		panelVenta.getCodigoBuscar().requestFocus();
	}


	
}
