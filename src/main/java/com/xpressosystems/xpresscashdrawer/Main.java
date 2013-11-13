/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer;

import com.xpressosystems.xpresscashdrawer.control.ApplicationLogic;
import com.xpressosystems.xpresscashdrawer.control.DialogLoginControl;
import com.xpressosystems.xpresscashdrawer.control.FramePrincipalControl;
import com.xpressosystems.xpresscashdrawer.control.UpadateApplicationJFrameControl;
import com.xpressosystems.xpresscashdrawer.dao.jdbc.DataSourceAdaptor;
import com.xpressosystems.xpresscashdrawer.view.DialogLogin;
import com.xpressosystems.xpresscashdrawer.view.UpadateApplicationJFrame;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author alfredo
 */
public class Main {

	private static Logger logger = Logger.getLogger(Main.class);
	public static final String INTELBTH = "intelbth";

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {		
		FramePrincipalControl framePrincipalControl = null;
		DialogLoginControl dialogLoginControl = null;
		//debugClassLoader();
		
		isSingleInstanceRunning();
		
		if( ApplicationLogic.getInstance().needsUpdateApplciation()) {
			UpadateApplicationJFrame uaf = new UpadateApplicationJFrame();
			UpadateApplicationJFrameControl uafc = new UpadateApplicationJFrameControl(uaf);
			uafc.estadoInicial();
		}
		
		try {
			framePrincipalControl = FramePrincipalControl.getInstance();

			DialogLogin dialogLogin = DialogLogin.getInstance(framePrincipalControl.getFramePrincipal());
			dialogLoginControl = new DialogLoginControl(dialogLogin);
			
						
			framePrincipalControl.estadoInicial();
			
			dialogLoginControl.estadoInicial();
			
			if(! dialogLoginControl.isLoggedIn()){
				throw new IllegalAccessException("COntraseña incorrecta");
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	private static void isSingleInstanceRunning(){
		Connection conn = null;
		try{
			conn = DataSourceAdaptor.getConnection();			
		} catch(Exception ex){
			ex.printStackTrace(System.err);
		} finally {
			if(conn == null) {
				System.err.println("-->> Another instance is running !");
				
				JOptionPane.showMessageDialog(null, "Ya esta corriendo la Aplicación en otra ventana", "Iniciar", JOptionPane.ERROR_MESSAGE);
				
				System.exit(1);
			}
		}
	}

	private static void debugClassLoader() {
		String os_arch = System.getProperty("os.arch");
		System.out.println("OS_ARCH:->"+os_arch);
		System.setProperty("os.arch", "x86");
		os_arch = System.getProperty("os.arch");
		System.out.println("\tOS_ARCH:->>>>"+os_arch);
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
 
		URL[] urls = ((URLClassLoader)cl).getURLs();
 
		for(URL url: urls){
			String cpFile = url.getFile();
			System.out.println("CLASSPATH:->"+cpFile);
			if(cpFile.toLowerCase().contains(".jar")){
				try {
					JarFile jarCPFile = new JarFile(new File(cpFile));
					Enumeration<JarEntry> jarEntries = jarCPFile.entries();
					while(jarEntries.hasMoreElements()){
						JarEntry nextJarEntry = jarEntries.nextElement();
						if(nextJarEntry.getName().contains(INTELBTH)){
							System.out.println("\t=> IN JAR:"+nextJarEntry);
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("SYSTEM_ENV:-> %s=%s%n",
							  envName,
							  env.get(envName));
		}		

		Properties sysProp = System.getProperties();		
		Set sysPropertiesSet = sysProp.keySet();
		
		for(Object k: sysPropertiesSet){
			String var = sysProp.getProperty(k.toString());						
			System.out.print("SYSTEM_PROP:->"+k+"="+var);						
			if(k.toString().toLowerCase().contains("java.library.path")){
				System.out.println("");
				String[] varDisr = var.split(";");
				for(String dir: varDisr){
					System.out.println("\t=>DIR:"+dir);
					File fileDir = new File(dir);
					File[] filesInDir = fileDir.listFiles();
					if(filesInDir != null){
						for(File fin: filesInDir){
							if(fin.getName().contains(INTELBTH)){
								System.out.println("\t\t (*) FILE :"+fin.getAbsolutePath());
							} else {
								//System.out.println("\t\t     FILE :"+fin.getAbsolutePath());
							}
							
						}
					}
				}
			} else {
				System.out.println("");				
			}
		}
		System.out.println("=========================================================");
	}
}
