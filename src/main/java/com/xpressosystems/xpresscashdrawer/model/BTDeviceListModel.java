/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Softtek
 */
public class BTDeviceListModel implements ListModel {
	private ArrayList<BTDevice> devicesList;
	private List<ListDataListener> listDataListener;
	public BTDeviceListModel(ArrayList<BTDevice> discoveredDevices) {
		this.devicesList = discoveredDevices;
		listDataListener = new ArrayList<ListDataListener>();		
	}
	
	@Override
	public int getSize() {
		return devicesList.size();
	}

	@Override
	public Object getElementAt(int index) {
		return devicesList.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listDataListener.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listDataListener.remove(l);
	}
	
}
