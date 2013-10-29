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
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 *
 * @author Softtek
 */
public class ApplicationLogic {
	private static final String version = "0.9.10";
	private static final String ULR_VERSION_FILE = "http://www.xpressosystems.com/xcd/version.txt";
	private static final String ULR_APP_PACKAGE  = "http://www.xpressosystems.com/xcd/lastUpdate.zip";
	private static final String FILE_APP_PACKAGE = "./lastUpdate.zip";
	
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
	
	private void updateApplication() {
		URL url=null;
		BufferedReader br = null;
		InputStream is = null;
		try{
			url = new URL(ULR_APP_PACKAGE);
			is = url.openStream();
			FileOutputStream fos = new FileOutputStream(FILE_APP_PACKAGE);
			byte[] buffer = new byte[1024 * 32];
			int r = -1;
			while ((r = is.read(buffer, 0, buffer.length)) != -1) {
				fos.write(buffer, 0, r);
				fos.flush();
			}
			is.close();
			fos.close();
		} catch (IOException ex) {
			throw new IllegalStateException("Can't download UPDATE data package:"+ex.getMessage());
		}
		try {
			extractFolder(FILE_APP_PACKAGE);
		} catch (IOException ex) {
			throw new IllegalStateException("Can't extract & deflate UPDATE data paclkage:"+ex.getMessage());
		}

	}

	private void extractFolder(String zipFile) throws ZipException, IOException {
		System.out.println(zipFile);
		int BUFFER = 2048;
		File file = new File(zipFile);

		ZipFile zip = new ZipFile(file);
		String destPathToInflate = ".";

		Enumeration zipFileEntries = zip.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(destPathToInflate, currentEntry);
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
	}
}
