package com.rs.tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.apache.ws.commons.util.Base64.DecodingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.rs.session.AryLogAnalysis;
import com.rs.session.ArySLR;
import com.rs.utils.DataStoreUtil;
import com.rs.utils.TestCaseSetUp;
import com.rs.utils.TestLinkUtil;
import com.rs.utils.XmlParser;

public class TestLAPart_ForKey extends ATestCases {

	@BeforeClass	
	public static void setUpBeforeClass() throws FileNotFoundException   
		{
			_credentials.DSUser = DSUser;
			_credentials.DSPassword = DSPassword;
			_credentials.DBUser = DBUser;
			_credentials.DBPassword = DBPassword;

			PathToSQL = Paths.get(PathToSQL.toString(), "part_forkey");
			
			String [] xmlFiles = {
									"setPart_ForKey.xml",
									"modTab1.xml",
									"modTab2.xml",
									"modTab3.xml",
									"mod2Tab1.xml"
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
public void LA_Part_ForKey_RE_1023() throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_ID = 832514;
	TEST_CASE_EXTERNAL_ID = 1023;
	
	TEST_CASE_STEP_NUM = 0;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	
	String tblFilter = "ARYMP01.'Hamster'";
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
public void LA_Part_ForKey_RE_32() throws DecodingException,UnsupportedEncodingException, Exception {

		TEST_CASE_ID = 825248;
		TEST_CASE_EXTERNAL_ID = 32;

		TEST_CASE_STEP_NUM = 4;
		TEST_CASE_VERSION = 1;

		TestLinkUtil.connect(testlinkURL, testlinkKey);

		String sqlKey = TestLinkUtil.getSqlKeyFromActions("RE-" + TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-" + TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);

		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);

		String tblFilter = "ARYMP01.";
		String tbsFilter = "";

		params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
		params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);

		la.RunLogAnalysis(DB2InstanceName, TargetDB, DSName, _credentials,
				params, AryLogAnalysis.Operation.INSERTS
						+ AryLogAnalysis.Operation.UPDATES
						+ AryLogAnalysis.Operation.DELETES,
				AryLogAnalysis.SQLDirection.REDO, AryLogAnalysis.Mode.SLR,
				AryLogAnalysis.ReportType.FULL,
				AryLogAnalysis.LobIgnore.CAPTURE,
				AryLogAnalysis.Transactions.COMMITTED);

		DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);

		List<String> expectedResultsList = XmlParser.getResultsStringList(xml);
		List<String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

		expectedResultsList.removeAll(obtainedResultsList);
		assertTrue(expectedResultsList.isEmpty());
	}

}
