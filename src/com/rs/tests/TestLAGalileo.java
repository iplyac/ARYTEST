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
import com.rs.utils.DataStoreUtil;
import com.rs.utils.TestCaseSetUp;
import com.rs.utils.TestLinkUtil;
import com.rs.utils.XmlParser;

public class TestLAGalileo extends ATestCases {
	
	@BeforeClass	
	public static void setUpBeforeClass() throws FileNotFoundException   
		{
		_credentials.DSUser = DSUser;
		_credentials.DSPassword = DSPassword;
		_credentials.DBUser = DBUser;
		_credentials.DBPassword = DBPassword;

		PathToSQL = Paths.get(PathToSQL.toString(), "galileo"); 
		
		withLob = false;
		
		String [] xmlFiles = {"RE-201.xml"};
		
		TestCaseSetUp.dropDB(TargetDB);
		TestCaseSetUp.createDB(TargetDB);
		TestCaseSetUp.updateLogarchcompr1(TargetDB);
		TestCaseSetUp.updateLogarchmeth1(TargetDB, LOGPATH);
		TestCaseSetUp.makeOfflineBackup(TargetDB, BACKUPPATH);
		
		DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);
		DataStoreUtil.dsConnection = DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
		
		TestCaseSetUp.runSQL(xmlFiles, withLob);

		slr.RunSLR(ARY_PATH, DB2InstanceName, TargetDB, DSName, _credentials, DataStoreUtil.isSlrExist(TargetDB) ? ArySLR.SLROperation.REBUILD : ArySLR.SLROperation.CREATE);
		}
	
	@Ignore
	@Test
	public void LA_Galileo_RE_201()throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 201;
		TEST_CASE_ID = 825910;
		TEST_CASE_STEP_NUM = 12;
		TEST_CASE_VERSION = 3;
		
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
						  AryLogAnalysis.LobIgnore.CAPTURE,
						  AryLogAnalysis.Transactions.COMMITTED);
		
		List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
		List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

	    expectedResultsList.removeAll(obtainedResultsList);
	    assertTrue(expectedResultsList.isEmpty());
	}
	
	@Ignore
	@Test
	public void LA_Galileo_RE_203()throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 203;
		TEST_CASE_ID = 825916;
		TEST_CASE_STEP_NUM = 4;
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
						  AryLogAnalysis.LobIgnore.CAPTURE,
						  AryLogAnalysis.Transactions.COMMITTED);
		
		List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
		List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

	    expectedResultsList.removeAll(obtainedResultsList);
	    assertTrue(expectedResultsList.isEmpty());
	}
	
	@Ignore
	@Test
	public void LA_Galileo_RE_204()throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 204;
		TEST_CASE_ID = 825919;
		TEST_CASE_STEP_NUM = 4;
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
						  AryLogAnalysis.SQLDirection.UNDO,
						  AryLogAnalysis.Mode.SLR,
						  AryLogAnalysis.ReportType.FULL,
						  AryLogAnalysis.LobIgnore.CAPTURE,
						  AryLogAnalysis.Transactions.COMMITTED);
		
		List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
		List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

	    expectedResultsList.removeAll(obtainedResultsList);
	    assertTrue(expectedResultsList.isEmpty());
	}
}
