package com.rs.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.ws.commons.util.Base64.DecodingException;
import org.junit.Before;
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

public class TestLACommon extends ATestCases {

	@BeforeClass	
	public static void setUpBeforeClass() throws FileNotFoundException   
		{
			_credentials.DSUser = DSUser;
			_credentials.DSPassword = DSPassword;
			_credentials.DBUser = DBUser;
			_credentials.DBPassword = DBPassword;

			PathToSQL = Paths.get(PathToSQL.toString(), "common"); 
			
			withLob = false;
			
			String [] xmlFiles = {"ary5897_1.xml"};
			
			TestCaseSetUp.dropDB(TargetDB);
			TestCaseSetUp.createDB(TargetDB);
			TestCaseSetUp.updateLogarchmeth1(TargetDB, LOGPATH);
			TestCaseSetUp.makeOfflineBackup(TargetDB, BACKUPPATH);
			
			DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);
			DataStoreUtil.dsConnection = DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
			
			TestCaseSetUp.runSQL(xmlFiles, withLob);
			DataStoreUtil.close(DataStoreUtil.dbConnection);
			
			DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);
			
			xmlFiles = new String[21];
			
			xmlFiles[0] = "createX.xml";
			xmlFiles[1] = "modX2.xml";
			xmlFiles[2] = "modX4.xml";
			xmlFiles[3] = "modX4LA.xml";
			xmlFiles[4] = "modX6LA.xml";
			xmlFiles[5] = "createTS.xml";
			xmlFiles[6] = "createTables1.xml";
			xmlFiles[7] = "createTables2.xml";
			xmlFiles[8] = "createTables3.xml";
			xmlFiles[9] = "createTables5.xml";
			xmlFiles[10] = "alter.xml";
			xmlFiles[11] = "insert1.xml";
			xmlFiles[12] = "insert2.xml";
			xmlFiles[13] = "insert31.xml";
			xmlFiles[14] = "insert51.xml";
			xmlFiles[15] = "random_index.xml";
			xmlFiles[16] = "hash.xml";
			xmlFiles[17] = "ary5897_2.xml";
			xmlFiles[18] = "re-1024.xml";
			xmlFiles[19] = "re-992.xml";
			xmlFiles[20] = "re-225.xml";
			
			TestCaseSetUp.runSQL(xmlFiles, withLob);

			slr.RunSLR(ARY_PATH, DB2InstanceName, TargetDB, DSName, _credentials, DataStoreUtil.isSlrExist(TargetDB) ? ArySLR.SLROperation.REBUILD : ArySLR.SLROperation.CREATE);
		
			
		}
	

	@Test
	public void LA_Common_RE_907()throws DecodingException, UnsupportedEncodingException, Exception{
		
		TEST_CASE_EXTERNAL_ID = 907;
		TEST_CASE_ID = 832166;
		TEST_CASE_STEP_NUM = 7;
		TEST_CASE_VERSION = 1;
		
		TestLinkUtil.connect( testlinkURL, testlinkKey);
		
		String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		xml = String.format(xml, "\""+DBUser+"\"");
		
		String tblFilter = "'DADLANI9'.";
		String tbsFilter = "";

		params.put(AryLogAnalysis.OPT_TABLES, tblFilter);
		params.put(AryLogAnalysis.OPT_TBSPACES, tbsFilter);
		
		la.RunLogAnalysis(
						  DB2InstanceName,
						  TargetDB, 
						  DSName,
						  _credentials,
						  params,
						  AryLogAnalysis.Operation.UPDATES, 
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
	


@Test
public void LA_Common_RE_170()throws DecodingException, UnsupportedEncodingException, Exception{

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
public void LA_Common_RE_192()throws DecodingException, UnsupportedEncodingException, Exception{
	
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


@Test
public void LA_Common_RE_1002()throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 1002;
	TEST_CASE_ID = 832451;
	TEST_CASE_STEP_NUM = 2;
	TEST_CASE_VERSION = 2;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = _credentials.DBUser+".MYDATA";
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


@Test
public void LA_Common_RE_1020()throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 1020;
	TEST_CASE_ID = 832505;
	TEST_CASE_STEP_NUM = 3;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = _credentials.DBUser+".TAB1";
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
    
    TEST_CASE_STEP_NUM = 3;
    
	sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");

    expectedResultsList = XmlParser.getResultsStringList(xml);
    obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
}

@Ignore
@Test
public void LA_Common_RE_1024()throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 1024;
	TEST_CASE_ID = 832517;
	TEST_CASE_STEP_NUM = 3;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "'DB2ADMN'.";
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
					  AryLogAnalysis.Transactions.COMMITTED+
					  AryLogAnalysis.Transactions.ROLLEDBACK+
					  AryLogAnalysis.Transactions.UNCOMMITTED);
	
	DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
	
	List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
	List <String> obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());
    
    /*TEST_CASE_STEP_NUM = 4;
    
	sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");

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
			  AryLogAnalysis.Mode.MRT,
			  AryLogAnalysis.ReportType.FULL,
			  AryLogAnalysis.LobIgnore.CAPTURE,
			  AryLogAnalysis.Transactions.COMMITTED+
			  AryLogAnalysis.Transactions.ROLLEDBACK+
			  AryLogAnalysis.Transactions.UNCOMMITTED);
	
    expectedResultsList = XmlParser.getResultsStringList(xml);
    obtainedResultsList = DataStoreUtil.getObtainedResultsList(sqlKey, la.getSessionID());

    expectedResultsList.removeAll(obtainedResultsList);
    assertTrue(expectedResultsList.isEmpty());*/
}

@Test
public void LA_Common_RE_992()throws DecodingException, UnsupportedEncodingException, Exception{
	
	TEST_CASE_EXTERNAL_ID = 992;
	TEST_CASE_ID = 832421;
	TEST_CASE_STEP_NUM = 5;
	TEST_CASE_VERSION = 1;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = "LIKE '____________________%'.";
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
public void LA_Common_RE_225()throws DecodingException, UnsupportedEncodingException, Exception{

	TEST_CASE_EXTERNAL_ID = 225;
	TEST_CASE_ID = 825982;
	TEST_CASE_STEP_NUM = 4;
	TEST_CASE_VERSION = 3;
	
	TestLinkUtil.connect( testlinkURL, testlinkKey);
	
	String sqlKey   = TestLinkUtil.getSqlKeyFromActions("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
	
	String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
	xml = String.format(xml, "\""+DBUser+"\"");
	
	String tblFilter = _credentials.DBUser+".U_table";
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

}
