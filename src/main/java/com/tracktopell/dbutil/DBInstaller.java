package com.tracktopell.dbutil;

import java.io.*;

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 *
 * @author Alfredo Estrada
 */
public abstract class DBInstaller {

	public static final String CREATION_SCRIPT = "creation_script";
	public static final String INIT_ENVIRONMENT_SCRIPTS = "environment_script";
	protected static final String PARAM_CONNECTION_JDBC_CLASS_DRIVER = "jdbc.driverClassName";
	protected Properties parameters4CreateAndExecute;
	//protected  final String JDBC_CLASS_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	//protected  final String PARAMS_PROPERTIES = "/jdbc.properties";
	protected Logger logger;

	public DBInstaller(String proConfigLocation,String masterHost) throws IOException {
		logger = Logger.getLogger(DBInstaller.class);

		logger.debug("init(): proConfigLocation=" + proConfigLocation);

		InputStream propertiesIS = null;

		if (proConfigLocation.startsWith("file:")) {
			String fileToLaod = proConfigLocation.substring("file:".length());
			logger.debug("init(): fileToLaod=" + fileToLaod + ", can read ?" + new File(fileToLaod).canRead());
			propertiesIS = new FileInputStream(new File(fileToLaod));
		} else if (proConfigLocation.startsWith("classpath:")) {
			String resource = proConfigLocation.substring("classpath:".length());
			logger.debug("init(): resource=" + resource);
			propertiesIS = DBInstaller.class.getResourceAsStream(resource);
		} else {
			throw new IOException("For read properties, Pattern {\"file:\" | \"classpath:\"} not found in " + proConfigLocation);
		}

		parameters4CreateAndExecute = new Properties();

		logger.debug("init(): try to load properties from proConfigLocation from InputStream:" + propertiesIS);
		parameters4CreateAndExecute.load(propertiesIS);
		logger.debug("init(): Ok, loaded parameters4CreateAndExecute");
		
		if(masterHost != null){
			logger.debug("\tinit(): ===> replaceing for masterHost="+masterHost);
			parameters4CreateAndExecute.put("jdbc.url",parameters4CreateAndExecute.getProperty("jdbc.url").replace("${master.host}", masterHost));
			parameters4CreateAndExecute.put("jdbc.url_prefix",parameters4CreateAndExecute.getProperty("jdbc.url_prefix").replace("${master.host}", masterHost));
			parameters4CreateAndExecute.put("jdbc.url_replace",parameters4CreateAndExecute.getProperty("jdbc.url_replace").replace("${master.host}", masterHost));			
		}
		
		logger.debug("init(): parameters4CreateAndExecute=" + parameters4CreateAndExecute);
	}

	protected Properties preparePropertiesForConnection() {
		Properties prop4Connection = new Properties();
		Enumeration<String> propertyNames = (Enumeration<String>) parameters4CreateAndExecute.propertyNames();

		while (propertyNames.hasMoreElements()) {
			String propName = propertyNames.nextElement();
			if (propName.startsWith("jdbc.urlconn.")) {
				prop4Connection.put(propName.substring("jdbc.urlconn.".length()), parameters4CreateAndExecute.getProperty(propName));
			}
		}

		logger.debug("preparePropertiesForConnection: prop4Connection=" + prop4Connection);

		return prop4Connection;
	}

	protected abstract Properties preparePropertiesForCreateConnection();

	protected abstract Connection createConnectionForCreate() throws IllegalStateException, SQLException;

	protected Connection createConnectionForInit() throws IllegalStateException, SQLException {
		Connection conn = null;
		try {
			logger.debug("createConnectionForInit: ...try get Connection for Create DB.");
			Class.forName(parameters4CreateAndExecute.getProperty(PARAM_CONNECTION_JDBC_CLASS_DRIVER)).newInstance();
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException(ex.getMessage());
		} catch (InstantiationException ex) {
			throw new IllegalStateException(ex.getMessage());
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(ex.getMessage());
		}

		logger.debug("createConnectionForInit:Ok, Loaded JDBC Driver.");
		String urlConnection = parameters4CreateAndExecute.getProperty("jdbc.url");

		if (urlConnection.contains("${db.name}")) {
			urlConnection = urlConnection.replace("${db.name}", parameters4CreateAndExecute.getProperty("db.name"));
			logger.debug("createConnectionForInit:replacement for variable db.name, now urlConnection=" + urlConnection);
		}

		Properties preparePropertiesForConnection = preparePropertiesForConnection();

		logger.debug("createConnectionForInit:urlConnection=" + urlConnection + ", preparePropertiesForConnection()=" + preparePropertiesForConnection);
		conn = DriverManager.getConnection(urlConnection, preparePropertiesForConnection);
		logger.debug("createConnectionForInit:Connected to DB.");

		printDBInfo(conn);
		return conn;
	}

