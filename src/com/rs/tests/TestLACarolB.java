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

public class TestLACarolB extends ATestCases {

	@BeforeClass	
	public static void setUpBeforeClass() throws FileNotFoundException   
		{
			_credentials.DSUser = DSUser;
			_credentials.DSPassword = DSPassword;
			_credentials.DBUser = DBUser;
			_credentials.DBPassword = DBPassword;

			PathToSQL = Paths.get(PathToSQL.toString(), "carolb"); 
			
			String [] xmlFiles = {
									"createCarolB.xml",
									"modCarolB.xml",
									"modCarolB2.xml"
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
	public void LA_CarolB_RE_899() throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 899;
		TEST_CASE_ID = 832142;
		TEST_CASE_STEP_NUM = 5;
		TEST_CASE_VERSION = 1;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = _credentials.DBUser+".CarolB";
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
