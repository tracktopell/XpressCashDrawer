/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.ticket.bluetooth;

import com.xpressosystems.xpresscashdrawer.control.ApplicationLogic;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAO;
import com.xpressosystems.xpresscashdrawer.dao.ProductoDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.DetalleVenta;
import com.xpressosystems.xpresscashdrawer.model.Producto;
import com.xpressosystems.xpresscashdrawer.model.Venta;
import com.xpressosystems.xpresscashdrawer.ticket.NumeroCastellano;
import com.xpressosystems.xpresscashdrawer.ticket.TicketPrinteService;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author alfredo
 */
public class TicketBlueToothPrinter implements TicketPrinteService {
    private static final String SPACES = "                                ";
    public  static final String DEFAULT_BT_PRINTER = "00037A66B839";
    private static final String TICKET_TEST        = "/ticket_layout/TICKET_TEST.txt";    
    private static final String TICKET_LAYOUT_FILE = "/ticket_layout/TICKET_DEFINITION.txt";
    private static SimpleDateFormat sdf_fecha_full = new SimpleDateFormat("yyyyMMdd_hhmmss");    
    private static SimpleDateFormat sdf_fecha = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat sdf_hora = new SimpleDateFormat("HH:mm");
    private static final int MAX_CHARS_PER_COLUMN = 32;
	public static final String BT_PRINTER_MODE = "Bluetooth";	
    private String btAdress;
	private ApplicationLogic applicationLogic;
	private static TicketBlueToothPrinter instance;
	
	private TicketBlueToothPrinter(){
	
	}
	
