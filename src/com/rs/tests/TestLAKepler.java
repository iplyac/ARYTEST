package com.rs.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.ws.commons.util.Base64.DecodingException;
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

public class TestLAKepler extends ATestCases {

	@BeforeClass	
	public static void setUpBeforeClass() throws FileNotFoundException   
		{
		_credentials.DSUser = DSUser;
		_credentials.DSPassword = DSPassword;
		_credentials.DBUser = DBUser;
		_credentials.DBPassword = DBPassword;

		PathToSQL = Paths.get(PathToSQL.toString(), "kepler"); 
		
		withLob = false;
		
		String [] xmlFiles = {"re-192.xml","index_on_expression.xml",
							  "createTS.xml",
							  "createTables1.xml",
							  "createTables2.xml",
							  "createTables3.xml",
							  "createTables5.xml",
							  "alter.xml",
							  "insert1.xml",
							  "insert2.xml",
							  "insert31.xml",
							  "insert51.xml"
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
	public void LA_Kepler_RE_184()throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 184;
		TEST_CASE_ID = 825859;
		TEST_CASE_STEP_NUM = 3;
		TEST_CASE_VERSION = 2;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = _credentials.DBUser+".EMP "+_credentials.DBUser+".EMPLOYEE";
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
	public void LA_Kepler_RE_170()throws DecodingException, UnsupportedEncodingException, Exception{
	
		TEST_CASE_EXTERNAL_ID = 170;
		TEST_CASE_ID = 825817;
		TEST_CASE_STEP_NUM = 4;
		TEST_CASE_VERSION = 3;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = "LIKE '"+_credentials.DBUser+"'.'T%'";
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
	public void LA_Kepler_RE_173()throws DecodingException, UnsupportedEncodingException, Exception{
	
		TEST_CASE_EXTERNAL_ID = 173;
		TEST_CASE_ID = 825826;
		TEST_CASE_STEP_NUM = 3;
		TEST_CASE_VERSION = 3;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = "LIKE '"+_credentials.DBUser+"'.'T%'";
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
						  AryLogAnalysis.SQLDirection.UNDO,
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
	public void LA_Kepler_RE_186()throws DecodingException, UnsupportedEncodingException, Exception{
	
		TEST_CASE_EXTERNAL_ID = 186;
		TEST_CASE_ID = 825865;
		TEST_CASE_STEP_NUM = 3;
		TEST_CASE_VERSION = 3;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = _credentials.DBUser+".EMP "+_credentials.DBUser+".EMPLOYEE";
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
	public void LA_Kepler_RE_192()throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 192;
		TEST_CASE_ID = 825883;
		TEST_CASE_STEP_NUM = 5;
		TEST_CASE_VERSION = 2;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = _credentials.DBUser+".T1";
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
}