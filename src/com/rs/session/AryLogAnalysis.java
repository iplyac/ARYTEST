package com.rs.session;


import java.util.ArrayList;
import java.util.Map;

import com.rs.tests.ATestCases;
import com.rs.utils.AryCredentials;
import com.rs.utils.DataStoreUtil;


public class AryLogAnalysis extends ArySession{

	public static class SQLDirection {
		public  final static String NONE = "none";
		public  final static String REDO = "redosql";
		public  final static String UNDO = "undosql";
	}
	
	public static class Mode {
		public final static String MRT = " -M M";
		public final static String SLR = "";
	}

	public static class Operation {
		public final static String INSERTS = "i";
		public final static String UPDATES = "u";
		public final static String DELETES = "d";
	}

	public static class Transactions {
		public final static String COMMITTED = "c";
		public final static String PARTIAL = "p";
		public final static String UNCOMMITTED = "u";
		public final static String ROLLEDBACK = "r";
	}
	
	public static class ReportType{
		public final static String SUMMARY = "sum";
		public final static String FULL = "full";
	}
	
	public static class LobIgnore{
		public final static String IGNORE = " -g ";
		public final static String CAPTURE = "";
	}
	
	protected String LastSessionID;
	protected String generalCommandString = "";
	
	private static final String OPT_NODE = " -O";
	
	public static final String OPT_TABLES = "tablelist";
	public static final String OPT_TBSPACES = "tablespace";
	public static final String OPT_PGROUPS = "partitiongroup";
	public static final String OPT_APPNAME = "appname";
	public static final String OPT_AUTHLIST = "authlist";
	public static final String OPT_APPID = "appid";
	
	public static final String PERMANENT_TABLE_FILTER = " !'ARY'.";
	public static final String PERMANENT_TBS_FILTER = " NOT LIKE 'ARY%' NOT LIKE 'SYS%'";
	
	
	private String tblFilter;
	private String pgFilter;
	private String logLoc;
	private String backupLoc;
	private String appID;
	private String appName;
	private String tbsFilter;
	private String authList;
	
	public AryLogAnalysis(){
		this.BINARY_NAME = "aryla";
		this.appID = "";
		this.appName = "";
		this.authList = "";
		this.backupLoc = "";
		this.logLoc = "";
		this.pgFilter = "";
		this.tblFilter = "";
		this.tbsFilter = "";
		this.SessionID = 0;
	}
	
	protected String setSQLDirection(String SQLDirection) {
		if (SQLDirection!=AryLogAnalysis.SQLDirection.NONE)
			return " -q \"" + SQLDirection + "\"";
		else return "";
	}

	protected String setOperation(String Operations) {
		return " -a " + Operations;
	}

	protected String setTransactions(String Transactions) {
		return " -z " + Transactions;
	}
	
	protected String setObjectsSet(String ObjectsSet) {
		return " -l \"" + ObjectsSet + "\"";
		
	}

	protected String setLobIgnore(String LobIgnore) {
		return LobIgnore;
	}

	
	protected String setLAMode(String LogAnalysisMode) {
		return LogAnalysisMode;
	}
	

	protected String setLogAnalysisReportType(String LogAnalysisReportType) {
		return " -r "+LogAnalysisReportType;
	}

    protected String getRunAgentCommand()
    {
        return ("CALL SYSTOOLS.ARY_RUN_LA (?, ?, ?, ?, ?, ?, ?)");
    }
	
	/**
	 * Make command string for execution
	 * @param ARY_PATH
	 * @param DB2InstanceName
	 * @param TargetDB
	 * @param DSName
	 * @param _credentials
	 * @param TableList
	 * @param Operations
	 * @param SQLDirection
	 * @param LAMode
	 * @param LAReportType
	 * @param LobIgnore
	 * @return
	 */
	
	public String makeLogAnalysisCommand(
			String DB2InstanceName, String TargetDB, String DSName,
			AryCredentials _credentials, Map<String, String> params,
			String Operations, String SQLDirection, String LAMode,
			String LAReportType, String LobIgnore, String Transactions) {
		
		opts = new ArrayList<AryOpt>();
		
        this.addAryOption(OPT_PGROUPS, params.get(OPT_PGROUPS));
        this.addAryOption(OPT_TABLES, params.get(OPT_TABLES) + PERMANENT_TABLE_FILTER);
        this.addAryOption(OPT_TBSPACES, params.get(OPT_TBSPACES) + PERMANENT_TBS_FILTER);
        this.addAryOption(OPT_DB_LOGLOC, params.get(OPT_DB_LOGLOC));
        this.addAryOption(OPT_DB_BACKUPLOC, params.get(OPT_DB_BACKUPLOC));
        this.addAryOption(OPT_APPID, params.get(OPT_APPID));
        this.addAryOption(OPT_APPNAME, params.get(OPT_APPNAME));
        this.addAryOption(OPT_AUTHLIST, params.get(OPT_AUTHLIST));
        
        boolean isMRT = (LAMode.equals(Mode.MRT)) ? true : false;

        this.SessionID = DataStoreUtil.startAgentSession(DataStoreUtil.getDbCreateTime(),DataStoreUtil.getCatalogNodeNum(),isMRT,ATestCases.DSUser);
        
        return 	
				setDB2Instance(DB2InstanceName) 
				+ " -64 "
				+ OPT_NODE + " 0 "
				+ setBinaryName()
				+ OPT_NODE + " A "
				+ setDatastore(DSName)
				+ setCredentials(_credentials)
				+ setSQLDirection(SQLDirection)
				+ setOperation(Operations)
				+ setSessionID(SessionID)
				+ setLAMode(LAMode)
				+ setTargetDB(TargetDB)
				+ setLogAnalysisReportType(LAReportType)
				+ setLobIgnore(LobIgnore)
				+ setTransactions(Transactions);
	}
	
	/**
	 *  Run LA
	 *  make command line for log analysis and execute it
	 *  
	 *  @param ARY_PATH Path to recovery expert back-end components
	 *  @param DB2InstanceName DB2 instance name
	 *  @param TargetDB Target database name
	 *  @param DSName Name of data store database name
	 *  @param _credentials Credentials for datastore and database users
	 *  @param TableList Tables for LA
	 *  @param Operations Operations for capturing (use Operation class)
	 *  @param SQLDirection Redo sql or Undo sql (use <code>SQLDirection</code> class)
	 *  @param LAMode log analysis mode (use Mode class)
	 *  @param LAReportType log analysis report type (use ReportType class)
	 *  @param Ignore lob data
	 */
	

	public void RunLogAnalysis(String DB2InstanceName,
			String TargetDB, String DSName, AryCredentials _credentials,Map<String, String> params,
			String Operations, String SQLDirection,
			String LAMode, String LAReportType, String LobIgnore, String Transactions){
		runprocess(makeLogAnalysisCommand( 
				  DB2InstanceName,
				  TargetDB,
				  DSName,
				  _credentials,
				  params,
				  Operations,
				  SQLDirection,
				  LAMode,
				  LAReportType,
				  LobIgnore,
				  Transactions));
	}
	
}
