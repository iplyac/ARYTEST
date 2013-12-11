package com.rs.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ibm.db2.jcc.DB2SimpleDataSource;
import com.rs.session.AryOpt;
import com.rs.tests.ATestCases;

public class DataStoreUtil {

	public static Connection dsConnection = null;
	public static Connection dbConnection = null;
	/**
     * This string is call statement for registering database
     *   in SLR if required.
     */
    private final static String SP_INIT_DB_PROCEDURE =
        "CALL SYSTOOLS.ARY_INIT_DB (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * This string is call statement for closing inactive agent sessions
     *   for the database.<br/>
     * Procedure parameters are:<br/>
     * 1. (IN) Database ID from table <code>SYSTOOLS.ARY_DATABASE</code>.<br/>
     * 2. (IN) It is time in UTC which session will be closed.
     */
    private final static String SP_CLOSE_SESSIONS =
        "CALL SYSTOOLS.ARY_CLOSE_TASKS(?)";
    
    /**
     * This string is call statement for opening agent session.<br/>
     * Procedure parameters are:<br/>
     * 1. (IN) Database ID from table <code>SYSTOOLS.ARY_DATABASE</code>.<br/>
     * 2. (IN) Session creation time in UTC.<br/>
     * 3. (OUT) New session ID.<br/>
     * 4. (IN) Session owner.
     */
    private final static String SP_CREATE_SESSION_PROCEDURE =
        "CALL SYSTOOLS.ARY_CREATE_SESSION_ONLY_AM (?, ?, ?, ?)";
    
    /**
     * This query is used for receiving birthday for database and
     *   instance timezone.
     */
    private static final String CTIME_QUERY =
        "SELECT REPLACE(REPLACE(REPLACE(VARCHAR_FORMAT(CTIME, 'YYYY-MM-DD HH24:MI:SS'), '-', ''), ':', ''), ' ', '') FROM SYSIBM.SYSTABLES" +
        " WHERE (CREATOR = 'SYSIBM' AND NAME = 'SYSTABLES') FETCH FIRST ROW ONLY FOR READ ONLY WITH UR";
    
    /**
     * This query is used for receiving SLR status
     */
    private static final String SLR_EXIST_PROCEDURE = 
    		"SELECT SYSTOOLS.ARY_VR_STATUS( ? ) FROM SYSIBM.SYSDUMMY1";

    /**
     * This query for obtaining catalog partition.
     */
    private static final String CAT_NODE_QUERY = "SELECT CATALOG_PARTITION FROM SYSIBMADM.SNAPDB";
    
    
    /**
     * <code>TRUE</code> for database. For insert to column with
     *   type <code>CHAR(1)</code>.
     */
    public static final String DBTRUE = "Y";

    /**
     * <code>FALSE</code> for database. For insert to column with
     *   type <code>CHAR(1)</code>.
     */
    public static final String DBFALSE = "N";
    
	/**
	 * Connecting to data store database
	 * @param ServerName Host name
	 * @param DB2InstancePort Instance port
	 * @param DSName Data store database name
	 * @param DSUser Data store database username
	 * @param DSPassword
	 * @return
	 */
	static public Connection connect(
						  String ServerName,
						  String DB2InstancePort,
						  String DSName,
						  String DSUser,
						  String DSPassword){
		Connection conn = null;
		try{
			DB2SimpleDataSource ds = new DB2SimpleDataSource();
			ds.setServerName(ServerName);
			ds.setPortNumber(Integer.parseInt(DB2InstancePort));
			ds.setDatabaseName(DSName);
			ds.setUser(DSUser);
			ds.setPassword(DSPassword);
			ds.setDriverType(4);

			conn = ds.getConnection(); 
			conn.setAutoCommit(false);
		}
		catch(SQLException ex)                                                    
	    {
	      System.err.println("SQLException information");
	      while(ex!=null) {
	        System.err.println ("Error msg: "  + ex.getMessage());
	        System.err.println ("SQLSTATE: "   + ex.getSQLState());
	        System.err.println ("Error code: " + ex.getErrorCode());
	        ex.printStackTrace();
	        ex = ex.getNextException(); 
				}
			}
		return conn;
	}
	
