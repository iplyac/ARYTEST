package com.rs.tests;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

import org.apache.ws.commons.util.Base64.DecodingException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.rs.session.AryLogAnalysis;
import com.rs.session.ArySLR;
import com.rs.utils.DataStoreUtil;
import com.rs.utils.TestCaseSetUp;

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
		
		String [] xmlFiles = {"index_of_expression.xml"};
		
		TestCaseSetUp.dropDB(TargetDB);
		TestCaseSetUp.createDB(TargetDB);
		TestCaseSetUp.updateLogarchmeth1(TargetDB, LOGPATH);
		TestCaseSetUp.makeOfflineBackup(TargetDB, BACKUPPATH);
		
		DataStoreUtil.dbConnection = DataStoreUtil.connect(HostName, DB2InstancePort, TargetDB, DBUser, DBPassword);
		DataStoreUtil.dsConnection = DataStoreUtil.connect(HostName, DB2InstancePort, DSName, DSUser, DSPassword);
		
		TestCaseSetUp.runSQL(xmlFiles, withLob);

		slr.RunSLR(ARY_PATH, DB2InstanceName, TargetDB, DSName, _credentials, DataStoreUtil.isSlrExist(TargetDB) ? ArySLR.SLROperation.REBUILD : ArySLR.SLROperation.CREATE);

		}
	@Ignore
	@Test
	public void LA_Kepler_RE_184()throws DecodingException, UnsupportedEncodingException, Exception{
		
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
	}
	
}
