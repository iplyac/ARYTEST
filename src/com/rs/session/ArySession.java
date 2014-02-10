package com.rs.session;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import com.rs.tests.ATestCases;
import com.rs.utils.AryCredentials;
import com.rs.utils.ArySqlQueries;
import com.rs.utils.ConsoleWriter;
import com.rs.utils.DataStoreUtil;
import com.rs.utils.SessionLogWriter;
import com.rs.utils.SqlUtil;


public abstract class ArySession {
	
	public String LAST_SESSION_ID;
	protected String BINARY_NAME;
	/**
     * Base partition means that for all partitions.
     */
    private static final int BASE_PARTITION = 1000;
    public final static String OPT_DB_BACKUPLOC = "backupdir";
    public final static String OPT_DB_LOGLOC = "logdir";
	/**
     * Options for datastore table <code>SYSTOOLS.ARY_OPTS</code>.
     */
    public static List<AryOpt> opts;
    public int SessionID;
	
    /**
     * Add base ARY option without value.
     */
    public void addAryOption(String key)
    {
        opts.add(new AryOpt(key, "", BASE_PARTITION, AryOpt.WITHOUT_VALUE));
    }

    /**
     * Add ARY option without value.
     */
    public void addAryOption(String key, int partition)
    {
        opts.add(new AryOpt(key, "", partition, AryOpt.WITHOUT_VALUE));
    }

    /**
     * Add base ARY option.
     */
    public void addAryOption(String key, String value)
    {
        opts.add(new AryOpt(key, value, BASE_PARTITION, AryOpt.WITH_VALUE));
    }

    /**
     * Add ARY option.
     */
    public void addAryOption(String key, String value, int partition)
    {
        opts.add(new AryOpt(key, value, partition, AryOpt.WITH_VALUE));
    }
    
    /**
     * This method must be returned concrete call statement for launching
     *   arymp agent process.
     */
    protected abstract String getRunAgentCommand();
    
	protected String isBatch(){
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			return ".bat";
		} else {
			return "";
		}
	}
	
	
	protected String getPathToAryRun(String ARY_PATH){
		return ARY_PATH + File.separatorChar +"bin64" + File.separatorChar + "aryrun" + isBatch();
	}
	

	public int getSessionID(){
		return SessionID;
	}
	
	protected String setDSUser(String DSUser) {
		return " -U " + DSUser;
	}

	protected String setDSPassword(String DSPassword) {
		return " -P " + DSPassword;
	}
	
	protected String setDBUser(String DBUser) {
		return " -u " + DBUser;
	}

	protected String setDBPassword(String DBPassword) {
		return " -p " + DBPassword;
	}
	
	protected String setCredentials(AryCredentials _credentials) {
		
		return   setDSUser(_credentials.DSUser)
			   + setDSPassword(_credentials.DSPassword)
			   + setDBUser(_credentials.DBUser)
			   + setDBPassword(_credentials.DBPassword);
	}
	
	protected String setDatastore(String DSName) {
		return " -D "+DSName;
	}
	
	protected String setDB2Instance(String DB2InstanceName) {
		return " -i "+DB2InstanceName;
	}
	
	protected String setTargetDB(String TargetDB) {
		return " -d "+TargetDB;
	}

	protected String setBinaryName() {
		return BINARY_NAME;
	}
	
	protected String setSessionID(int SessionID) {
		return " -s " +SessionID;
	}

	
	/**
	 * Run process
	 * @param CMD command
	 * @throws UnsupportedEncodingException 
	 */

protected void runprocess(String command){
	
	CallableStatement callStmt = null;
	int RC = 0;
	String message = "";
	
	
        //--> fill ARY_OPTS
        fillDsOpts();
        	try
            {
        callStmt = DataStoreUtil.getDsConnection().prepareCall(getRunAgentCommand());
        System.out.println(command);
        callStmt.setString(1, command);
        callStmt.setString(2, ATestCases.DSPassword);
        callStmt.setString(3, ATestCases.DBPassword);
        callStmt.setInt(4, SessionID);
     // ---->> HARDCODED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        callStmt.setInt(5, 0); 
     // <<---- HARDCODED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        callStmt.registerOutParameter(6, Types.VARCHAR);
        callStmt.registerOutParameter(7, Types.INTEGER);
        callStmt.execute();

        //--> get procedure output
        message = callStmt.getString(6);
        RC = callStmt.getInt(7);
        ConsoleWriter.println(message + "RC="+RC);
        DataStoreUtil.getDsConnection().commit();
            }catch(SQLException ex){
        		System.err.println("SQLException information");
        	      while(ex!=null) {
        	        System.err.println ("Error msg: "  + ex.getMessage());
        	        System.err.println ("SQLSTATE: "   + ex.getSQLState());
        	        System.err.println ("Error code: " + ex.getErrorCode());
        	        ex.printStackTrace();
        	        ex = ex.getNextException(); 
        			}
        	}finally
        	{
        		SqlUtil.closeStatement(callStmt);
        	}
	
	SessionLogWriter sessionLogWriter = new SessionLogWriter(SessionID);
	sessionLogWriter.start();
	
	try {
		sessionLogWriter.join();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}


/**
 * Fill table <code>SYSTOOLS>ARY_OPTS</code>.
 */
private void fillDsOpts()
{
    PreparedStatement pStmt = null;
    try
    {
        pStmt = DataStoreUtil.getDsConnection().prepareStatement(ArySqlQueries.getQuery("fillOptsQuery"));
        for (AryOpt opt : opts)
        {
        	if (opt.getValue()!=null)
				if ((opt.getValue().length() > 0)) {
					pStmt.setInt(1, SessionID);
					pStmt.setInt(2, opt.getPartition());
					pStmt.setString(3, opt.getKey());
					pStmt.setString(4, opt.getValue());
					pStmt.executeUpdate();
				}
        }
        DataStoreUtil.getDsConnection().commit();
    }catch(SQLException ex){
		System.err.println("Fill Opts SQLException information");
	      while(ex!=null) {
	        System.err.println ("Error msg: "  + ex.getMessage());
	        System.err.println ("SQLSTATE: "   + ex.getSQLState());
	        System.err.println ("Error code: " + ex.getErrorCode());
	        ex.printStackTrace();
	        ex = ex.getNextException(); 
			}
	}
    finally
    {
    	SqlUtil.closeStatement(pStmt);
    }
}



}