	/**
	 * Do query to data store to obtain results
	 * @param sqlKey SQL key for query (all queries in sql_queris.arysql)
	 * @param option options for query
	 * @return
	 * @throws SQLException
	 */
	
	static public ResultSet getResultForStatement(String sqlKey, Object...option ) throws SQLException{
		Statement stmt = dsConnection.createStatement();
		return stmt.executeQuery(String.format(ArySqlQueries.getQuery(sqlKey), option));
	}
	
	/**
	 * Get list of strings with obtained results
	 * @param sqlKey sqlKey SQL key for query (all queries in sql_queris.arysql)
	 * @param sessionID Session id
	 * @return
	 */
	
	static public List<String> getObtainedResultsList(String sqlKey, int sessionID)
	{
		List <String> obtainedResultsList = new ArrayList<String>();
	    try {
	    	ResultSet obtainedResults = DataStoreUtil.getResultForStatement(sqlKey, getIDPart(sessionID));
	    	String tmp = null;
			while (obtainedResults.next()){
				tmp = obtainedResults.getString(1).toString().replace(" ", "");
				if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
					tmp = tmp.replace("E+0", "E+");
				obtainedResultsList.add(tmp);
			}
	    } catch (SQLException ex) {
			 System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
		}

	    return obtainedResultsList;
	}
	
	public static int getDatabaseID(String TargetDB){
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		try
		{
			pStmt = dsConnection.prepareStatement(ArySqlQueries.getQuery("getDIDbyName"));
			pStmt.setString(1, TargetDB.toUpperCase());
			rs = pStmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
			rs.close();
			pStmt.close();
		}catch (SQLException ex) {
			 System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
		}
		return 0;
	}

	public static boolean isSlrExist(String TargetDB){
		int did = 0;
		ResultSet rs = null;
		PreparedStatement pStmt = null;
		boolean SlrExist = false;
		try{
			if ( (did = getDatabaseID(TargetDB))>0 )
			{
				pStmt = dsConnection.prepareStatement(SLR_EXIST_PROCEDURE);
				pStmt.setInt(1, did);
				rs = pStmt.executeQuery();
				rs.next();
				if (rs.getString(1).equals("N"))
					 SlrExist = false;
				else SlrExist = true;
				rs.close();
				pStmt.close();
			}
			else SlrExist = false;
		}catch (SQLException ex) {
			SlrExist = false;
			 System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
		}
		return SlrExist;
	}
	
	static public Connection getDsConnection(){
		return dsConnection;
	}
	
	static public Connection getDbConnection(){
		return dbConnection;
	}
	
	static public void close(Connection conn){
		
		try {
			conn.close();
		} catch (SQLException ex) {
			 System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
		}
	}
	
	/**
     * Register a database without checking birthday.
     */
    public int registerTargetBase(
        String birthDay,
        int nodeNum)
    {
        return registerTargetBase(birthDay, nodeNum, DBFALSE, false);
    }

