/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.EntidadExistenteException;
import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAO;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import com.xpressosystems.xpresscashdrawer.view.DialogEdicionProducto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author alfredo
 */
public class DialogEdicionProductoControl implements ActionListener{
	
	ProductoDAO prodcutoDAO;
	DialogEdicionProducto dialogEdicionProducto;
	Producto  productoEdicion;
	int optionClosed = 0;
	DecimalFormat df = new DecimalFormat("########0.00");
        
	private DialogEdicionProductoControl(DialogEdicionProducto panelEdicionProducto) {
		this.dialogEdicionProducto = panelEdicionProducto;
		optionClosed = JOptionPane.CANCEL_OPTION;
		
		dialogEdicionProducto.getAceptar().addActionListener(this);
		dialogEdicionProducto.getCancelar().addActionListener(this);
		
		prodcutoDAO = ProductoDAOFactory.getProductoDAO();
	}
	
	public void estadoInicial(){
		dialogEdicionProducto.getCampos()[0].setText(productoEdicion.getCodigo());
		dialogEdicionProducto.getCampos()[1].setText(productoEdicion.getNombre());
		dialogEdicionProducto.getCampos()[2].setText(productoEdicion.getLinea());
		dialogEdicionProducto.getCampos()[3].setText(productoEdicion.getMarca());
		dialogEdicionProducto.getCampos()[4].setText(df.format(productoEdicion.getCosto()));
		dialogEdicionProducto.getCampos()[5].setText(df.format(productoEdicion.getPrecioVenta()));
		dialogEdicionProducto.getCampos()[6].setText(String.valueOf(productoEdicion.getPiezasXCaja()));
		dialogEdicionProducto.getCampos()[7].setText(String.valueOf(productoEdicion.getExistencia()));	
		
		if(!esEdicionNuevoProducto()){
			dialogEdicionProducto.getCampos()[0].setEnabled(false);			
			dialogEdicionProducto.getCampos()[1].requestFocus();
		} else {
			dialogEdicionProducto.getCampos()[0].setEnabled(true);			
			dialogEdicionProducto.getCampos()[0].requestFocus();
		}
		
	}
	
	public static int crearProducto(Producto producto){
		int ret = JOptionPane.CANCEL_OPTION;
			
		DialogEdicionProducto panelEdicionProducto = new DialogEdicionProducto("Agregar nuevo Producto");
		
		DialogEdicionProductoControl control = new DialogEdicionProductoControl(panelEdicionProducto);
		control.productoEdicion = producto;
		
		control.estadoInicial();

		panelEdicionProducto.getSurtirLbl().setVisible(false);
		panelEdicionProducto.getSurtir().setVisible(false);
		
		panelEdicionProducto.setVisible(true);
		
		ret = control.optionClosed;
		
		return ret;
	}
	
	public static int crearProducto_precargado(Producto producto,String codigo){
		int ret = JOptionPane.CANCEL_OPTION;
			
		DialogEdicionProducto panelEdicionProducto = new DialogEdicionProducto("Agregar nuevo Producto");
		
		DialogEdicionProductoControl control = new DialogEdicionProductoControl(panelEdicionProducto);
		control.productoEdicion = producto;
		
		control.estadoInicial();

		panelEdicionProducto.getCampos()[0].setText(codigo);
		
		panelEdicionProducto.getSurtirLbl().setVisible(false);
		panelEdicionProducto.getSurtir().setVisible(false);
		
		panelEdicionProducto.setVisible(true);
		
		ret = control.optionClosed;
		
		return ret;
	}
	
