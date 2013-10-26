/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.dao.jdbc;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.PreferenciaDAO;
import com.xpressosystems.xpresscashdrawer.model.Preferencia;
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
public final class PreferenciaDAOJDBC implements PreferenciaDAO{
	private Connection conn;
	private Logger logger;

	public PreferenciaDAOJDBC(Connection connection) {
		logger = Logger.getLogger(PreferenciaDAOJDBC.class);
		logger.info("-->> init with connection:"+connection);
		conn = connection;
	}
	
	@Override
	public List<Preferencia> getAll() {
		List<Preferencia> r = new ArrayList<Preferencia>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT ID,VALOR FROM PREFERENCIA");
			
			rs = ps.executeQuery();
			while(rs.next()) {
				Preferencia x = new Preferencia();
				x.setId	    (rs.getString("ID"));
				x.setValor	(rs.getString("VALOR"));
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
	public Preferencia getPreferencia(String id) {
		Preferencia x = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT ID,VALOR FROM PREFERENCIA WHERE ID=?");
			ps.setString(1, id);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				x = new Preferencia();
				x.setId	    (rs.getString("ID"));
				x.setValor	(rs.getString("VALOR"));
			}
		}catch(SQLException ex) {
			logger.error("in getPreferencia", ex);			
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
	public Preferencia edit(Preferencia preferencia) throws EntidadInexistenteException {
		Preferencia x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("UPDATE PREFERENCIA SET VALOR=? WHERE ID=?");
			ps.setString(1, preferencia.getValor());
			ps.setString(2, preferencia.getId());
			
			ra = ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadInexistenteException();
			} else{
				x = preferencia;
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
	public Preferencia persist(Preferencia preferencia) throws EntidadExistenteException {
		Preferencia x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("INSERT INTO PREFERENCIA (ID,VALOR) VALUES(?,?)");
			
			ps.setString(1, preferencia.getId());			
			ps.setString(2, preferencia.getValor());
			
			ra = ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadExistenteException();
			} else{
				x = preferencia;
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
	public Preferencia delete(Preferencia preferencia) throws EntidadInexistenteException {
		Preferencia x = null;
		PreparedStatement ps = null;
		int ra= -1;
		try {
			ps = conn.prepareStatement("DELETR FROM PREFERENCIA WHERE ID=?");
			ps.setString(1, preferencia.getId());
			
			ra = ps.executeUpdate();
			if(ra != 1) {
				x = null;
				throw new EntidadInexistenteException();
			} else{
				x = preferencia;
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
