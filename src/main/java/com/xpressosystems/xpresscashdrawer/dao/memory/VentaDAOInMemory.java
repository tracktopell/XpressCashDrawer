/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao.memory;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAO;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 *
 * @author alfredo
 */
public class VentaDAOInMemory implements VentaDAO{
	
	Hashtable<Integer,Venta> ventaTable;
	Hashtable<Integer,DetalleVenta> detalleVentaTable;
	Random random;		
	
	public VentaDAOInMemory(){
		ventaTable = new Hashtable<Integer,Venta>() ;
		detalleVentaTable = new Hashtable<Integer,DetalleVenta>();
		random =  new Random();		
	}
	
	@Override
	public List<Venta> getAll() {
		List<Venta> r = new ArrayList<Venta>();
		r.addAll(ventaTable.values());
		return r;
	}
	
	@Override	
	public List<DetalleVenta> getDetalleVenta(Integer ventaId)throws EntidadInexistenteException{
		List<DetalleVenta> result= new ArrayList<DetalleVenta> ();
		final Collection<DetalleVenta> dvs = detalleVentaTable.values();		
		for(DetalleVenta dv: dvs){
			if(dv.getVentaId() == ventaId){
				result.add(dv);
			}
		}
		
		return result;
	}


	@Override
	public Venta getVenta(Integer codigo) {
		return ventaTable.get(codigo);
	}

	@Override
	public Venta edit(Venta venta,List<DetalleVenta> detalleVentaList) throws EntidadInexistenteException {
		if(ventaTable.get(venta.getId()) == null){
			throw new EntidadInexistenteException();
		}		
		ventaTable.put(venta.getId(), venta);
		Collection<DetalleVenta> dvs = detalleVentaTable.values();
		int maxDV = 0;
		for(DetalleVenta dv: dvs){
			if ( maxDV < dv.getId()){
				maxDV = dv.getId();
			}
		}
		int i=1;
		
		for(DetalleVenta dv: detalleVentaList){
			
			detalleVentaTable.remove(dv.getId());
			
			dv.setId(maxDV+i);
			i++;
			dv.setVentaId(venta.getId());
			detalleVentaTable.put(dv.getId(),dv);
		}
		return venta;
	}

	@Override
	public Venta persist(Venta venta,List<DetalleVenta> detalleVentaList) throws EntidadExistenteException {
		
		if(ventaTable.get(venta.getId()) != null){
			throw new EntidadExistenteException();
		}
		
		int max = 0;
		final Collection<Venta> values = ventaTable.values();
		for(Venta v: values){
			if ( max < v.getId()){
				max = v.getId();
			}
		}
		venta.setId(max+1);
		ventaTable.put(venta.getId(), venta);
		
		Collection<DetalleVenta> dvs = detalleVentaTable.values();
		int maxDV = 0;
		for(DetalleVenta dv: dvs){
			if ( maxDV < dv.getId()){
				maxDV = dv.getId();
			}
		}
		int i=1;
		
		for(DetalleVenta dv: detalleVentaList){
			dv.setId(maxDV+i);
			i++;
			dv.setVentaId(venta.getId());
			detalleVentaTable.put(dv.getId(),dv);
		}
		
		return venta;
	}

	@Override
	public Venta delete(Venta venta) throws EntidadInexistenteException {
		if(ventaTable.get(venta.getId()) == null){
			throw new EntidadInexistenteException();
		}

		return ventaTable.remove(venta.getId());
	}	
}