	/**
	 * @return the instance
	 */
	public static TicketBlueToothPrinter getInstance() {
		if(instance == null){
			instance = new TicketBlueToothPrinter();
		}
		return instance;
	}

	
	public String getBtAdress(){
		if(applicationLogic!=null ){
			btAdress = applicationLogic.getBTImpresora();
		} 
		return btAdress;
	}
	
	
	@Override
    public Object generateTicket(Venta pedidoVenta,List<DetalleVenta> pedidoVentaDetalleCollection,HashMap<String,String> extraInformation) throws IOException {
        String tiketPrinted = null;
        PrintStream psPrintTicket = null;
        ProductoDAO productoDAO=null;
        
        InputStream is = TicketBlueToothPrinter.class.getResourceAsStream(TICKET_LAYOUT_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            String line = null;
            Date fecha = new Date();
            tiketPrinted = "TICKET_"+pedidoVenta.getId()+"_"+sdf_fecha_full.format(fecha)+".TXT";
            psPrintTicket = new PrintStream(new File(tiketPrinted),"ISO-8859-1");
			productoDAO = ProductoDAOFactory.getProductoDAO();
            HashMap<String, String> staticVars = new HashMap();
            staticVars.put("${FECHA}", sdf_fecha.format(fecha));
            staticVars.put("${HORA}", sdf_hora.format(fecha));
            staticVars.put("${CLIENTE}", applicationLogic.getCliente());

			String nombreEmpresa = applicationLogic.getNombreNegocio();

            List<String> nombreEmpresaList = justifyText(nombreEmpresa, MAX_CHARS_PER_COLUMN - 2 );

            staticVars.put("${NOMBRE_EMRPESA_0}", alignTextCenter(nombreEmpresaList.get(0),MAX_CHARS_PER_COLUMN - 2));
            if (nombreEmpresaList.size() > 1) {
                staticVars.put("?{NOMBRE_EMRPESA_1}", alignTextCenter(nombreEmpresaList.get(1),MAX_CHARS_PER_COLUMN - 2));
            }
            
            String direccion = applicationLogic.getDireccion();

            List<String> direccionList = justifyText(direccion, MAX_CHARS_PER_COLUMN - 2 );

            staticVars.put("${DIRECCION_0}", alignTextCenter(direccionList.get(0),MAX_CHARS_PER_COLUMN - 2));
            if (direccionList.size() > 1) {
                staticVars.put("?{DIRECCION_1}", alignTextCenter(direccionList.get(1),MAX_CHARS_PER_COLUMN - 2));
            }
            if (direccionList.size() > 2) {
                staticVars.put("?{DIRECCION_2}", alignTextCenter(direccionList.get(2),MAX_CHARS_PER_COLUMN - 2));
            }
            if (direccionList.size() > 3) {
                staticVars.put("?{DIRECCION_3}", alignTextCenter(direccionList.get(3),MAX_CHARS_PER_COLUMN - 2));
            }
			staticVars.put("${TELEFONO_0}", alignTextCenter(applicationLogic.getTelefonos(),MAX_CHARS_PER_COLUMN - 2));

            boolean skipLine = false;
            boolean detailStart = false;
            boolean expandDetail = false;
            int numIter = 1;
            List<String> iterationLines = new ArrayList<String>();
            
            while ((line = br.readLine()) != null) {
                if (line.contains("#{")) {
                    if (line.contains("#{DETAIL_START}")) {
                        detailStart = true;
                        iterationLines.clear();
                        continue;
                    } else if (line.contains("#{DETAIL_END}")) {
                        detailStart = false;
                        expandDetail = true;
                    }
                } else if (!detailStart) {
                    iterationLines.clear();
                    iterationLines.add(line);
                    //System.err.print("#_>>"+iterationLines.size()+"\t");
                }

                if (detailStart) {
                    iterationLines.add(line);
                    //System.err.println("#=>>"+line);
                    continue;
                }

                if (expandDetail) {
                    numIter = pedidoVentaDetalleCollection.size();
                } else {
                    numIter = 1;
                }
                DecimalFormat df_6 = new DecimalFormat("#####0");
                DecimalFormat dfs5_2 = new DecimalFormat("##,##0.00");


                double sum_importe = 0.0;
                double importe = 0.0;
                double sum_desc = 0.0;
                double desc = 0.0;

                for (int i = 0; i < numIter; i++) {

                    if (expandDetail) {
						
						Producto prod=productoDAO.getProducto(pedidoVentaDetalleCollection.get(i).getProductoCodigo());
						
						//${CODIGO } ${PRODUCTO}
						//   ${CANT} *  ${PRECIO}  ${IMP}
						staticVars.put("${CANT}"    , alignTextRigth(df_6.format(pedidoVentaDetalleCollection.get(i).getCantidad()),4));
                        staticVars.put("${PRODUCTO}", alignTextLeft(prod.getNombre(),25));
                        
						staticVars.put("${CODIGO}", alignTextRigth(pedidoVentaDetalleCollection.get(i).getProductoCodigo(),14));                        
						
                        importe  = pedidoVentaDetalleCollection.get(i).getCantidad() * pedidoVentaDetalleCollection.get(i).getPrecioVenta();
                        sum_importe += importe;
                        sum_desc += desc;

                        staticVars.put("${PRECIO}" , alignTextRigth(dfs5_2.format(pedidoVentaDetalleCollection.get(i).getPrecioVenta()),9));
                        
                        staticVars.put("${IMP}", alignTextRigth(dfs5_2.format(importe),9));
                    }

                    for (String innerLine : iterationLines) {

                        skipLine = false;
                        Iterator<String> ik = staticVars.keySet().iterator();
                        while (ik.hasNext()) {
                            String k = ik.next();
                            if (innerLine.contains(k)) {
                                innerLine = innerLine.replace(k, staticVars.get(k));
                            }
                            //System.err.println("\t\t===>>> replace "+k+" ->"+staticVars.get(k));
                        }
                        if (innerLine.indexOf("?{") >= 0) {
                            String optionalField = innerLine.substring(innerLine.indexOf("?{"), innerLine.indexOf("}"));
                            if (!staticVars.containsKey(optionalField)) {
                                skipLine = true;
                            }
                        }
                        if (!skipLine) {
                            //System.err.println("=>>" + innerLine);
                            psPrintTicket.print(innerLine+"\r");
                        } else {
                            //System.err.println("X=>>" + innerLine);
                        }

                    }
                }

                if (expandDetail) {
                    //System.err.println("#=>>______________");
                    expandDetail = false;
					final double subTotal = sum_importe;
                    staticVars.put("${SBTOT}" , alignTextRigth(dfs5_2.format(subTotal),9));
                    //staticVars.put("${DESCUENTO}", alignTextRigth(df_6_2.format(sum_desc),12));
                    double total = sum_importe - sum_desc;
                    String strTotal = dfs5_2.format(total);
                    staticVars.put("${TOTAL}", alignTextRigth(strTotal,9));
					String recibimosOriginal = extraInformation.get("recibimos").toString();
					String recibimos = recibimosOriginal;
					if(recibimos!=null && recibimos.trim().length()>0){
						recibimos = dfs5_2.format(Double.parseDouble(recibimos));
					}
					staticVars.put("${RECIB}", alignTextRigth(recibimos,9));
					String suCambio = extraInformation.get("cambio").toString();
					if(suCambio == null || suCambio.trim().length()==0){
						suCambio = dfs5_2.format(Double.parseDouble(recibimosOriginal) - total);
					}
					staticVars.put("${CAMBI}", alignTextRigth(suCambio,9));
					
                    String enterosLetra  = NumeroCastellano.numeroACastellano(Long.parseLong(strTotal.substring(0, strTotal.indexOf(".")))).toUpperCase().trim();
                    String centavosLetra = strTotal.substring(strTotal.indexOf(".") + 1);

                    List<String> totalLetraList = justifyText("SON [" + enterosLetra + " " + centavosLetra + "/100 M.N.]", MAX_CHARS_PER_COLUMN-2);

                    staticVars.put("${TOTAL_LETRA_0}", totalLetraList.get(0));
                    if (totalLetraList.size() > 1) {
                        staticVars.put("?{TOTAL_LETRA_1}", totalLetraList.get(1));
                    }
                }
            }
        } catch (IOException ex) {
            //ex.printStackTrace(System.err);
            throw new IllegalStateException("No sepuede generar el Ticket:");
        } finally {
            if(psPrintTicket!= null){
                psPrintTicket.close();
            }
        }

        return tiketPrinted;
    }