	static int editaProducto(Producto producto) {
		int ret = JOptionPane.CANCEL_OPTION;
			
		DialogEdicionProducto panelEdicionProducto = new DialogEdicionProducto("Edición de Producto");
		
		DialogEdicionProductoControl control = new DialogEdicionProductoControl(panelEdicionProducto);
		control.productoEdicion = producto;
		
		control.estadoInicial();
		
		panelEdicionProducto.getSurtirLbl().setVisible(true);
		
		panelEdicionProducto.getSurtir().setVisible(true);
		panelEdicionProducto.getSurtir().setText("0");
		
		panelEdicionProducto.setVisible(true);
		
		ret = control.optionClosed;
		
		return ret;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == dialogEdicionProducto.getAceptar()) {
			aceptar_actionPerformed();
		} else if(e.getSource() == dialogEdicionProducto.getCancelar()) {
			cancelar_actionPerformed();
		}
	}

	private void aceptar_actionPerformed() {
		try {
			validacionCampos();
		}catch(ValidacionCamposException e){
			JOptionPane.showMessageDialog(dialogEdicionProducto, e.getMessage(), "Error en Producto", JOptionPane.ERROR_MESSAGE);
			final JTextField source = e.getSource();
			source.setSelectionStart(0);
			source.setSelectionEnd(source.getText().length());
			source.requestFocus();
			return;
		}
		
		if(esEdicionNuevoProducto()){
			guardarNuevoProducto();
		} else {
			guardarEdicionProducto();
		}
	}

	private void validacionCampos() throws ValidacionCamposException{
		
		validacionCamposLlenos();
		
		final String codigo = dialogEdicionProducto.getCampos()[0].getText();
		if(!codigo.matches("([0-9]){6,18}")){
			throw new ValidacionCamposException("CODIGO de Barras deb ser solo números (de 6 a 18, Use el Escanner)",dialogEdicionProducto.getCampos()[0]);
		}
		
		if(esEdicionNuevoProducto()){
			final Producto productoEncontrado = prodcutoDAO.getProducto(codigo);
			if(productoEncontrado != null) {
				dialogEdicionProducto.getCampos()[0].setText("");
				throw new ValidacionCamposException("Este CODIGO ya existe de otro Producto("+productoEncontrado.getNombre()+")",dialogEdicionProducto.getCampos()[0]);
			}
		}
		
		final String nombre = dialogEdicionProducto.getCampos()[1].getText();
		if(!(nombre.trim().length()>4 && nombre.trim().length()<=35)){
			throw new ValidacionCamposException("El Nombre debe ser correcto (5 a 35 Letras)",dialogEdicionProducto.getCampos()[1]);
		}
		
		final String linea = dialogEdicionProducto.getCampos()[2].getText();
		if(!(linea.trim().length()>4 && linea.trim().length()<=35)){
			throw new ValidacionCamposException("La Linea debe ser correcta (5 a 35 Letras)",dialogEdicionProducto.getCampos()[2]);
		}
		
		final String marca = dialogEdicionProducto.getCampos()[3].getText();
		if(!(marca.trim().length()>4 && marca.trim().length()<=35)){
			throw new ValidacionCamposException("La Marca debe ser correcta (5 a 35 Letras)",dialogEdicionProducto.getCampos()[3]);
		}
		
		final String costo = dialogEdicionProducto.getCampos()[4].getText();
		double costoParsed = -1.0;
		try {
			costoParsed = Double.parseDouble(costo);
			if(costoParsed <= 0.02){
				throw new ValidacionCamposException("El Costo debe ser lógicamente mayor a 20 Cent. ( $ 0.02 )",dialogEdicionProducto.getCampos()[4]);
			}
		}catch(NumberFormatException nfe){
			dialogEdicionProducto.getCampos()[4].setText("0.00");
			throw new ValidacionCamposException("EL Costo debe ser un Importe Monetario",dialogEdicionProducto.getCampos()[4]);
		}
		
		final String precio = dialogEdicionProducto.getCampos()[5].getText();
		try {
			double precioParsed = Double.parseDouble(precio);
			if(precioParsed <= costoParsed+0.02){
				throw new ValidacionCamposException("El Precio debe ser lógicamente mayor al Costo",dialogEdicionProducto.getCampos()[5]);
			}
		}catch(NumberFormatException nfe){
			dialogEdicionProducto.getCampos()[5].setText("0.00");
			throw new ValidacionCamposException("El Precio debe ser un Importe Monetario",dialogEdicionProducto.getCampos()[5]);
		}
		
		final String piezas = dialogEdicionProducto.getCampos()[6].getText();
		try {
			int piezasParsed = Integer.parseInt(piezas);
			if(piezasParsed <= 2){
				throw new ValidacionCamposException("Una xpresscashdrawer debe tener lógicamente mas de 2 piezas",dialogEdicionProducto.getCampos()[6]);
			}
		}catch(NumberFormatException nfe){
			dialogEdicionProducto.getCampos()[6].setText("0");
			throw new ValidacionCamposException("Las Piezas X Caja debe ser una cantidad entera",dialogEdicionProducto.getCampos()[6]);
		}
		
		final String existencia = dialogEdicionProducto.getCampos()[7].getText();
		try {
			int piezasParsed = Integer.parseInt(piezas);
			if(piezasParsed < 1){
				throw new ValidacionCamposException("La existencia debe ser lógicamente por lo menos 1 piezas",dialogEdicionProducto.getCampos()[7]);
			}
		}catch(NumberFormatException nfe){
			dialogEdicionProducto.getCampos()[7].setText("0");
			throw new ValidacionCamposException("La existencia debe ser una cantidad entera",dialogEdicionProducto.getCampos()[7]);
		}
		
		if(dialogEdicionProducto.getSurtir().isVisible()){
			final String surtir = dialogEdicionProducto.getSurtir().getText();
			try {
				int surtirParsed = Integer.parseInt(surtir);
				if(surtirParsed < 0){
					throw new ValidacionCamposException("La cantidad a Surtir debe ser lógicamente > 0",dialogEdicionProducto.getSurtir());
				}
			}catch(NumberFormatException nfe){
				dialogEdicionProducto.getSurtir().setText("0");
				throw new ValidacionCamposException("La cantidad a Surtir debe ser una cantidad entera",dialogEdicionProducto.getCampos()[7]);
			}
		}
		
	}

	private boolean validacionCamposLlenos() throws ValidacionCamposException {
		final JTextField[] campos = dialogEdicionProducto.getCampos();
		for (JTextField c : campos) {
			if (c.getText().trim().length() == 0) {
				throw new ValidacionCamposException("Debe llenar este campo",c);
			}
		}
		return true;
	}

	private void guardarNuevoProducto() {
		vaciarDatosCapturados();
		try {
			prodcutoDAO.persist(productoEdicion);
			optionClosed = JOptionPane.OK_OPTION;
			dialogEdicionProducto.dispose();
		} catch (EntidadExistenteException ex) {
			JOptionPane.showMessageDialog(dialogEdicionProducto, "El producto no se puede guardar", "Error en Producto", JOptionPane.ERROR_MESSAGE);			
		}
	}

	private void guardarEdicionProducto() {
		vaciarDatosCapturados();
		try {
			prodcutoDAO.edit(productoEdicion);
			optionClosed = JOptionPane.OK_OPTION;
			dialogEdicionProducto.dispose();
		} catch (EntidadInexistenteException ex) {
			JOptionPane.showMessageDialog(dialogEdicionProducto, "El producto no se puede guardar", "Error en Producto", JOptionPane.ERROR_MESSAGE);			
		}
	}

	private boolean esEdicionNuevoProducto() {
		return productoEdicion.getCodigo()==null ||productoEdicion.getCodigo().trim().length()==0;
	}

	private void vaciarDatosCapturados() throws NumberFormatException {
		productoEdicion.setCodigo(dialogEdicionProducto.getCampos()[0].getText());
		productoEdicion.setNombre(dialogEdicionProducto.getCampos()[1].getText());
		productoEdicion.setLinea(dialogEdicionProducto.getCampos()[2].getText());
		productoEdicion.setMarca(dialogEdicionProducto.getCampos()[3].getText());
		productoEdicion.setCosto(Double.parseDouble(dialogEdicionProducto.getCampos()[4].getText()));
		productoEdicion.setPrecioVenta(Double.parseDouble(dialogEdicionProducto.getCampos()[5].getText()));
		productoEdicion.setPiezasXCaja(Integer.parseInt(dialogEdicionProducto.getCampos()[6].getText()));
		productoEdicion.setExistencia(Integer.parseInt(dialogEdicionProducto.getCampos()[7].getText()));
		if(! esEdicionNuevoProducto()){
			if(dialogEdicionProducto.getSurtir().getText().trim().length()>0){
				final String surtir = dialogEdicionProducto.getSurtir().getText();
				try {
					int surtirParsed = Integer.parseInt(surtir);
					if(surtirParsed > 0){
						productoEdicion.setExistencia(productoEdicion.getExistencia() + surtirParsed);
					}
				}catch(NumberFormatException nfe){
					dialogEdicionProducto.getSurtir().setText("0");					
				}
			}
		}
	}

	private void cancelar_actionPerformed() {
		optionClosed = JOptionPane.CANCEL_OPTION;
		dialogEdicionProducto.dispose();
	}
	
	
}
