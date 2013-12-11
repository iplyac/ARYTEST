package com.rs.tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
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
import com.rs.utils.TestLinkUtil;
import com.rs.utils.XmlParser;

public class TestLAWildCard extends ATestCases {
	
	@BeforeClass	
	public static void setUpBeforeClass() throws FileNotFoundException   
		{
			_credentials.DSUser = DSUser;
			_credentials.DSPassword = DSPassword;
			_credentials.DBUser = DBUser;
			_credentials.DBPassword = DBPassword;

			PathToSQL = Paths.get(PathToSQL.toString(), "wildcard"); 
			
			withLob = false;
			
			String [] xmlFiles = {
									"createWCs.xml",
									"sqlV95.xml",
									"modWC3.xml",
									"modWC4.xml",
									"modWC4V8.xml",
									"modWC6.xml"
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
	
@AfterClass
public static void CompleteTest(){
	DataStoreUtil.close(DataStoreUtil.getDbConnection());
	DataStoreUtil.close(DataStoreUtil.getDsConnection());
}

@Ignore
@Test
public void LA_WildCard_RE_905(){
	
}


@Test
public void LA_WildCard_RE_689() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 689;
	TEST_CASE_ID = 831512;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
    
	String tblFilter = "";
	String tbsFilter = "ARYMPTESTTS ARYMPTESTXMLTS FAN GRAPE arympts";
	String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_690() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 690;
	TEST_CASE_ID = 831515;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "";
	String tbsFilter = "";
    String pgFilter = "MYPARTITIONGROUP";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_701() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 701;
	TEST_CASE_ID = 831548;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE '"+_credentials.DBUser+"'.'a%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_702() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 702;
	TEST_CASE_ID = 831551;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE '%'.'a%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_703() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 703;
	TEST_CASE_ID = 831554;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE '%'.'%M%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_704() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 704;
	TEST_CASE_ID = 831557;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE 'P%'.'a%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_715() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 715;
	TEST_CASE_ID = 831590;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE 'm%'.'a%' LIKE 'M%'.'A%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_716() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 716;
	TEST_CASE_ID = 831593;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE 'low%'.'%' LIKE 'dot%'.'%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
					  AryLogAnalysis.Operation.INSERTS+
					  AryLogAnalysis.Operation.UPDATES+
					  AryLogAnalysis.Operation.DELETES, 
					  AryLogAnalysis.SQLDirection.UNDO,
					  AryLogAnalysis.Mode.SLR,
					  AryLogAnalysis.ReportType.FULL,
					  AryLogAnalysis.LobIgnore.IGNORE);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
	
	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}


@Test
public void LA_WildCard_RE_718() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 718;
	TEST_CASE_ID = 831599;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE 'low%'.'%' LIKE 'dot%'.'%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_719() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 719;
	TEST_CASE_ID = 831602;
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE 'miX%'.'%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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

@Test
public void LA_WildCard_RE_902() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 902;
	TEST_CASE_ID = 832151;
	TEST_CASE_STEP_NUM = 6;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE 'low%'.'%' LIKE 'dot%'.'%'";
	String tbsFilter = "";
    String pgFilter = "";
	
	la.RunLogAnalysis(ARY_PATH,
					  DB2InstanceName,
					  TargetDB, 
					  DSName,
					  _credentials,
					  tblFilter,
					  tbsFilter,
					  pgFilter,
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
