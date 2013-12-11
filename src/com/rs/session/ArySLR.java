package com.rs.session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.rs.tests.ATestCases;
import com.rs.utils.AryCredentials;
import com.rs.utils.DataStoreUtil;

public class ArySLR extends ArySession {

	public static class SLROperation {
		public final static String CREATE = "create";
		public final static String REBUILD = "rebuild";
		public final static String DROP = "drop";
	}
	
	private String logLoc;
	private String backupLoc;
	
	public ArySLR(){
		BINARY_NAME = "aryslr";
		this.backupLoc = "";
		this.logLoc = "";
	}
	
	private static final String OPT_NODE = " -O";
    
	/**
     * Get SQL command for starts <b>aryrun</b>.
     */
    public String getRunAgentCommand()
    {
        return ("CALL SYSTOOLS.ARY_RUN_SLR (?, ?, ?, ?, ?, ?, ?)");
    }
	
	protected String setBackupTimestamp(String BackupTimestamp) {
		return " -b " + BackupTimestamp;
	}
	
	protected String setSLROperation(String SLROperation) {
		return " -o \"" + SLROperation + "\"";
	}
	
public String getbackupTimestamp(String TargetDB) {
		String inputLine = "";
		try {
			Process p;
			p = Runtime.getRuntime().exec(
					"db2 list history backup all for " + TargetDB);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			p.getOutputStream().close();

			while (((inputLine = reader.readLine()) != null)
					^ (inputLine.toLowerCase().indexOf("start time") >= 0));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputLine.substring(inputLine.toLowerCase().indexOf("start time") + 12,
								   inputLine.toLowerCase().indexOf("start time") + 26);
	}
	
	
	public String makeSLRCommandLine(String ARY_PATH, String DB2InstanceName,
			String TargetDB, String DSName, AryCredentials _credentials,
			String BackupTimestamp, String setSLROperation) {
		
		opts = new ArrayList<AryOpt>();

        this.addAryOption(ArySLR.OPT_DB_LOGLOC, logLoc);
        this.addAryOption(ArySLR.OPT_DB_BACKUPLOC, backupLoc);
		
		boolean resolveCtimeConflists;
        if (setSLROperation.equals("rebuild") || setSLROperation.equals("drop"))
        {
            resolveCtimeConflists = true;
        }
        else
        {
            resolveCtimeConflists = false;
        }
		   	
				   this.SessionID = DataStoreUtil.startAgentSession(DataStoreUtil.getDbCreateTime(),DataStoreUtil.getCatalogNodeNum(),resolveCtimeConflists,ATestCases.DSUser);
		   
//				   		getPathToAryRun(ARY_PATH)
			return   	setDB2Instance(DB2InstanceName)
				   		+ " -64 "
				   		+ setBinaryName()
				   		+ OPT_NODE + " A "
				   		+ setDatastore(DSName)
				   		+ setCredentials(_credentials)
				   		+ setSessionID(SessionID)
				   		+ setTargetDB(TargetDB)
				   		+ setBackupTimestamp(BackupTimestamp)
				   		+ setSLROperation(setSLROperation)
				   		;
}
	
	public void RunSLR(String ARY_PATH, String DB2InstanceName,
			String TargetDB, String DSName, AryCredentials _credentials,
			String SLROperation) {
		
		runprocess(makeSLRCommandLine(ARY_PATH,
								 DB2InstanceName, 
								 TargetDB, 
								 DSName,
								 _credentials, 
								 getbackupTimestamp(TargetDB),
								 SLROperation));
	}
}