	private void printDBInfo(Connection conn) throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();

		System.out.println("\t=>>SchemaTerm:" + metaData.getSchemaTerm());

		System.out.println("Schemas:");

		ResultSet schemas = metaData.getSchemas();
		while (schemas.next()) {
			System.out.println("\t=>>" + schemas.getString("TABLE_SCHEM") + ", " + schemas.getString("TABLE_CATALOG"));
		}
		schemas.close();
		ResultSet tablesRS = metaData.getTables(null, null, "%", null);
		System.out.println("Tables:");
		Statement statement = conn.createStatement();
		while (tablesRS.next()) {
			String schemaTableIter = tablesRS.getString(2);
			String tableNameIter = tablesRS.getString(3);
			if(schemaTableIter.toLowerCase().contains("sys")){
				continue;
			}
			System.out.print("\t" + schemaTableIter + "." + tableNameIter+"(");
			ResultSet executeQuery = statement.executeQuery("SELECT * FROM "+schemaTableIter + "." + tableNameIter+" WHERE 1=2");
			ResultSetMetaData emptyTableMetaData = executeQuery.getMetaData();
			int columnCount = emptyTableMetaData.getColumnCount();
			for(int columNumber=1;columNumber<=columnCount;columNumber++) {
				if(columNumber>1){
					System.out.println(",");
				}else{
					System.out.println("");
				}
				int columnSize   = emptyTableMetaData.getPrecision(columNumber);
				int columnDD     = emptyTableMetaData.getScale(columNumber);
				int nullableFlag = emptyTableMetaData.isNullable(columNumber);
				boolean autoIncrementFlag = emptyTableMetaData.isAutoIncrement(columNumber);				
				
				System.out.print("\t\t" + emptyTableMetaData.getColumnName(columNumber)+ "  " + emptyTableMetaData.getColumnTypeName(columNumber));
				if(columnSize>0) {
					System.out.print(" ( " + columnSize);
					if(columnDD>0){
					
					}
					System.out.print(" )");
				}
				if(nullableFlag ==1) {
					System.out.print(" NULL");
				}
				if(autoIncrementFlag) {
					System.out.print(" AUTOINCREMENT");
				}
			}
			System.out.println();
			System.out.println("\t);");
			executeQuery.close();
			
			
			/*
			ResultSet resColumnsTable = metaData.getColumns(null, schemaTableIter, tableNameIter, null);
			for(int columnCounter = 0;resColumnsTable.next();columnCounter++) {
				if(columnCounter>0){
					System.out.println(",");
				}else{
					System.out.println("");
				}
				
				
				System.out.print("\t\t" + 
						resColumnsTable.getString("COLUMN_NAME")+ "  " + resColumnsTable.getString("TYPE_NAME"));
				
				int columnSize = resColumnsTable.getInt("COLUMN_SIZE");
				int columnDD   = resColumnsTable.getInt("DECIMAL_DIGITS");
				int nullableFlag = resColumnsTable.getInt("NULLABLE");
				boolean autoIncrementFlag = resColumnsTable.getString("IS_AUTOINCREMENT").equalsIgnoreCase("yes");
				
				if(columnSize>0) {
					System.out.print(" ( " + columnSize);
					if(columnDD>0){
					
					}
					System.out.print(" )");
				}
				if(nullableFlag ==1) {
					System.out.print(" NULL");
				}
				if(autoIncrementFlag) {
					System.out.print(" AUTOINCREMENT");
				}				
			}
			System.out.println();
			System.out.println("\t);");
			resColumnsTable.close();
			*/
		}
		tablesRS.close();

