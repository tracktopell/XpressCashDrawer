/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author alfredo
 */
public class VentaTableModel implements TableModel{
	
	private static String[] columnNames = new String [] {
			"No", "Fecha", "Importe"
	};
	private static Class[] columnClasses = new Class [] {
			Integer.class,Date.class,BigDecimal.class
	};
	
	private List<Venta> ventaList;
	private Hashtable<Integer,Double> ventaImporteList;
	
	private List<TableModelListener> tableModelListenerList;
	
	public VentaTableModel(){
		this.ventaList = new ArrayList<Venta>();
		tableModelListenerList = new ArrayList<TableModelListener> ();
		//refrescar();
	}

	@Override
	public int getRowCount() {
		return ventaList.size();
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
		final Venta dvti = ventaList.get(rowIndex);
		if(columnIndex == 0)
			return dvti.getId();
		else if(columnIndex == 1)
			return dvti.getFecha();
		else if(columnIndex == 2)
			//return new Double(-1.0);
			return new BigDecimal(ventaImporteList.get(dvti.getId()));
		else
			throw new IndexOutOfBoundsException("for column:"+columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		/*
		final Venta dvti = detalleVentaTableItemList.get(rowIndex);
		
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
	public List<Venta> getVentaList() {
		return ventaList;
	}

	private void callListeners() {
		for(TableModelListener tml: tableModelListenerList){
			tml.tableChanged(new TableModelEvent(this));
		}
	}
	/*
	public void refrescar() {
		VentaDAO ventaDAO = VentaDAOFactory.getVentaDAO();
		ventaList = ventaDAO.getAll(); 		
		ventaImporteList       = new Hashtable<Integer,Double>();
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
	}
	*/
}