    /**
     * Registers target database in datastore database.
     * 
     * @param aryCon - connection to datastore database.
     * @param dbName - target database name.
     * @param utcBirthDay - target database creation time in UTC.
     * @param nodeNum - number of node which must be registered.
     * @param isNoCheckMode - it is flag that we do not want check birthday.
     * @param solveConflict - if <code>TRUE</code> datastore information
     *   about database will be updated if database was recreated.
     * @return - database ID from table <code>SYSTOOLS.ARY_DATABASE</code>
     *   for target database.
     * 
     */
    public static int registerTargetBase(
        String birthDay,
        int nodeNum,
        String isNoCheckMode,
        boolean solveConflict)
    {
        int DID = 0;

        CallableStatement callStmt = null;

            try
            {
                callStmt = dsConnection.prepareCall(SP_INIT_DB_PROCEDURE);
                callStmt.setString(1, ATestCases.TargetDB);
                callStmt.setNull(2, Types.VARCHAR);
                callStmt.setInt(3, nodeNum);
                callStmt.setString(4, DBTRUE);
                callStmt.setString(5, isNoCheckMode);
                callStmt.setString(6, (hasPureScale() ? DBTRUE : DBFALSE));
                callStmt.setString(7, ATestCases.DB2InstanceName);
                callStmt.setString(8, ATestCases.HostName);
                callStmt.registerOutParameter(9, Types.INTEGER);
                callStmt.execute();
                //--> database id in systools.ary_database
                DID = callStmt.getInt(9);
                dsConnection.commit();
            }
            catch (SQLException ex) {
   			 System.err.println("SQLException information");
   		      while(ex!=null) {
   		        System.err.println ("Error msg: "  + ex.getMessage());
   		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
   		        System.err.println ("Error code: " + ex.getErrorCode());
   		        ex.printStackTrace();
   		        ex = ex.getNextException(); 
   				}
            }
        return DID;
    }

    protected static boolean hasPureScale(){
    	
    boolean hasPureScale = false;
    boolean hasRoutine = false;
    ResultSet rs = null;
    Statement stmt = null;
    	
    try
    	{
    	stmt = dsConnection.createStatement();
        rs = stmt.executeQuery("SELECT 1 FROM SYSIBM.SYSROUTINES WHERE ROUTINENAME='DB_MEMBERS'");
        hasRoutine = rs.next(); 
        hasPureScale = false;
        
        if (hasRoutine)
        	{
            	rs = stmt.executeQuery(ArySqlQueries.getQuery("hasPureScaleQuery"));
            	hasPureScale = rs.next();
        	}
    	}catch(SQLException ex){
    		System.err.println("SQLException information");
 		      while(ex!=null) {
 		        System.err.println ("Error msg: "  + ex.getMessage());
 		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
 		        System.err.println ("Error code: " + ex.getErrorCode());
 		        ex.printStackTrace();
 		        ex = ex.getNextException(); 
 				}
    	}
    	return hasPureScale;
    }
    
    
    /**
     * Create new agent session.
     */
    public static int startAgentSession(
        String birthDay,
        int catNode,
        boolean isNoCheckMode,
        boolean solveConflict,
        String owner,
        String spec)
    {
        //--> register database in SLR
        String isNonChechModeStr = (isNoCheckMode) ? DBTRUE : DBFALSE;
        //--> convert birthDay
        Integer DID = registerTargetBase(birthDay, catNode, isNonChechModeStr, solveConflict);

        return startAgentSessionByDID(DID, owner, spec);
    }

    /**
     * This method starts new CLP Recovery Expert agent session.
     */
    public static int startAgentSession(
        String birthDay,
        int catNode,
        boolean isNoCheckMode,
        String owner)
    {
    	return startAgentSession(birthDay, catNode, isNoCheckMode, false, owner, "");
    }

