/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.control;

import com.xpressosystems.xpresscashdrawer.dao.EntidadInexistenteException;
import com.xpressosystems.xpresscashdrawer.dao.PreferenciaDAO;
import com.xpressosystems.xpresscashdrawer.dao.PreferenciaDAOFactory;
import com.xpressosystems.xpresscashdrawer.model.Preferencia;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;

/**
 *
 * @author Softtek
 */
public class ApplicationLogic {
	public  static final String version = "0.9.11";
	private static final String ULR_VERSION_FILE = "http://dulcesaga.com.mx/xcd/version.txt";
	private static final String ULR_APP_PACKAGE  = "http://dulcesaga.com.mx/xcd/UPDATE_BUILD.zip";
	private static final String FILE_APP_PACKAGE = "./UPDATE_BUILD.zip";
	
	private static final String VERSION_PROPERTY = "xpresscashdrawer.version";
	
	private static ApplicationLogic instance;
	private static PreferenciaDAO preferenciaDAO;
	private ApplicationLogic(){	
	}

	public String getNombreNegocio() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("NOMBRE_NEGOCIO");
		return preferencia.getValor();
	}

	public String getDireccion() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("DIRECCION_NEGOCIO");
		return preferencia.getValor();
	}

	public String getTelefonos() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("TELEFONOS_NEGOCIO");
		return preferencia.getValor();
	}

	public String getCliente() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("CLIENTE_DEFAULT");
		return preferencia.getValor();
	}

	public String getBTImpresora() {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
			List<Preferencia> all = preferenciaDAO.getAll();
			for(Preferencia p: all){
				System.err.println("\t->Preferencia:"+p);
			}
		}
		Preferencia preferencia = preferenciaDAO.getPreferencia("BLUETOOTHADDRESS_PRINTER");
		System.err.println("->getBTImpresora():"+preferencia.getValor());
		return preferencia.getValor();
	}
	
	public void setBTImpresora(String btaImpresora) {
		if(preferenciaDAO == null){
			preferenciaDAO = PreferenciaDAOFactory.getPreferenciaDAO();
		}
		Preferencia preferencia = new Preferencia("BLUETOOTHADDRESS_PRINTER",btaImpresora);
		try {
			preferenciaDAO.edit(preferencia);
		} catch (EntidadInexistenteException ex) {
			
		}
	}

	/**
	 * @return the instance
	 */
	public static ApplicationLogic getInstance() {
		if(instance == null){
			instance =  new ApplicationLogic();
		}
		return instance;
	}
	
	public boolean needsUpdateApplciation(){
		boolean updateApp =  false;
		
		URL url=null;
		InputStream is = null;
		BufferedReader br = null;
		try{
			url = new URL(ULR_VERSION_FILE);
			is = url.openStream();
		}catch(IOException ioe){
		
		}
		br = new BufferedReader(new InputStreamReader(is));
		String lineRead = null;
		try{
			while((lineRead = br.readLine()) != null) {
				if(lineRead.contains(VERSION_PROPERTY)){
					System.err.println("->needsUpdateApplciation:lineRead="+lineRead);
					String[] propValue = lineRead.split("=");
					String versionReadOfLine = propValue[1]; 
					if(versionReadOfLine.compareTo(version)>0){
						System.err.println("->needsUpdateApplciation: Ok, update!");
						return true;
					}
				}
			}
		} catch(IOException ioe){
		
		}
		
		return updateApp;
	}
	
	public void updateApplication(final UpdateApplicationListener ual) {
		new Thread(){

			@Override
			public void run() {
				downloadApplication(ual);
			}
		}.start();
	}
	
	public void cacellUpdateApplication() {
		keepDownlaod = false;
	}
	
	private boolean keepDownlaod;
	
	private void downloadApplication(final UpdateApplicationListener ual) {
		URL url=null;
		BufferedReader br = null;
		InputStream is = null;
		HttpURLConnection conn = null;
		
		try{
			url = new URL(ULR_APP_PACKAGE);
			conn = (HttpURLConnection)url.openConnection();
			int length = conn.getContentLength();
			is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(FILE_APP_PACKAGE);
			byte[] buffer = new byte[1024 * 16];
			int r = -1;
			int t= 0;
			keepDownlaod = true;
			while ((r = is.read(buffer, 0, buffer.length)) != -1) {
				if(!keepDownlaod){
					int resp = JOptionPane.showConfirmDialog(null, "¿Desea cancelar la descarga ?", "Cancelar", 
							JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(resp == JOptionPane.YES_OPTION){
						break;
					} else {
						keepDownlaod = true;
					}
				}
				t += r;
				fos.write(buffer, 0, r);
				fos.flush();
				int advance = (100 * t) / length;
				System.err.print("Downloaded:\t"+advance+" % \r");
				ual.updateProgress(advance);				
			}
			System.err.println("");
			System.err.println("finished");
			is.close();
			fos.close();
			if(!keepDownlaod){
				throw new IllegalStateException("Update Canceled");
			} else {
				extractFolder(FILE_APP_PACKAGE);
				JOptionPane.showMessageDialog(null, "Se ha actualizado la Aplicación, \nReinicie por favor.", 
						"Actualización", JOptionPane.INFORMATION_MESSAGE);
				//System.exit(2);			
			}
		} catch (IOException ex) {
			throw new IllegalStateException("Can't download UPDATE data package:"+ex.getMessage());
		}
		/*
		try {
			extractFolder(FILE_APP_PACKAGE);
			JOptionPane.showMessageDialog(null, "Se ha actualizado la Aplicación, \nPor favor reinicie nuevamente", 
					"Actualización", JOptionPane.INFORMATION_MESSAGE);
			System.exit(2);
		} catch (IOException ex) {
			throw new IllegalStateException("Can't extract & deflate UPDATE data paclkage:"+ex.getMessage());
		}
		*/
	}

	private void extractFolder(String zipFile) throws ZipException, IOException {
		System.out.println(zipFile);
		int BUFFER = 2048;
		File file = new File(zipFile);

		ZipFile zip = new ZipFile(file);
		String destPathToInflate = ".";

		Enumeration zipFileEntries = zip.entries();

		// Process each entry
		System.err.println("-> extracting :");
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(destPathToInflate, currentEntry);
			System.err.println("-> inflating :"+destFile.getPath());
			//destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zip
						.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		}
		System.err.println("-> OK, finish extracting.");
	}

	public boolean canDownlaodUpdateApplication() {
		try {
			URL  url = new URL(ULR_APP_PACKAGE);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			int length = conn.getContentLength();
			if(length > 1024*1024){
				return true;
			} else{
				return false;
			}
		} catch(Exception ex){
			ex.printStackTrace(System.err);
			return false;
		}
	}
}
