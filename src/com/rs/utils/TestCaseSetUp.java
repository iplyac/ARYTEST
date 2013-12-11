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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rs.tests.ATestCases;

public class TestCaseSetUp {

	public static void createDB(String TargetDB){
			System.out.println("create database "+ TargetDB +"...");
			runCMD("db2 create db " + TargetDB);
	}
	
	public static void dropDB(String TargetDB){
			System.out.println("drop database "+ TargetDB +"...");
			runCMD("db2 drop db " + TargetDB);
	}
	
	public static void updateLogarchmeth1(String TargetDB,String LOGARCHMETH1){
			System.out.println("update database config...");
			runCMD("db2 update db cfg for "+TargetDB+" using logarchmeth1 disk:" + LOGARCHMETH1);
	}

	public static void makeOfflineBackup(String TargetDB,String BACKUP_PATH){
			System.out.println("backup database...");
			runCMD("db2 backup db "+TargetDB+" on all dbpartitionnums to "+BACKUP_PATH);
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

			PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out,System.getProperty("file.encoding"))); 
			String inputLine;
			
			while ((inputLine = reader.readLine()) != null) {
				out.println(inputLine);
				out.flush();
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
		File LobFile = null;
		InputStream fis = null;
		
		if (withLobs) {
			LobFile = new File(ATestCases.PathToSQL.toString()	+ File.separator + "lob" + File.separator + "lob.file");
			fis = new BufferedInputStream(new FileInputStream(LobFile));
			fis.mark((int) LobFile.length() << 1);
		}
		for (String xmlFile:xmlFiles){
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(ATestCases.PathToSQL+ File.separator + xmlFile);
				NodeList StatementList = doc.getElementsByTagName("statement");
				
				for (int stmt_id = 0; stmt_id < StatementList.getLength(); stmt_id++) {
				    statement = (Element)StatementList.item(stmt_id);
					ConsoleWriter.println(statement.getAttribute("stmt_id") +" "+ statement.getTextContent());
					try {
						pStmt = DataStoreUtil.dbConnection.prepareStatement(statement.getTextContent());
						if ( withLobs && ( statement.getTextContent().indexOf(" ? ")>0 )) 
							{
								pStmt.setBinaryStream(1, fis, (int) LobFile.length());
								fis.reset();
							}
						pStmt.executeUpdate();
						pStmt.close();
					} 
					catch (SQLException ex) {
						System.err.println("SQLException information");
					      while(ex!=null) {
					        System.err.println ("Error msg: "  + ex.getMessage());
					        System.err.println ("SQLSTATE: "   + ex.getSQLState());
					        System.err.println ("Error code: " + ex.getErrorCode());
					        ex.printStackTrace();
					        ex = ex.getNextException(); 
								}
						}
				}
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
}