    /**
     * This method starts new Recovery Expert agent session.
     */
    public static int startAgentSessionByDID(
        int DID,
        String owner,
        String spec)
    {
        int sid = 0;

        CallableStatement callStmt = null;
//        PreparedStatement preStmt = null;
        try
        {
            //--> create timestamp with current time
            long ts = getCurrentDsCatNodeTs();
            Timestamp srvTs = new Timestamp(removeServerTimeOffset(ts));
            srvTs.setNanos(0);
            //--> close inactive agent sessions
            callStmt = dsConnection.prepareCall(SP_CLOSE_SESSIONS);
            callStmt.setInt(1, DID);
            callStmt.execute();
            dsConnection.commit();

            //--> start new agent session and get sessionID
            callStmt = dsConnection.prepareCall(SP_CREATE_SESSION_PROCEDURE);
            //--> set database ID
            callStmt.setInt(1, DID); //--> database ID
            callStmt.setTimestamp(2, srvTs); //--> create session time in UTC
            callStmt.setInt(3, -1); //--> current session ID
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.setString(4, owner);
            callStmt.execute();
            //--> get session ID
            sid = callStmt.getInt(3);

            //--> save specification
/*            SpecificationManager specMgr = new SpecificationManager(aryCon);
            specMgr.saveSpecificationInSession(owner, DID, spec, sid);
*/            //<--

            dsConnection.commit();

            //--> set results
//            res  = new SessionIDStruct(sid, ts, aryCon.getServerTZ());
            //<--
        }
        catch(SQLException ex){
    		System.err.println("SQLException information");
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
            SqlUtil.closeStatement(callStmt);
//            SqlUtil.closeStatement(preStmt);
        }
        return sid;
    }
    
    
    /**
     * Get current timestamp via JDBC connection.
     */
    public static long getCurrentDsCatNodeTs()
    {
        long res = 0;

        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try
        {
            pStmt = dbConnection.prepareStatement(ArySqlQueries.getQuery("currentTs"));
            rs = pStmt.executeQuery();
            rs.next();
            res = getTimeOfTs(rs.getTimestamp(1)); //--> CURRENT_TS
            dsConnection.commit();
        }
        catch(SQLException ex){
    		System.err.println("SQLException information");
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
            SqlUtil.closeResultSet(rs);
        }

        //--> remove nanoseconds
        res = (res - (res % 1000));

        return res;
    }
    
    /**
     * This method get current time stamp from UTC.
     */
    public static long getCurrentFromUtc(long ts)
    {
        Calendar cld = Calendar.getInstance();
        cld.setTimeInMillis(ts);
        return (ts + cld.get(Calendar.ZONE_OFFSET) + cld.get(Calendar.DST_OFFSET));
    }
    
    
    /**
     * Return time in mills from <code>java.sql.Timestamp</code>
     *   if it is not null and -1 otherwise.
     */
    public static long getTimeOfTs(Timestamp ts)
    {
        if (ts != null)
        {
            return getCurrentFromUtc(ts.getTime());
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * Save UTC in time stamp.
     */
    public static long removeServerTimeOffset(long ts)
    {
        Calendar cld = Calendar.getInstance();
        cld.setTimeInMillis(ts);
        return (ts - cld.get(Calendar.ZONE_OFFSET) - cld.get(Calendar.DST_OFFSET));
    }
    
    /**
     * This method returns database creation time by connection.
     * 
     * @param conn - connection to database.
     * 
     * @return create database time as <code>java.sql.Timestamp</code>.
     */
    public static String getDbCreateTime()
    {
        String ctime = "";

        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = dsConnection.createStatement();

            //--> get birthday
            rs = stmt.executeQuery(CTIME_QUERY);
            rs.next();
            ctime = rs.getString(1); //--> CTIME
            dsConnection.commit();
        }
        catch(SQLException ex){
    		System.err.println("SQLException information");
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
            SqlUtil.closeResultSet(rs);
        }

        return ctime;
    }
    
    /**
     * This method obtains number of catalog partition.
     * 
     * @param aryCon - connection to a database.
     * 
     * @return - number of catalog partition.
     * 
     * @throws ARYException if <code>SQLException</code> occurred.
     */
    public static short getCatalogNodeNum()
    {
        //--> Result
        short catNode = -1;

        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = dsConnection.createStatement();
            rs = stmt.executeQuery(CAT_NODE_QUERY);
            while (rs.next())
            {
                catNode = rs.getShort(1);
            }
            dsConnection.commit();
        }
        catch(SQLException ex){
    		System.err.println("SQLException information");
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
        	SqlUtil.closeResultSet(rs);
        }

        return catNode;
    }
    
    /**
     * This method gets string part for <i>datastore</i> tables.
     * 
     * @param sessionID - session ID.
     * 
     * @return return suffix with session ID for LA report tables.
     */
    public static String getIDPart(int sessionID)
    {
        NumberFormat nf = new DecimalFormat("000");
        String res = nf.format(sessionID);

        return res;
    }
}
