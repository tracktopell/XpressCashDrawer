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
import com.xpressosystems.xpresscashdrawer.ticket.bluetooth.TicketBlueToothPrinter;
import com.xpressosystems.xpresscashdrawer.ticket.TicketPrinteService;
import com.xpressosystems.xpresscashdrawer.view.PanelVenta;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author alfredo
 */
public class PanelVentaControl implements ActionListener, TableModelListener, MouseListener {

	private PanelVenta panelVenta;
	private ArrayList<DetalleVentaTableItem> detalleVentaTableItemList;
	private ProductoDAO productoDAO;
	private VentaDAO ventaDAO;
	private DecimalFormat df;
	private TicketPrinteService ticketPrinteService;

	public PanelVentaControl(PanelVenta panelVenta) {
		this.panelVenta = panelVenta;
		DetalleVentaTableModel x = (DetalleVentaTableModel) this.panelVenta.getDetalleVentaJTable().getModel();
		x.addTableModelListener(this);
		System.err.println("->>table columns=" + panelVenta.getDetalleVentaJTable().getColumnCount());

		final ImporteCellRender importeCellRender = new ImporteCellRender();

		importeCellRender.setHorizontalAlignment(SwingConstants.RIGHT);
		panelVenta.getDetalleVentaJTable().addMouseListener(this);
		panelVenta.getDetalleVentaJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelVenta.getDetalleVentaJTable().getColumnModel().getColumn(5).setCellRenderer(importeCellRender);
		panelVenta.getDetalleVentaJTable().getColumnModel().getColumn(6).setCellRenderer(importeCellRender);
		panelVenta.getDetalleVentaJTable().addComponentListener(new JTableColumnAutoResizeHelper(
				new int[]{5,18,27,15,15,10,10}));
		
		panelVenta.getDetalleVentaJTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean valueIsAdjusting = e.getValueIsAdjusting();
				//System.out.println("=>TableSelection:"+fi+"->"+li+", adj?"+valueIsAdjusting);
				if (!valueIsAdjusting) {
					updateSelectedRow();
				}
			}
		});

		detalleVentaTableItemList = (x).getDetalleVentaTableItemList();
		ventaDAO = VentaDAOFactory.getVentaDAO();

		this.panelVenta.getCodigoBuscar().addActionListener(this);
		this.panelVenta.getTerminar().addActionListener(this);
		this.panelVenta.getCancelar().addActionListener(this);

		df = new DecimalFormat("$ ###,###,##0.00");
		ticketPrinteService = TicketBlueToothPrinter.getInstance();
		ticketPrinteService.setApplicationLogic(ApplicationLogic.getInstance());
	}

	public void estadoInicial() {
		if (productoDAO == null) {
			productoDAO = ProductoDAOFactory.getProductoDAO();
		}
		if (detalleVentaTableItemList.size() > 0) {
			detalleVentaTableItemList.clear();
		}
		panelVenta.getDetalleVentaJTable().updateUI();
		renderTotal();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == panelVenta.getCodigoBuscar()) {
			codigoBuscar_ActionPerformed();
		} else if (e.getSource() == this.panelVenta.getTerminar()) {
			terminar_ActionPerformed();
		} else if (e.getSource() == this.panelVenta.getCancelar()) {
			cancelar_ActionPerformed();
		}

	}

	private void codigoBuscar_ActionPerformed() {
		String codigoBuscar = panelVenta.getCodigoBuscar().getText().trim();
		System.err.println("=>codigoBuscar_ActionPerformed:codigoBuscar=" + codigoBuscar);
		Producto productoEncontrado = productoDAO.getProducto(codigoBuscar);
		System.err.println("=>codigoBuscar_ActionPerformed:productoEncontrado=" + productoEncontrado);

		if (productoEncontrado != null) {
			DetalleVenta detalleVenta = new DetalleVenta(-1, -1, productoEncontrado.getCodigo(), 1, productoEncontrado.getPrecioVenta());

			DetalleVentaTableItem detalleVentaTableItemNuevo = null;
			int idx = 0;
			for (DetalleVentaTableItem dvti : detalleVentaTableItemList) {
				if (dvti.getCodigo().equals(codigoBuscar)) {
					detalleVentaTableItemNuevo = dvti;
					break;
				}
				idx++;
			}

			if (detalleVentaTableItemNuevo == null) {
				detalleVentaTableItemNuevo = new DetalleVentaTableItem(productoEncontrado, detalleVenta);
				idx = detalleVentaTableItemList.size();
				detalleVentaTableItemList.add(detalleVentaTableItemNuevo);
			} else {
				detalleVentaTableItemNuevo.setCantidad(detalleVentaTableItemNuevo.getCantidad() + 1);
			}

			panelVenta.getCodigoBuscar().setText("");
			panelVenta.getDetalleVentaJTable().getSelectionModel().setSelectionInterval(idx, idx);
			panelVenta.getDetalleVentaJTable().updateUI();
			renderTotal();
		} else {
			new Thread() {
				@Override
				public void run() {
					Color pc = panelVenta.getCodigoBuscar().getBackground();
					panelVenta.getCodigoBuscar().setBackground(Color.red);
					try {
						for (int i = 0; i < 3; i++) {
							panelVenta.getCodigoBuscar().setBackground(Color.red);
							Thread.sleep(500);
							panelVenta.getCodigoBuscar().setBackground(pc);
							Thread.sleep(100);
						}
					} catch (InterruptedException ex) {
					} finally {
						panelVenta.getCodigoBuscar().setBackground(pc);
						panelVenta.getCodigoBuscar().setText("");
					}
				}
			}.start();
		}
	}

	private void terminar_ActionPerformed() {

		if (detalleVentaTableItemList.size() == 0) {
			JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(),
					"Cuando termine de agregar más productos, podra terminar esta venta", "Terminar Venta",
					JOptionPane.WARNING_MESSAGE);

			panelVenta.getCodigoBuscar().requestFocus();

			return;
		}

		try {
			final Venta venta = new Venta(0, new Date());
			final List<DetalleVenta> detalleVentaList = new ArrayList<DetalleVenta>();

			for (DetalleVentaTableItem dvil : detalleVentaTableItemList) {
				detalleVentaList.add(new DetalleVenta(0, 0, dvil.getCodigo(), dvil.getCantidad(), dvil.getPrecioVenta()));
			}

			ventaDAO.persist(venta, detalleVentaList);

			JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(), "Se guardo Correctamente, ...Imprimiendo ticket", "Venta", JOptionPane.INFORMATION_MESSAGE);
			if (ApplicationLogic.getInstance().isPrintingEnabled()) {
				new Thread() {
					@Override
					public void run() {
						imprimirTicket(venta, detalleVentaList);
					}
				}.start();
			}
			estadoInicial();

		} catch (EntidadExistenteException ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(), ex.getMessage(), "Venta", JOptionPane.ERROR_MESSAGE);

		} finally {
			panelVenta.getCodigoBuscar().requestFocus();
		}
	}

	private void cancelar_ActionPerformed() {
		int r = JOptionPane.showConfirmDialog(FramePrincipalControl.getInstance().getFramePrincipal(), "¿Cancelar la Venta Actual?", "Venta", JOptionPane.YES_NO_OPTION);
		if (r == JOptionPane.YES_OPTION) {
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
		if (detalleVentaTableItemList.size() > 0) {
			FramePrincipalControl.getInstance().setEnabledVentasMenus(true);

			this.panelVenta.getTerminar().setEnabled(true);
			this.panelVenta.getCancelar().setEnabled(true);
		} else {
			FramePrincipalControl.getInstance().setEnabledVentasMenus(false);
			this.panelVenta.getTerminar().setEnabled(false);
			this.panelVenta.getCancelar().setEnabled(false);
		}

		for (DetalleVentaTableItem dvti : detalleVentaTableItemList) {
			total += dvti.getImporete();
		}
		panelVenta.getTotal().setText(df.format(total));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == panelVenta.getDetalleVentaJTable()) {
			//System.err.println("->>mouseClicked:"+e.getClickCount()+" "+panelVenta.getDetalleVentaJTable().getSelectedRow()+","+panelVenta.getDetalleVentaJTable().getSelectedColumn());
			if (e.getClickCount() == 2 && panelVenta.getDetalleVentaJTable().getSelectedColumn() == 0) {
				editarCantidad(panelVenta.getDetalleVentaJTable().getSelectedRow());
			}
		}
	}

	void updateSelectedRow() {
		int sr = panelVenta.getDetalleVentaJTable().getSelectedRow();
		System.out.println("=>Selected:sr=" + sr);
		if(sr != -1){
			DetalleVentaTableItem prod = detalleVentaTableItemList.get(sr);
			cargarImagenDeProducto(prod.getProducto());
		} else {
			cargarImagenDeProducto(null);
		}
	}

	private void cargarImagenDeProducto(Producto prod) {
		BufferedImage imagenDeProducto = null;
		if(prod!=null){
			try {
				imagenDeProducto = ImageIO.read(new FileInputStream(DialogEdicionProductoControl.getImgPath() + prod.getCodigo().toUpperCase() + ".jpg"));
				System.out.println("->cargarImagenDeProducto: "+imagenDeProducto);
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
				imagenDeProducto = null;
			}
		}
		if(imagenDeProducto != null){
			panelVenta.getImgProducto().setIcon(new ImageIcon(imagenDeProducto));
		} else if(panelVenta.getImgProducto().getIcon() != null){
			panelVenta.getImgProducto().setIcon(null);
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
		Object result = JOptionPane.showInputDialog(panelVenta, "Cambiar la Cantidad:", cantidad);
		if (result != null) {
			Integer nuevaCantidad = new Integer(result.toString().trim());

			if (nuevaCantidad > 0) {
				detalleVentaTableItemList.get(selectedRow).setCantidad(nuevaCantidad);
				panelVenta.getDetalleVentaJTable().updateUI();
				renderTotal();
			}
		}
		panelVenta.getCodigoBuscar().requestFocus();

	}

	void ventaeliminarProdMenu() {
		int selectedRow = panelVenta.getDetalleVentaJTable().getSelectedRow();
		if (selectedRow >= 0) {
			detalleVentaTableItemList.remove(selectedRow);
			panelVenta.getDetalleVentaJTable().updateUI();
			renderTotal();

			panelVenta.getDetalleVentaJTable().getSelectionModel().clearSelection();
			panelVenta.getCodigoBuscar().requestFocus();
		}
	}

	void verVentaActual() {
		panelVenta.getCodigoBuscar().requestFocus();
	}

	private void imprimirTicket(Venta venta, List<DetalleVenta> detalleVentaList) {
		HashMap<String, String> extraInformation = new HashMap<String, String>();

		extraInformation.put("recibimos", "100000.45");
		extraInformation.put("cambio", "2323.00");

		boolean printed = false;
		try {
			Object ticketFile = ticketPrinteService.generateTicket(venta, detalleVentaList, extraInformation);
			ticketPrinteService.sendToPrinter(ticketFile);
			printed = true;
		} catch (IOException ioe) {
			//ioe.printStackTrace(System.err);
			JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(), "Error al imprimir Ticket", "Imprimir Ticket", JOptionPane.ERROR_MESSAGE);
		} finally {
			System.err.println("------------->>> DESPUES DE IMPRIMIR TICKET");
			if (!printed) {
				System.err.println("------------->>> ERROR AL IMPRIMIR");
				//t.printStackTrace(System.err);
				JOptionPane.showMessageDialog(FramePrincipalControl.getInstance().getFramePrincipal(), "Error grave al imprimir Ticket", "Imprimir Ticket", JOptionPane.ERROR_MESSAGE);
			}
			panelVenta.getCodigoBuscar().requestFocus();
		}
	}
}
