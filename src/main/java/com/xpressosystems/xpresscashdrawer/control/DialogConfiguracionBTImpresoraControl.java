/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.model.BTDevice;
import com.xpressosystems.xpresscashdrawer.model.BTDeviceListModel;
import com.xpressosystems.xpresscashdrawer.ticket.bluetooth.RemoteDeviceDiscovery;
import com.xpressosystems.xpresscashdrawer.ticket.bluetooth.TicketBlueToothPrinter;
import com.xpressosystems.xpresscashdrawer.view.DialogConfiguracionBTImpresora;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Softtek
 */
public class DialogConfiguracionBTImpresoraControl implements ActionListener, ListSelectionListener {
	DialogConfiguracionBTImpresora dcbt;

	public DialogConfiguracionBTImpresoraControl(DialogConfiguracionBTImpresora dcbt) {
		this.dcbt = dcbt;

		this.dcbt.getProbarImpresoraBtn().addActionListener(this);
		this.dcbt.getExplorar().addActionListener(this);
		this.dcbt.getAceptar().addActionListener(this);
		this.dcbt.getCancelar().addActionListener(this);
		this.dcbt.getListaDispositivos().addListSelectionListener(this);
		this.dcbt.getListaDispositivos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void estadoInicial() {
		dcbt.getProbarImpresoraBtn().setEnabled(false);
		dcbt.getAceptar().setEnabled(false);
		dcbt.getCancelar().setEnabled(false);
		new Thread() {
			public void run() {
				dcbt.getBtaddressActual().setText(ApplicationLogic.getInstance().getBTImpresora());
			}
		}.start();

		new Thread() {
			public void run() {
				actualizarDispositivos();
			}
		}.start();
		dcbt.setVisible(true);
	}

	public void actualizarDispositivos() {
		ArrayList<BTDevice> discoverDevices = null;
		Cursor prevCursor = dcbt.getCursor();
		try {
			dcbt.getCancelar().setEnabled(false);
			dcbt.getProbarImpresoraBtn().setEnabled(false);
			dcbt.getListaDispositivos().setModel(new DefaultListModel());
			dcbt.getListaDispositivos().setEnabled(false);			
			dcbt.getExplorar().setEnabled(false);
			dcbt.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));			
			discoverDevices = RemoteDeviceDiscovery.discoverDevices();
			dcbt.getListaDispositivos().setModel(new BTDeviceListModel(discoverDevices));
		} catch (IOException ioe) {
		} catch (InterruptedException ie) {
		} finally {
			dcbt.getCancelar().setEnabled(true);
			dcbt.getProbarImpresoraBtn().setEnabled(true);			
			dcbt.getListaDispositivos().setEnabled(true);
			dcbt.setCursor(prevCursor);
			dcbt.getExplorar().setEnabled(true);
			dcbt.getProbarImpresoraBtn().setEnabled(true);
			dcbt.getCancelar().setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dcbt.getProbarImpresoraBtn()) {
			probarImpresoraBtn_actionPerformed();
		} else if (e.getSource() == dcbt.getAceptar()) {
			aceptar_actionPerformed();
		} else if (e.getSource() == dcbt.getCancelar()) {
			cancelar_actionPerformed();
		} else if (e.getSource() == dcbt.getExplorar()) {
			explorar_actionPerformed();
		}
	}

	private void probarImpresoraBtn_actionPerformed() {
		try {
			TicketBlueToothPrinter.getInstance().testDefaultPrinter();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
			JOptionPane.showMessageDialog(dcbt, "Error al enviar a imprimir prueba:" + ex.getMessage(),
					"Imprimr prueba", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void explorar_actionPerformed() {
		new Thread() {
			public void run() {
				actualizarDispositivos();
			}
		}.start();
		dcbt.setVisible(true);
	}

	private void aceptar_actionPerformed() {
		int selectedIndex = dcbt.getListaDispositivos().getSelectedIndex();
		System.err.println("->>aceptar_actionPerformed: selectedIndex=" + selectedIndex);
		if (selectedIndex < 0) {
			return;
		}
		BTDevice selectedBTDeviceAt = (BTDevice) ((BTDeviceListModel) dcbt.getListaDispositivos().getModel()).
				getElementAt(selectedIndex);
		
		System.err.println("->> update BlueTooth Address for Printer");
		ApplicationLogic.getInstance().setBTImpresora(selectedBTDeviceAt.getBlueToothAddress());
		System.err.println("->> OK, updated !");
		dcbt.getBtaddressActual().setText(selectedBTDeviceAt.getBlueToothAddress());
		dcbt.getAceptar().setEnabled(false);
	}

	private void cancelar_actionPerformed() {
		System.err.println("->>cancelar_actionPerformed");
		dcbt.dispose();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.err.println("->valueChanged:FirstIndex=" + e.getFirstIndex() + ", LastIndex=" + e.getLastIndex() + ", isAdjusting?" + e.getValueIsAdjusting()+"; SelectedIndex="+dcbt.getListaDispositivos().getSelectedIndex());
		int selectedIndex = dcbt.getListaDispositivos().getSelectedIndex();
		
		if(selectedIndex>=0){
			BTDevice selectedBTDeviceAt = (BTDevice) ((BTDeviceListModel) dcbt.getListaDispositivos().getModel()).
				getElementAt(selectedIndex);
			if (!selectedBTDeviceAt.getBlueToothAddress().equals(ApplicationLogic.getInstance().getBTImpresora()) && 
					selectedBTDeviceAt.getFriendlyName().contains("Impresora")) {				
				dcbt.getAceptar().setEnabled(true);		
			} else {
				dcbt.getAceptar().setEnabled(false);		
			}
		} else{
			dcbt.getAceptar().setEnabled(false);
		}
	}
}
