/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.ticket;

import com.xpressosystems.xpresscashdrawer.control.ApplicationLogic;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author alfredo
 */
public interface TicketPrinteService {
    Object generateTicket(Venta venta,List<DetalleVenta> pedidoVentaDetalleCollection,HashMap<String,String> extraInformation) throws IOException ;
    void sendToPrinter(Object objectToPrint) throws IOException ;
    void testDefaultPrinter() throws IOException;
	void setApplicationLogic(ApplicationLogic al);
}
