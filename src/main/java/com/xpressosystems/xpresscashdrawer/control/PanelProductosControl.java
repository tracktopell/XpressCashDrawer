/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.ProductoDAO;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.CantidadCellRender;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import com.xpressosystems.xpresscashdrawer.model.DetalleProductoTableModel;
import com.xpressosystems.xpresscashdrawer.model.ImporteCellRender;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import com.xpressosystems.xpresscashdrawer.view.PanelProductos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author alfredo
 */
public class PanelProductosControl implements ActionListener,TableModelListener,MouseListener{

	private PanelProductos panelProductos;
	private List<Producto> detalleProductoList;
	private ProductoDAO productoDAO;
	private DecimalFormat df;
	
	public PanelProductosControl(PanelProductos panelProductos) {
		this.panelProductos = panelProductos;
		DetalleProductoTableModel x = (DetalleProductoTableModel)this.panelProductos.getDetalleProductoJTable().getModel();
		x.addTableModelListener( this);
		panelProductos.getDetalleProductoJTable().addMouseListener(this);
		panelProductos.getDetalleProductoJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		System.err.println("->>table columns="+panelProductos.getDetalleProductoJTable().getColumnCount());
		
		final ImporteCellRender importeCellRender = new ImporteCellRender();
		final CantidadCellRender cantidadCellRender = new CantidadCellRender();
		
		importeCellRender.setHorizontalAlignment(SwingConstants.RIGHT);
		cantidadCellRender.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panelProductos.getDetalleProductoJTable().getColumnModel().getColumn(4).setCellRenderer(importeCellRender);
		panelProductos.getDetalleProductoJTable().getColumnModel().getColumn(5).setCellRenderer(importeCellRender);
		panelProductos.getDetalleProductoJTable().getColumnModel().getColumn(6).setCellRenderer(cantidadCellRender);
		panelProductos.getDetalleProductoJTable().getColumnModel().getColumn(7).setCellRenderer(cantidadCellRender);		
		
		detalleProductoList = (x).getDetalleProductoList();
		productoDAO = ProductoDAOFactory.getProductoDAO();
		
		detalleProductoList.addAll(productoDAO.getAll());
		
		this.panelProductos.getNuevo().addActionListener(this);
		
		this.panelProductos.getEditar().addActionListener(this);
		
		this.panelProductos.getEliminar().addActionListener(this);

		this.panelProductos.getCodigoBuscar().addActionListener(this);

		
		df = new DecimalFormat("$ ###,###,###,##0.00");
	}
	
	public void estadoInicial(){
		if(detalleProductoList.size()>0){
			detalleProductoList.clear();
		}
		detalleProductoList.addAll(productoDAO.getAll());		
		panelProductos.getDetalleProductoJTable().updateUI();		
		panelProductos.getDetalleProductoJTable().getSelectionModel().clearSelection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panelProductos.getNuevo()){
			nuevo_actionPerformed();
		} else if(e.getSource() == panelProductos.getEditar()){
			editar_actionPerformed();
		}
		else if(e.getSource() == this.panelProductos.getCodigoBuscar()){
			codigoBuscar_actionPerformed();
		}
		
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		panelProductos.getDetalleProductoJTable().updateUI();		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==panelProductos.getDetalleProductoJTable()){
			if(e.getClickCount()==2 ){
				editar_actionPerformed();
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

	void ventaeliminarProdMenu() {
		int selectedRow = panelProductos.getDetalleProductoJTable().getSelectedRow();
		if(selectedRow>=0){
			detalleProductoList.remove(selectedRow);
			panelProductos.getDetalleProductoJTable().updateUI();
			
			panelProductos.getDetalleProductoJTable().getSelectionModel().clearSelection();
		}
	}

	private void nuevo_actionPerformed() {
		Producto producto = new Producto();
		int ret = DialogEdicionProductoControl.crearProducto(producto);
		if(ret == JOptionPane.OK_OPTION){
			estadoInicial();
			panelProductos.getCodigoBuscar().setText("");
			panelProductos.getCodigoBuscar().requestFocus();
		}
		
	}
	
	private void nuevo_actionPerformed_precargado() {
		Producto producto = new Producto();
		
		int ret = DialogEdicionProductoControl.crearProducto_precargado(producto,panelProductos.getCodigoBuscar().getText());
		panelProductos.getCodigoBuscar().setText("");
		if(ret == JOptionPane.OK_OPTION){
			estadoInicial();
			panelProductos.getCodigoBuscar().setText("");
			panelProductos.getCodigoBuscar().requestFocus();
		}
		
	}

	private void editar_actionPerformed() {
		int selectedRow = panelProductos.getDetalleProductoJTable().getSelectedRow();
		if(selectedRow>=0){
			Producto producto = detalleProductoList.get(selectedRow);
			int ret = DialogEdicionProductoControl.editaProducto(producto);
			if(ret == JOptionPane.OK_OPTION){
				estadoInicial();
				panelProductos.getCodigoBuscar().requestFocus();
			}	
		}
	}
	
	private void codigoBuscar_actionPerformed() {
		int selectedRow = -1;
		int i=0;
		for(Producto p: detalleProductoList){
			//System.err.println("\t-->> codigoBuscar_actionPerformed(): "+p.getCodigo()+" == "+panelProductos.getCodigoBuscar().getText()+" ?");
			if(p.getCodigo().equals(panelProductos.getCodigoBuscar().getText())){
				selectedRow = i;
				break;
			}
			i++;
		}
		//System.err.println("-->> codigoBuscar_actionPerformed(): selectedRow="+selectedRow+", panelProductos.getCodigoBuscar()="+panelProductos.getCodigoBuscar().getText());
		
		if(selectedRow>=0){
			Producto producto = detalleProductoList.get(selectedRow);
			int ret = DialogEdicionProductoControl.editaProducto(producto);
			if(ret == JOptionPane.OK_OPTION){
				estadoInicial();
			}
			panelProductos.getCodigoBuscar().setText("");
			panelProductos.getCodigoBuscar().requestFocus();
		} else {
			int r = JOptionPane.showConfirmDialog(FramePrincipalControl.getInstance().getFramePrincipal(), 
					"¿Desea agregar este codigo como nuevo Producto ?","Productos",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if( r == JOptionPane.YES_OPTION) {
				new Thread(){

					@Override
					public void run() {
						nuevo_actionPerformed_precargado();
					}				
				}.start();
			} else{
				panelProductos.getCodigoBuscar().setText("");
				panelProductos.getCodigoBuscar().requestFocus();
			}
		}
	}
	
}