		System.out.println("=======================================");
	}

	protected void executeScriptFrom(InputStream is, Connection conn, boolean continueWithErrors)
			throws SQLException, IOException {

		BufferedReader brInput = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String sql = null;

		int updateCount;
		int numberOfColumns;
		boolean prinToConsole = true;

		prinToConsole = true;

		try {
			conn.setAutoCommit(true);
			brInput = new BufferedReader(new InputStreamReader(is));

			if (prinToConsole) {
				System.out.print("sql > ");
			}
			Statement sexec = conn.createStatement();

			String fullSql = "";
			while ((sql = brInput.readLine()) != null) {
				if (is != System.in) {
					if (prinToConsole) {
						System.out.println(sql);
					}
				}
				if (sql.trim().toLowerCase().equals("exit")) {
					break;
				} else if (sql.trim().toLowerCase().equals("!dbinfo")) {
					printDBInfo(conn);
					if (prinToConsole) {
						System.out.println("");
						System.out.print("sql > ");
					}
					continue;
				}
				if (sql.trim().length() == 0 || sql.startsWith("--")) {
					if (prinToConsole) {
						System.out.print("sql > ");
					}
					continue;
				} else if (sql.trim().endsWith(";")) {
					fullSql += " " + sql.trim();
					try {
						fullSql = fullSql.replaceAll(";$", "");
						boolean resultExecution = false;
						if (fullSql.toLowerCase().startsWith("call ")) {
							//fullSql = fullSql.replace("call ","");
							//System.err.println("==>>conn.prepareCall("+fullSql+")");
							CallableStatement callSt = conn.prepareCall(fullSql);

							resultExecution = callSt.execute();
							rs = resultExecution ? callSt.getResultSet() : null;
						} else {
							resultExecution = sexec.execute(fullSql);
							rs = resultExecution ? sexec.getResultSet() : null;
						}

						if (resultExecution && rs != null) {

							rsmd = rs.getMetaData();
							numberOfColumns = rsmd.getColumnCount();
							if (prinToConsole) {
								System.out.print("Resultset{\n");
							}
							if (prinToConsole) {
								System.out.print("\tClassResultSet{\n\t\t");
							}
							for (int j = 0; j < numberOfColumns; j++) {
								if (prinToConsole) {
									System.out.print((j > 0 ? ",'" : "'") + rsmd.getColumnClassName(j + 1) + "'");
								}
							}
							if (prinToConsole) {
								System.out.print("\n\t},\n");
							}

							if (prinToConsole) {
								System.out.print("\tHeaderLabels{\n\t\t");
							}
							for (int j = 0; j < numberOfColumns; j++) {
								if (prinToConsole) {
									System.out.print((j > 0 ? ",'" : "'") + rsmd.getColumnLabel(j + 1) + "'");
								}
							}
							if (prinToConsole) {
								System.out.print("\n\t},\n");
							}

							if (prinToConsole) {
								System.out.print("\tDataRows{");
							}
							int numRows;
							for (numRows = 0; rs.next(); numRows++) {
								if (numRows > 0) {
									if (prinToConsole) {
										System.out.print(",");
									}
								}
								if (prinToConsole) {
									System.out.print("\n\t\t{");
								}
								for (int j = 0; j < numberOfColumns; j++) {

									if (prinToConsole) {
										Object o = rs.getObject(j + 1);
										if (o == null) {
											System.out.print((j > 0 ? ", <NULL>" : "<NULL>"));
										} else if (o.getClass().equals(String.class)) {
											System.out.print((j > 0 ? "," : "") + "'" + rs.getString(j + 1) + "'");
										} else {
											System.out.print((j > 0 ? "," : "") + rs.getString(j + 1));
										}
									}
								}
								if (prinToConsole) {
									System.out.print(" }");
								}
							}
							rs.close();
							if (prinToConsole) {
								System.out.print("\n\t}.size()=" + numRows + "\n");
							}
							if (prinToConsole) {
								System.out.print("};\n");
							}
						} else {
							updateCount = sexec.getUpdateCount();
							if (prinToConsole) {
								System.out.print(updateCount + " rows affected\n");
							}
						}
					} catch (Exception exExec) {
						if (prinToConsole) {
							exExec.printStackTrace(System.err);
							//System.err.print("\t[x]:" + exExec.getMessage() + "\n");
						}
						if (!continueWithErrors) {
							break;
						}
					}
					fullSql = "";
					if (prinToConsole) {
						System.out.print("sql > ");
					}
				} else {
					fullSql += " " + sql.trim();
					if (prinToConsole) {
						System.out.print("    > ");
					}
				}
			}
			if (prinToConsole) {
				System.out.println("Script executed.");
			}
		} catch (SQLException ex) {
			throw ex;
		} catch (IOException ex2) {
			throw ex2;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception ex3) {
				ex3.printStackTrace(System.err);
			}
		}
	}

	public boolean existDB() throws IllegalStateException {
		Connection connectionForCreate = null;
		try {
			connectionForCreate = createConnectionForInit();
			return true;
		} catch (SQLException ex) {
			return false;
		} finally {
			if (connectionForCreate != null) {
				try {
					connectionForCreate.close();
				} catch (SQLException ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
	}
	
	public Connection getExistDB() throws IllegalStateException {
		Connection connectionForCreate = null;
		try {
			connectionForCreate = createConnectionForInit();			
		} catch (SQLException ex) {
			throw new IllegalStateException(ex.getMessage());
		} finally {
			return connectionForCreate;
		}
	}

	public void installDBfromScratch() {
		Connection connectionForCreate = null;
		try {
			connectionForCreate = createConnectionForCreate();
			String creationScriptResource = parameters4CreateAndExecute.getProperty(CREATION_SCRIPT);

			logger.info("installDBfromScratch:->executeScriptFrom:" + creationScriptResource);

			InputStream isCreationScript = null;

			if (creationScriptResource.startsWith("classpath:")) {
				isCreationScript = DBInstaller.class.getResourceAsStream(creationScriptResource.substring("classpath:".length()));
			} else if (creationScriptResource.startsWith("file:")) {
				isCreationScript = new FileInputStream(creationScriptResource.substring("file:".length()));
			} else {
				throw new IOException("For read properties, Pattern {\"file:\" | \"classpath:\"} not found in " + creationScriptResource);
			}
			executeScriptFrom(isCreationScript, connectionForCreate, false);

			String envScripts = parameters4CreateAndExecute.getProperty(INIT_ENVIRONMENT_SCRIPTS);
			logger.debug("installDBfromScratch:envScripts=" + envScripts);
			if (envScripts != null) {
				String[] scrips = envScripts.split(",");
				for (String sc : scrips) {
					if (sc.contains("/IMPORT_")) {
						String resourceToExtract = sc.substring(sc.indexOf("IMPORT_") + 7, sc.indexOf(".SQL")) + ".txt";
						String resourcePathToExtract = "/importSqlResources/" + resourceToExtract;

						logger.info("\t\tinstallDBfromScratch:->resourceToExtract=" + resourceToExtract + ", resourcePathToExtract=" + resourcePathToExtract);
						extractResource(resourceToExtract, resourcePathToExtract);
						logger.info("\t\tinstallDBfromScratch:extractResource OK !");
					}
					logger.debug("\tinstallDBfromScratch:->executeScriptFrom: " + sc);
					InputStream isInitEnvScript = null;
					if (sc.startsWith("classpath:")) {
						isInitEnvScript = DBInstaller.class.getResourceAsStream(sc.substring("classpath:".length()));
					} else if (sc.startsWith("file:")) {
						isInitEnvScript = new FileInputStream(sc.substring("file:".length()));
					} else {
						throw new IOException("For read properties, Pattern {\"file:\" | \"classpath:\"} not found in " + sc);
					}
					executeScriptFrom(isInitEnvScript, connectionForCreate, false);
				}
			}
		} catch (IOException ex2) {
			ex2.printStackTrace(System.err);
			//System.exit(4);
		} catch (SQLException ex1) {
			logger.error(ex1.getLocalizedMessage(), ex1);
			//System.exit(3);
		} finally {
			if (connectionForCreate != null) {
				try {
					connectionForCreate.close();
				} catch (SQLException ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
	}
	
	public Connection getInstallDBfromScratch() {
		Connection connectionForCreate = null;
		try {
			connectionForCreate = createConnectionForCreate();
			String creationScriptResource = parameters4CreateAndExecute.getProperty(CREATION_SCRIPT);

			logger.info("installDBfromScratch:->executeScriptFrom:" + creationScriptResource);

			InputStream isCreationScript = null;

			if (creationScriptResource.startsWith("classpath:")) {
				isCreationScript = DBInstaller.class.getResourceAsStream(creationScriptResource.substring("classpath:".length()));
			} else if (creationScriptResource.startsWith("file:")) {
				isCreationScript = new FileInputStream(creationScriptResource.substring("file:".length()));
			} else {
				throw new IOException("For read properties, Pattern {\"file:\" | \"classpath:\"} not found in " + creationScriptResource);
			}
			executeScriptFrom(isCreationScript, connectionForCreate, false);

			String envScripts = parameters4CreateAndExecute.getProperty(INIT_ENVIRONMENT_SCRIPTS);
			logger.debug("installDBfromScratch:envScripts=" + envScripts);
			if (envScripts != null) {
				String[] scrips = envScripts.split(",");
				for (String sc : scrips) {
					if (sc.contains("/IMPORT_")) {
						String resourceToExtract = sc.substring(sc.indexOf("IMPORT_") + 7, sc.indexOf(".SQL")) + ".txt";
						String resourcePathToExtract = "/importSqlResources/" + resourceToExtract;

						logger.info("\t\tinstallDBfromScratch:->resourceToExtract=" + resourceToExtract + ", resourcePathToExtract=" + resourcePathToExtract);
						extractResource(resourceToExtract, resourcePathToExtract);
						logger.info("\t\tinstallDBfromScratch:extractResource OK !");
					}
					logger.debug("\tinstallDBfromScratch:->executeScriptFrom: " + sc);
					InputStream isInitEnvScript = null;
					if (sc.startsWith("classpath:")) {
						isInitEnvScript = DBInstaller.class.getResourceAsStream(sc.substring("classpath:".length()));
					} else if (sc.startsWith("file:")) {
						isInitEnvScript = new FileInputStream(sc.substring("file:".length()));
					} else {
						throw new IOException("For read properties, Pattern {\"file:\" | \"classpath:\"} not found in " + sc);
					}
					executeScriptFrom(isInitEnvScript, connectionForCreate, false);
				}
			}
		} catch (IOException ex2) {
			ex2.printStackTrace(System.err);
			//System.exit(4);
		} catch (SQLException ex1) {
			logger.error(ex1.getLocalizedMessage(), ex1);
			//System.exit(3);
		} finally {
			return connectionForCreate;
		}
	}

	public void shellDB() {

		logger.debug("shellDB: --------------");

		//String urlConnection = parameters4CreateAndExecute.getProperty("jdbc.url_prefix") + parameters4CreateAndExecute.getProperty("db.name");
		//logger.debug("shellDB: urlConnection="+urlConnection);
		Connection connectionForInit = getExistDB();
		
		if (connectionForInit == null) {
			logger.debug("shellDB:The DB does'nt exist -> installDBfromScratch !");
			connectionForInit = getInstallDBfromScratch();
		}

		try {
			logger.debug("shellDB:OK, the DB exist !!");
			//connectionForInit = createConnectionForInit();
			logger.debug("shellDB:OK, connected.");
			logger.debug("shellDB:Ready, Now read from stdin.");
			executeScriptFrom(System.in, connectionForInit, true);
			logger.debug("-> EOF stdin, end");
		} catch (IOException ex) {
			logger.error("Something with the reading script:" + ex.getLocalizedMessage(), ex);
		} catch (IllegalStateException ex) {
			logger.error("Something with the Classpath and JDBC Driver:" + ex.getLocalizedMessage(), ex);
		} catch (SQLException ex) {
			logger.error("Something with the JDBC Connection:" + ex.getLocalizedMessage(), ex);
		} finally {
			try {
				if (connectionForInit != null) {
					connectionForInit.close();
				}
			} catch (SQLException ex1) {
				logger.error(ex1.getLocalizedMessage(), ex1);
			}
		}
	}

	private void extractResource(String resourceToExtract, String resourcePathToExtract) throws FileNotFoundException, IOException {
		InputStream is = getClass().getResourceAsStream(resourcePathToExtract);
		OutputStream os = new FileOutputStream(resourceToExtract);
		int r;
		byte[] buffer = new byte[1024 * 128];
		while ((r = is.read(buffer, 0, buffer.length)) != -1) {
			os.write(buffer, 0, r);
		}
		is.close();
		os.close();
	}
}
