package com.rs.tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;


import org.apache.ws.commons.util.Base64.DecodingException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rs.session.AryLogAnalysis;
import com.rs.session.ArySLR;
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
								"modCarolV2.xml",
								"modCarolV3.xml",
								"modCarolV4.xml"
							 };
		
		TestCaseSetUp.dropDB(TargetDB);
		TestCaseSetUp.createDB(TargetDB);
		TestCaseSetUp.updateLogarchmeth1(TargetDB, LOGPATH);
		TestCaseSetUp.makeOfflineBackup(TargetDB, BACKUPPATH);
		
		DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);
		DataStoreUtil.dsConnection = DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
		
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
		
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  _credentials.DBUser + ".CAROLV", 
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.CAPTURE);
	
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
    
    la.RunLogAnalysis(ARY_PATH,
			  DB2InstanceName,
			  TargetDB, 
			  DSName,
			  _credentials,
			  _credentials.DBUser + ".CAROLV", 
			  AryLogAnalysis.Operation.INSERTS+
			  AryLogAnalysis.Operation.UPDATES+
			  AryLogAnalysis.Operation.DELETES, 
			  AryLogAnalysis.SQLDirection.UNDO,
			  AryLogAnalysis.Mode.SLR,
			  AryLogAnalysis.ReportType.FULL,
			  AryLogAnalysis.LobIgnore.CAPTURE);
    
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
    
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  _credentials.DBUser + ".CAROLV", 
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.IGNORE);
	
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
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	
	xml = String.format(xml, "\""+DBUser+"\"");
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  _credentials.DBUser + ".CAROLV", 
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.CAPTURE);
	
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
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = (TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName));
	xml = String.format(xml, "\""+DBUser+"\"");
    
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  _credentials.DBUser + ".CAROLV", 
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.REDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.IGNORE);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);

	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}

}


	

