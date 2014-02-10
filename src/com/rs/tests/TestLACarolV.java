package com.rs.tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


import org.apache.ws.commons.util.Base64.DecodingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.rs.session.AryLogAnalysis;
import com.rs.session.ArySLR;
import com.rs.utils.ConsoleWriter;
import com.rs.utils.DataStoreUtil;
import com.rs.utils.TestCaseSetUp;
import com.rs.utils.XmlParser;
import com.rs.utils.TestLinkUtil;

public class TestLACarolV extends ATestCases {

	public TestLACarolV() {

	}
	
@BeforeClass	
public static void setUpBeforeClass() throws FileNotFoundException   
	{
		_credentials.DSUser = DSUser;
		_credentials.DSPassword = DSPassword;
		_credentials.DBUser = DBUser;
		_credentials.DBPassword = DBPassword;

		PathToSQL = Paths.get(PathToSQL.toString(), "carolv"); 
		
		String [] xmlFiles = {
								"createCarolV.xml",
								"modCarolV1.xml",
								"modCarolV2.xml"
							 };
		
		TestCaseSetUp.dropDB(TargetDB);
		TestCaseSetUp.createDB(TargetDB);
		TestCaseSetUp.updateLogarchmeth1(TargetDB, LOGPATH);
		TestCaseSetUp.makeOfflineBackup(TargetDB, BACKUPPATH);
		
		DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);
		DataStoreUtil.dsConnection = DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
		
		TestCaseSetUp.runSQL(xmlFiles, withLob);
		DataStoreUtil.close(DataStoreUtil.dbConnection);

		TestCaseSetUp.makeOfflineBackup(TargetDB, BACKUPPATH);
		DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);

		
		xmlFiles = new String[2];
		
		xmlFiles[0] = "modCarolV3.xml";
		xmlFiles[1] = "modCarolV4.xml";
		TestCaseSetUp.runSQL(xmlFiles, withLob);
		
		slr.RunSLR(ARY_PATH, DB2InstanceName, TargetDB, DSName, _credentials, DataStoreUtil.isSlrExist(TargetDB) ? ArySLR.SLROperation.REBUILD : ArySLR.SLROperation.CREATE);
	}


@Test
public void LA_CarolV_RE_727() throws DecodingException, UnsupportedEncodingException, Exception{
	TEST_CASE_ID = 831626;
	TEST_CASE_EXTERNAL_ID = 727;
	
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		
	String tblFilter = _credentials.DBUser + ".CAROLV";
	String tbsFilter = "";

	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
	
	la.RunLogAnalysis(
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  params,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.CAPTURE,
					  AryLogAnalysis.Transactions.COMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
	
	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

	expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
	
    TEST_CASE_STEP_NUM = 1;
    
	sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
    expectedResultsList = XmlParser.getResultsStringList(xml);
    obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());
    
    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
    
    tblFilter = _credentials.DBUser + ".CAROLV";
	tbsFilter = "";
	
	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
    
    la.RunLogAnalysis(
			  DB2InstanceName,
			  TargetDB, 
			  DSName,
			  _credentials,
			  params,
			  AryLogAnalysis.Operation.INSERTS+
			  AryLogAnalysis.Operation.UPDATES+
			  AryLogAnalysis.Operation.DELETES, 
			  AryLogAnalysis.SQLDirection.UNDO,
			  AryLogAnalysis.Mode.SLR,
			  AryLogAnalysis.ReportType.FULL,
			  AryLogAnalysis.LobIgnore.CAPTURE,
			  AryLogAnalysis.Transactions.COMMITTED);
    
    TEST_CASE_STEP_NUM = 2;
    
	sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
    
	xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
    expectedResultsList = XmlParser.getResultsStringList(xml);
    obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}


@Test
public void LA_CarolV_RE_726() throws DecodingException, UnsupportedEncodingException, Exception{

	TEST_CASE_EXTERNAL_ID = 726;
	TEST_CASE_ID = 831623;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
    
	String tblFilter = _credentials.DBUser + ".CAROLV";
	String tbsFilter = "";
	
	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
	
	la.RunLogAnalysis(
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  params,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.IGNORE,
					  AryLogAnalysis.Transactions.COMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
	
	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    System.out.println(expectedResultsList.size());
    assertTrue(expectedResultsList.isEmpty());
}


@Test
public void LA_CarolV_RE_725() throws DecodingException, UnsupportedEncodingException, Exception{
	TEST_CASE_EXTERNAL_ID = 725;
	TEST_CASE_ID = 831620;
	TEST_CASE_STEP_NUM = 6;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = _credentials.DBUser + ".CAROLV";
	String tbsFilter = "";

	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
	
	la.RunLogAnalysis(
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  params,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.CAPTURE,
					  AryLogAnalysis.Transactions.COMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
	
	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}


@Test
public void LA_CarolV_RE_724() throws DecodingException, UnsupportedEncodingException, Exception{
	TEST_CASE_EXTERNAL_ID = 724;
	TEST_CASE_ID = 831617;
	TEST_CASE_STEP_NUM = 6;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = (TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName));
	xml = String.format(xml, "\""+DBUser+"\"");
    
	String tblFilter = _credentials.DBUser + ".CAROLV";
	String tbsFilter = "";

	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
	
	la.RunLogAnalysis(
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  params,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.IGNORE,
					  AryLogAnalysis.Transactions.COMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);

	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}

@Ignore
@Test
public void LA_CarolV_RE_211() throws DecodingException, UnsupportedEncodingException, Exception{
	TEST_CASE_EXTERNAL_ID = 211;
	TEST_CASE_ID = 831617;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = (TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName));
	xml = String.format(xml, "\""+DBUser+"\"");
    
	String tblFilter = "";
	String tbsFilter = "APPLE";

	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
	
	la.RunLogAnalysis(
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  params,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.IGNORE,
					  AryLogAnalysis.Transactions.COMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);

	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}

@Ignore
@Test
public void LA_CarolV_RE_1007() throws DecodingException, UnsupportedEncodingException, Exception{
	TEST_CASE_EXTERNAL_ID = 1007;
	TEST_CASE_ID = 832466;
	TEST_CASE_STEP_NUM = 2;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = (TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName));
	xml = String.format(xml, "\""+DBUser+"\"");
    
	String tblFilter = _credentials.DBUser + ".CAROLV";
	String tbsFilter = "";

	params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
	params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
	
	la.RunLogAnalysis(
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  params,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.NONE,
					  AryLogAnalysis.Mode.MRT,
					  AryLogAnalysis.ReportType.SUMMARY,
					  AryLogAnalysis.LobIgnore.CAPTURE,
					  AryLogAnalysis.Transactions.COMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);

	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}

}


	

