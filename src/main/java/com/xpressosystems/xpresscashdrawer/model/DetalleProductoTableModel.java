/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author alfredo
 */
public class DetalleProductoTableModel implements TableModel{
	
	private static String[] columnNames = new String [] {
			"Codigo", "Producto", "Linea", "Marca","Costo","Precio Venta", "Existencia","Piezas X Caja"
	};
	private static Class[] columnClasses = new Class [] {
			String.class,String.class,String.class,String.class,Double.class,Double.class,Integer.class,Integer.class
	};
	
	private List<Producto> detalleProductoList;
	private List<TableModelListener> tableModelListenerList;
	
	public DetalleProductoTableModel(){
		tableModelListenerList	= new ArrayList<TableModelListener> ();
		detalleProductoList		= new ArrayList<Producto> (); 
	}

	@Override
	public int getRowCount() {
		return detalleProductoList.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		//return columnIndex==0;
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final Producto dvti = detalleProductoList.get(rowIndex);
		if(columnIndex == 0)
			return dvti.getCodigo();
		else if(columnIndex == 1)
			return dvti.getNombre();
		else if(columnIndex == 2)
			return dvti.getLinea();
		else if(columnIndex == 3)
			return dvti.getMarca();
		else if(columnIndex == 4)
			return dvti.getCosto();
		else if(columnIndex == 5)
			return dvti.getPrecioVenta();
		else if(columnIndex == 6)
			return dvti.getExistencia();
		else if(columnIndex == 7)
			return dvti.getPiezasXCaja();
		else 
			throw new IndexOutOfBoundsException("for column:"+columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		/*
		final  dvti = detalleVentaTableItemList.get(rowIndex);
		
		if(columnIndex == 0) {
			dvti.setCantidad((Integer)aValue);
			callListeners();
		} else {
			throw new IndexOutOfBoundsException("for column:"+columnIndex);
		}
		*/
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModelListenerList.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		tableModelListenerList.remove(l);
	}

	/**
	 * @return the detalleVentaTableItemList
	 */
	public List<Producto> getDetalleProductoList() {
		return detalleProductoList;
	}

	private void callListeners() {
		for(TableModelListener tml: tableModelListenerList){
			tml.tableChanged(new TableModelEvent(this));
		}
	}
}
