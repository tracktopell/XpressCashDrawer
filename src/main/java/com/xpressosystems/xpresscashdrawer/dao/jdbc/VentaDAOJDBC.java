/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao.jdbc;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.VentaDAO;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author alfredo
 */
public final class VentaDAOJDBC implements VentaDAO{
	private Connection conn;
	private Logger logger;

	public VentaDAOJDBC(Connection connection) {
		logger = Logger.getLogger(VentaDAOJDBC.class);
		logger.info("-->> init with connection:"+connection);
		conn = connection;
	}
	
	public List<DetalleVenta> getDetalleVenta(Integer ventaId)throws EntidadInexistenteException{
		List<DetalleVenta> r = new ArrayList<DetalleVenta> ();				
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT ID,VENTA_ID,PRODUCTO_CODIGO,CANTIDAD,PRECIO_VENTA FROM VENTA_DETALLE");
			
			rs = ps.executeQuery();
			while(rs.next()) {
				DetalleVenta x = new DetalleVenta();
				
				x.setId(rs.getInt("ID"));
				x.setVentaId(rs.getInt("VENTA_ID"));
				
				x.setProductoCodigo	(rs.getString	("PRODUCTO_CODIGO"));
				x.setCantidad		(rs.getInt		("CANTIDAD"));
				x.setPrecioVenta	(rs.getDouble	("PRECIO_VENTA"));
				
				r.add(x);
			}
		}catch(SQLException ex) {
			logger.error("in getDetalleVenta:", ex);
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
	public List<Venta> getAll() {
		List<Venta> r = new ArrayList<Venta>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT ID,FECHA FROM VENTA");
			
			rs = ps.executeQuery();
			while(rs.next()) {
				Venta x = new Venta();
				x.setId	    (rs.getInt("ID"));
				x.setFecha	(rs.getTimestamp("FECHA"));
				
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
	public Venta getVenta(Integer id) {
		Venta x = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT ID,FECHA FROM VENTA WHERE ID=?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				x = new Venta();
				
				x.setId	    (rs.getInt("ID"));
				x.setFecha	(rs.getTimestamp("FECHA"));								
			}

		}catch(SQLException ex) {
			logger.error("in getVenta", ex);			
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
	public Venta edit(Venta venta,List<DetalleVenta> detalleVentaList) throws EntidadInexistenteException {
		Venta x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("UPDATE VENTA SET FECHA=? "+
					" WHERE ID=?");
			
			ps.setTimestamp	(1, new Timestamp(venta.getFecha().getTime()));
			ps.setInt		(2, venta.getId());
			
			ra = ps.executeUpdate();
			
			ps.close();
			
			if(ra != 1) {
				x = null;
				throw new EntidadInexistenteException();
			} else{
				ps = conn.prepareStatement("DELETE FROM VENTA_DETALLE WHERE VENTA_ID=?");
				
				ps.setInt(1, venta.getId());
				
				ra+=ps.executeUpdate();
				
				ps.close();
				
				ps = conn.prepareStatement("INSERT INTO VENTA_DETALLE(VENTA_ID,PRODUCTO_CODIGO,CANTIDAD,PRECIO_VENTA) "+
					"VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				
				for(DetalleVenta dv:detalleVentaList){
					ps.clearParameters();
					ps.clearWarnings();
					
					dv.setVentaId(venta.getId());
					
					ps.setInt   (1, dv.getVentaId());
					ps.setString(2, dv.getProductoCodigo());
					ps.setInt   (3, dv.getCantidad());
					ps.setDouble(4, dv.getPrecioVenta());
					
					ra += ps.executeUpdate();
				}
				x = venta;
			}
			logger.info("ra="+ra);
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
	public Venta persist(Venta venta,List<DetalleVenta> detalleVentaList) throws EntidadExistenteException {
		Venta x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("INSERT INTO VENTA (FECHA) "+
					" VALUES(?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setTimestamp	(1, new Timestamp(venta.getFecha().getTime()));
			
			ra = ps.executeUpdate();
			final ResultSet generatedKeysRS = ps.getGeneratedKeys();
			if(generatedKeysRS.next()){
				venta.setId(generatedKeysRS.getInt(1));
			}else{
				venta.setId(-1);
			}
			
			ps.close();
			
			if(venta.getId() != 1) {
				x = null;
				throw new EntidadExistenteException();
			} else{
				ps = conn.prepareStatement("INSERT INTO VENTA_DETALLE(VENTA_ID,PRODUCTO_CODIGO,CANTIDAD,PRECIO_VENTA) "+
					"VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				
				for(DetalleVenta dv:detalleVentaList){
					ps.clearParameters();
					ps.clearWarnings();
					
					dv.setVentaId(venta.getId());
					
					ps.setInt   (1, dv.getVentaId());
					ps.setString(2, dv.getProductoCodigo());
					ps.setInt   (3, dv.getCantidad());
					ps.setDouble(4, dv.getPrecioVenta());
					
					ra += ps.executeUpdate();
				}
				x = venta;
			}
			logger.info("ra="+ra);
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
	public Venta delete(Venta venta) throws EntidadInexistenteException {
		Venta x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("DELETE FROM VENTA_DETALLE WHERE VENTA_ID=?");
			ps.setInt(1, venta.getId());
			
			ra = ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETR FROM VENTA WHERE ID=?");
			ps.setInt(1, venta.getId());
			
			ra += ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadInexistenteException();
			} else{
				x = venta;
			}
			logger.info("ra="+ra);
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
