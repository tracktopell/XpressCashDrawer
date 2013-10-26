/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.model;

/**
 *
 * @author Softtek
 */
public class BTDevice {
	private String blueToothAddress;
	private String friendlyName;

	public BTDevice(String blueToothAddress, String friendlyName) {
		this.blueToothAddress = blueToothAddress;
		this.friendlyName = friendlyName;
	}
	
	/**
	 * @return the blueToothAddress
	 */
	public String getBlueToothAddress() {
		return blueToothAddress;
	}

	/**
	 * @param blueToothAddress the blueToothAddress to set
	 */
	public void setBlueToothAddress(String blueToothAddress) {
		this.blueToothAddress = blueToothAddress;
	}

	/**
	 * @return the friendlyName
	 */
	public String getFriendlyName() {
		return friendlyName;
	}

	/**
	 * @param friendlyName the friendlyName to set
	 */
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	@Override
	public String toString() {
		return friendlyName+"\t["+blueToothAddress+"]";
	}
	
}
