/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.ticket.bluetooth;

import com.xpressosystems.xpresscashdrawer.model.BTDevice;
import java.io.IOException;
import java.util.ArrayList;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Minimal Device Discovery example.
 */
public class RemoteDeviceDiscovery {
        //private static HashMap<String,String> discoveredDevices = new HashMap<String,String> ();
        //private static boolean discoverComplretted;

        public static ArrayList<BTDevice> discoverDevices()throws IOException, InterruptedException {
                
        final Object inquiryCompletedEvent = new Object();
                final ArrayList<BTDevice> discoveredDevices = new ArrayList<BTDevice> ();
        discoveredDevices.clear();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("-> BT Address: " + btDevice.getBluetoothAddress()+", Authenticated:"+btDevice.isAuthenticated() + " Class: "+cod);
                                String friendlyName = btDevice.getBluetoothAddress();
                try {
                                        friendlyName = btDevice.getFriendlyName(false);
                                        if(cod.toString().toLowerCase().contains("printer")){
                                        friendlyName = friendlyName+"(Impresora)";
                                        }
                    System.out.println("\tFriendly Name:" + friendlyName);                                        
                } catch (IOException cantGetDeviceName) {
                                        System.out.println("\t:( no  name");
                }
                                discoveredDevices.add(new BTDevice(btDevice.getBluetoothAddress(),friendlyName));                
            }

            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        synchronized (inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("-->>> wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(discoveredDevices.size() + " device(s) found");
            }
        }
                return discoveredDevices;
    }
}