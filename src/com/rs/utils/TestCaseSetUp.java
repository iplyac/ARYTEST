package com.rs.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rs.tests.ATestCases;

public class TestCaseSetUp {

	public static void createDB(String TargetDB){
			ConsoleWriter.println("create database "+ TargetDB +"...");
			runCMD("db2 create db " + TargetDB);
	}
	
	public static void updateLogarchcompr1(String TargetDB){
		ConsoleWriter.println("enable log compression "+ TargetDB +"...");
		runCMD("db2 update db cfg for "+TargetDB+" using logarchcompr1 ON");
}
	
	public static void dropDB(String TargetDB){
			ConsoleWriter.println("drop database "+ TargetDB +"...");
			runCMD("db2 drop db " + TargetDB);
	}

	public static void init(){
		ConsoleWriter.println("Init...");
		runCMD("db2cmd -i -w db2clpsetcp");
}
	
	public static void updateLogarchmeth1(String TargetDB,String LOGARCHMETH1){
			ConsoleWriter.println("update database config...");
			runCMD("db2 update db cfg for "+TargetDB+" using logarchmeth1 disk:" + LOGARCHMETH1);
	}

	public static void updateTableOrganization(String TargetDB,String DFT_TABLE_ORG){
		ConsoleWriter.println("update database config...");
		runCMD("db2 update db cfg for "+TargetDB+" USING DFT_TABLE_ORG " + DFT_TABLE_ORG);
}
	
	public static void makeOfflineBackup(String TargetDB,String BACKUP_PATH){
			ConsoleWriter.println("backup database...");
			try {
				Thread.sleep(5000);
				runCMD("db2 backup db "+TargetDB+" on all dbpartitionnums to "+BACKUP_PATH);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				ConsoleWriter.println("An error occurred during backup or sleep operation...");
				e.printStackTrace();
			}
			
	}
	
	public static void attachtoInstance(String DB2InstanceName, String DSUser, String DSPassword){
		System.out.println("attach to instance...");
		runCMD("db2 attach to "+DB2InstanceName+" user "+DSUser+" using "+DSPassword);
		
	} 
	
	private static void runCMD(String cmd){
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream(),"UTF-8"));

			String inputLine;
			
			while ((inputLine = reader.readLine()) != null) {
				ConsoleWriter.println(inputLine);
				}
			p.waitFor();
//			Assert.assertEquals(0, p.exitValue());
			reader.close();
			p.getOutputStream().close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void runSQL(String [] xmlFiles, boolean withLobs) throws FileNotFoundException{
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Element statement;
		PreparedStatement pStmt = null;
		CallableStatement cStmt = null;
		File LobFile = null;
		InputStream fis = null;
		
		if (withLobs) {
			LobFile = new File(ATestCases.PathToSQL.toString()	+ File.separator + "lob" + File.separator + "lob.file");
			fis = new BufferedInputStream(new FileInputStream(LobFile));
			fis.mark((int) LobFile.length() << 1);
		}
/*go through all xml files*/
		for (String xmlFile:xmlFiles){
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(ATestCases.PathToSQL+ File.separator + xmlFile);
				NodeList StatementList = doc.getElementsByTagName("statement");
				
				NodeList TestCases = doc.getElementsByTagName("testcasesetup");
				Element testcase = (Element)TestCases.item(0);
				
				try{
				if (testcase.getAttribute("autocommit").equals("no"))DataStoreUtil.getDbConnection().setAutoCommit(false); else DataStoreUtil.getDbConnection().setAutoCommit(true); 
				}catch (SQLException ex) {
					System.err.println("error on set autocommit value");
					}
				
				/*go through all statements in current file*/	
				for (int stmt_id = 0; stmt_id < StatementList.getLength(); stmt_id++) {
					
				    statement = (Element)StatementList.item(stmt_id);
				    ConsoleWriter.println(xmlFile+" : "+statement.getAttribute("stmt_id") +" "+ statement.getTextContent());
					
					try {
						if (statement.getAttribute("call").equals("yes"))
						{
							cStmt = DataStoreUtil.dbConnection.prepareCall(statement.getTextContent());
							cStmt.execute();
							cStmt.close();
						}	
						else{
						pStmt = DataStoreUtil.dbConnection.prepareStatement(statement.getTextContent());
						if ( withLobs && ( statement.getTextContent().indexOf(" ? ")>0 )) 
							{
								pStmt.setBinaryStream(1, fis, (int) LobFile.length());
								fis.reset();
							}
						pStmt.executeUpdate();
						pStmt.close();
						}
					} 
					catch (SQLException ex) {
						System.err.println("\n\nSQLException information for statement "+ statement.getAttribute("stmt_id"));
					      while(ex!=null) {
					        System.err.println ("Error msg: "  + ex.getMessage());
					        System.err.println ("SQLSTATE: "   + ex.getSQLState());
					        System.err.println ("Error code: " + ex.getErrorCode());
					        ex = ex.getNextException(); 
								}
						}finally{
							SqlUtil.closeStatement(pStmt);
						}
				}
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
}