	private static String alignTextCenter(String text, int maxPerColumn) {
        try{
			int length  = text.trim().length(); 
			int lengthL = (maxPerColumn - length)/2;
			int lengthR = (maxPerColumn - length) - lengthL;
			
            if(length<maxPerColumn){                
                return SPACES.substring(0,lengthL)+
                        text +
						SPACES.substring(0,lengthR);
            }else {
                return text.trim().substring(0, maxPerColumn);
            }
        }catch(Exception ex){
            System.err.println("\t==>>> alignTextRigth: ->"+text.trim()+"<-["+text.trim().length()+"],"+maxPerColumn+":"+ex.getMessage());
            return text.trim();
        }
    }

    private static String alignTextRigth(String text, int maxPerColumn) {
        try{
            if(text.trim().length()<maxPerColumn){
                
                return SPACES.substring(0,maxPerColumn-text.trim().length())+
                        text;
            }else {
                return text.trim().substring(0, maxPerColumn);
            }
        }catch(Exception ex){
            System.err.println("\t==>>> alignTextRigth: ->"+text.trim()+"<-["+text.trim().length()+"],"+maxPerColumn+":"+ex.getMessage());
            return text.trim();
        }
    }
    
    private static String alignTextLeft(String text, int maxPerColumn) {
        try{
            if(text.trim().length()<maxPerColumn){                
                return text+SPACES.substring(0,maxPerColumn-text.trim().length());
            }else {
                return text.trim().substring(0, maxPerColumn);
            }
        }catch(Exception ex){
            System.err.println("\t==>>> alignTextLeft: ->"+text.trim()+"<-["+text.trim().length()+"],"+maxPerColumn+":"+ex.getMessage());
            return text.trim();
        }
    }

    private static List<String> justifyText(String text, int maxPerColumn) {
        List<String> result = new ArrayList<String>();

        String[] words = text.split("\\s");
        String currentLine = "";

        for (String w : words) {
            if (w.trim().length() == 0) {
                continue;
            }
            if (currentLine.length() + 1 + w.length() <= maxPerColumn) {
                if (currentLine.length() == 0) {
                    currentLine = w;
                } else {
                    currentLine = currentLine + " " + w;
                }

            } else {
                result.add(currentLine);
                currentLine = w;
            }
        }

        if (currentLine.length() > 0) {
            result.add(currentLine);
            currentLine = "";
        }

        return result;
    }
    
	
	@Override
    public void testDefaultPrinter() throws IOException {
        InputStream is = TicketBlueToothPrinter.class.getResourceAsStream(TICKET_TEST);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
		Date fecha = new Date();
		
		String fileNameTest = "./TICKET_TEST_"+sdf_fecha_full.format(fecha)+".txt";
            
		FileOutputStream fos = new FileOutputStream(fileNameTest);
		PrintStream psTestPrint    = new PrintStream(fos,true,"ISO-8859-1");

        try {
            String line = null;
            
            HashMap<String, String> staticVars = new HashMap();
            //${FECHA},${HORA},${BTADDERSS}
            staticVars.put("${FECHA}", sdf_fecha.format(fecha));
            staticVars.put("${HORA}", sdf_hora.format(fecha));
			staticVars.put("${BTADDERSS}", getBtAdress());
			
			Set<String> keySet = staticVars.keySet();
			for(String key: keySet){
				System.err.println("\t-->>key:"+key+"="+staticVars.get(key));				
			}
			while((line=br.readLine())!=null){
				//final String key = "${FECHA}";
				for(String key: keySet){
					if(line.contains(key)){
						line = line.replace(key, staticVars.get(key));
					}
				}
				//System.err.println("-->>testDefaultPrinter:"+line);
				psTestPrint.print(line+"\r");
			}
			br.close();
			psTestPrint.close();
			fos.close();
			
			SendBytesToDevice.print(getBtAdress(), fileNameTest);
			
        } catch (IOException ex) {
            //ex.printStackTrace(System.err);
            throw new IllegalStateException("No se puede generar la Prueba");
        } finally {
            
        }
    }

	@Override
	public void sendToPrinter(Object ticketFileName) throws IOException {
		SendBytesToDevice.print(getBtAdress(),(String)ticketFileName);
	}
	
	/**
	 * @param al the al to set
	 */
	public void setApplicationLogic(ApplicationLogic al) {
		this.applicationLogic = al;
	}
	
}
