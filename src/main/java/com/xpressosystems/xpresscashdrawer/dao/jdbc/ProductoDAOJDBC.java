/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao.jdbc;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAO;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author alfredo
 */
public final class ProductoDAOJDBC implements ProductoDAO{
	private Connection conn;
	private Logger logger;

	public ProductoDAOJDBC(Connection connection) {
		logger = Logger.getLogger(ProductoDAOJDBC.class);
		logger.info("-->> init with connection:"+connection);
		conn = connection;
	}
	
	@Override
	public List<Producto> getAll() {
		List<Producto> r = new ArrayList<Producto>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT CODIGO,NOMBRE,LINEA,MARCA,COSTO,PRECIO_VENTA,EXISTENCIA,PIEZAS_POR_CAJA FROM PRODUCTO");
			
			rs = ps.executeQuery();
			while(rs.next()) {
				Producto x = new Producto();
				x.setCodigo	(rs.getString("CODIGO"));
				x.setNombre	(rs.getString("NOMBRE"));
				x.setLinea	(rs.getString("LINEA"));
				x.setMarca	(rs.getString("MARCA"));
				
				x.setCosto		(rs.getDouble("COSTO"));
				x.setPrecioVenta(rs.getDouble("PRECIO_VENTA"));
				
				x.setExistencia (rs.getInt("EXISTENCIA"));
				x.setExistencia (rs.getInt("PIEZAS_POR_CAJA"));
				
				r.add(x);
			}
		}catch(SQLException ex) {
			logger.error("in getAll:", ex);
			r = null;
		} finally {
			if(rs != null) {
				try{
					rs.close();
					ps.close();
				}catch(SQLException ex) {
				}
			}
		}
		return r;
	}

	@Override
	public Producto getProducto(String codigo) {
		Producto x = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT CODIGO,NOMBRE,LINEA,MARCA,COSTO,PRECIO_VENTA,EXISTENCIA,PIEZAS_POR_CAJA FROM PRODUCTO "+
					"WHERE CODIGO=?");
			ps.setString(1, codigo);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				x = new Producto();
				
				x.setCodigo	(rs.getString("CODIGO"));
				x.setNombre	(rs.getString("NOMBRE"));
				x.setLinea	(rs.getString("LINEA"));
				x.setMarca	(rs.getString("MARCA"));				
				x.setCosto		(rs.getDouble("COSTO"));
				x.setPrecioVenta(rs.getDouble("PRECIO_VENTA"));				
				x.setExistencia (rs.getInt("EXISTENCIA"));
				x.setExistencia (rs.getInt("PIEZAS_POR_CAJA"));								
			}
		}catch(SQLException ex) {
			logger.error("in getProducto", ex);			
		} finally {
			if(rs != null) {
				try{
					rs.close();
					ps.close();
				}catch(SQLException ex) {
				}
			}
		}
		return x;
	}

	@Override
	public Producto edit(Producto producto) throws EntidadInexistenteException {
		Producto x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("UPDATE PRODUCTO SET NOMBRE=?,LINEA=?,MARCA=?,COSTO=?,PRECIO_VENTA=?,EXISTENCIA=?,PIEZAS_POR_CAJA=? "+
					"WHERE CODIGO=?");
			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getLinea());
			ps.setString(3, producto.getMarca());
			ps.setDouble(4, producto.getCosto());
			ps.setDouble(5, producto.getPrecioVenta());
			ps.setInt   (6, producto.getExistencia());
			ps.setInt   (7, producto.getPiezasXCaja());
			
			ps.setString(8, producto.getCodigo());
			
			ra = ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadInexistenteException();
			} else{
				x = producto;
			}
			
		}catch(SQLException ex) {
			logger.error("in edit:", ex);
			x = null;
		} finally {
			if(ps != null) {
				try{
					ps.close();
				}catch(SQLException ex) {
				}
			}
		}
		return x;	}

	@Override
	public Producto persist(Producto producto) throws EntidadExistenteException {
		Producto x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("INSERT INTO PRODUCTO (CODIGO,NOMBRE,LINEA,MARCA,COSTO,PRECIO_VENTA,EXISTENCIA,PIEZAS_POR_CAJA) "+
					" VALUES(?,?,?,?,?,?,?,?)");
			
			ps.setString(1, producto.getCodigo());			
			ps.setString(2, producto.getNombre());
			ps.setString(3, producto.getLinea());
			ps.setString(4, producto.getMarca());
			ps.setDouble(5, producto.getCosto());
			ps.setDouble(6, producto.getPrecioVenta());
			ps.setInt   (7, producto.getExistencia());
			ps.setInt   (8, producto.getPiezasXCaja());
			
			ra = ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadExistenteException();
			} else{
				x = producto;
			}
			
		}catch(SQLException ex) {
			logger.error("in insert:", ex);
			x = null;
		} finally {
			if(ps != null) {
				try{
					ps.close();
				}catch(SQLException ex) {
				}
			}
		}
		return x;
	}

	@Override
	public Producto delete(Producto producto) throws EntidadInexistenteException {
		Producto x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("DELETR FROM PRODUCTO WHERE CODIGO=?");
			ps.setString(1, producto.getCodigo());
			
			ra = ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadInexistenteException();
			} else{
				x = producto;
			}
			
		}catch(SQLException ex) {
			logger.error("in delete:", ex);
			x = null;
		} finally {
			if(ps != null) {
				try{
					ps.close();
				}catch(SQLException ex) {
				}
			}
		}
		return x;
	}	

	/**
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * @param conn the conn to set
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
