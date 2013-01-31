/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao.memory;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAO;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 *
 * @author alfredo
 */
public class ProductoDAOInMemory implements ProductoDAO{
	
	Hashtable<String,Producto> productoTable;
	DecimalFormat df1 = new DecimalFormat("000000");
	DecimalFormat df2 = new DecimalFormat("000000");
	public ProductoDAOInMemory(){
		productoTable = new Hashtable<String,Producto>() ;
		Random random =  new Random();
		for(int i=0;i<10;i++){
			String codigo = df1.format(random.nextInt(999999))+df2.format(random.nextInt(999999));
			double costo  = random.nextDouble()*100;
			double precio = costo * 1.2;
			
			productoTable.put(codigo, 
					new Producto(codigo, "Producto "+i, "Linea Producto "+i, "Marca Producto "+i, costo, precio, random.nextInt(200), 1+random.nextInt(20)));
			System.err.println("-->>ProductoDAOInMemory: add "+codigo);
		}
	
	}
	
	@Override
	public List<Producto> getAll() {
		List<Producto> r = new ArrayList<Producto>();
		r.addAll(productoTable.values());
		return r;
	}

	@Override
	public Producto getProducto(String codigo) {
		return productoTable.get(codigo);
	}

	@Override
	public Producto edit(Producto producto) throws EntidadInexistenteException {
		if(productoTable.get(producto.getCodigo()) == null){
			throw new EntidadInexistenteException();
		}		
		productoTable.put(producto.getCodigo(), producto);
		return producto;
	}

	@Override
	public Producto persist(Producto producto) throws EntidadExistenteException {
		if(productoTable.get(producto.getCodigo()) != null){
			throw new EntidadExistenteException();
		}
		productoTable.put(producto.getCodigo(), producto);
		return producto;
	}

	@Override
	public Producto delete(Producto producto) throws EntidadInexistenteException {
		if(productoTable.get(producto.getCodigo()) == null){
			throw new EntidadInexistenteException();
		}

		return productoTable.remove(producto.getCodigo());
	}	
}
