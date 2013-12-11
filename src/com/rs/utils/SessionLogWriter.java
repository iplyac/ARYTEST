package com.rs.utils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.rs.session.AryLogAnalysis;
import com.rs.session.ArySession;
import com.rs.tests.ATestCases;

public class SessionLogWriter extends Thread{

	private class SessionStatusStruct{
		/**
	     * Id of row in <code>SYSTOOLS.ARY_SESSIONLOG</code>.
	     */
	    private int id;

	    /**
	     * String message.
	     */
	    private String message;

	    /**
	     * Message type.
	     */
	    private String messType;

	    /**
	     * Partition ID.
	     */
	    private int partition;

	    /**
	     * Time of message.
	     */
	    private long startTs;

	    /**
	     * First string parameter of message.
	     */
	    private String strParam1;

	    /**
	     * Second string parameter of message.
	     */
	    private String strParam2;

	    /**
	     * Third string parameter of message.
	     */
	    private String strParam3;

	    /**
	     * Fourth string parameter of message.
	     */
	    private String strParam4;
	    
	    /**
	     * Default constructor without parameters.
	     */
	    public SessionStatusStruct()
	    {
	        this(0, "", "", "", "", "", -1, -1, "I");
	    }
	    
	    /**
	     * Construct new object using all field this structure.
	     */
	    public SessionStatusStruct(
	        int id,
	        String message,
	        String strParam1,
	        String strParam2,
	        String strParam3,
	        String strParam4,
	        long startTs,
	        int partition,
	        String messType)
	    {
	        this.id = id;
	        this.message = message;
	        this.strParam1 = strParam1;
	        this.strParam2 = strParam2;
	        this.strParam3 = strParam3;
	        this.strParam4 = strParam4;
	        this.startTs = startTs;
	        this.partition = partition;
	        this.messType = messType;
	    }
	 
	    public void setId(int id)
	    {
	        this.id = id;
	    }

	    public void setMessage(String message)
	    {
	        this.message = message;
	    }

	    public void setMessType(String messType)
	    {
	        this.messType = messType;
	    }

	    public void setPartition(int partition)
	    {
	        this.partition = partition;
	    }

	    public void setStartTs(long startTs)
	    {
	        this.startTs = startTs;
	    }

	    public void setStrParam1(String strParam1)
	    {
	        this.strParam1 = strParam1;
	    }

	    public void setStrParam2(String strParam2)
	    {
	        this.strParam2 = strParam2;
	    }

	    public void setStrParam3(String strParam3)
	    {
	        this.strParam3 = strParam3;
	    }

	    public void setStrParam4(String strParam4)
	    {
	        this.strParam4 = strParam4;
	    }
	    
	    public int getId()
	    {
	        return id;
	    }

	    public String getMessage()
	    {
	        return message;
	    }
	    
	    public String getMessType()
	    {
	        return messType;
	    }
	    
	    public int getPartition()
	    {
	        return partition;
	    }

	    public long getStartTs()
	    {
	        return startTs;
	    }

	    public String getStrParam1()
	    {
	        return strParam1;
	    }

	    public String getStrParam2()
	    {
	        return strParam2;
	    }

	    public String getStrParam3()
	    {
	        return strParam3;
	    }

	    public String getStrParam4()
	    {
	        return strParam4;
	    }
	    
	    /**
	     * This method checks string on validity.
	     * 
	     * @param str - string that is checked on validity.
	     * 
	     * @return <code>true</code> if string is not NULL and is not empty,
	     *   <code>false</code> otherwise.
	     */
	    public boolean isNormal(String str)
	    {
	        return ((str != null && str.length() > 0) ? true : false);
	    }
	    
	    /**
	     * Trim string if the string is not null.
	     */
	    public String weakTrim(String str)
	    {
	        return (isNormal(str) ? str.trim() : "");
	    }
	    
	    public List<String> getParamArray()
	    {
	        List<String> res = new ArrayList<String>();


	        res.add(weakTrim(message));
            res.add(weakTrim(strParam1));
            res.add(weakTrim(strParam2));
            res.add(weakTrim(strParam3));
            res.add(weakTrim(strParam4));


	        return res;
	    }
	    
	    public String toString(){
	    	
	    	return "Struct " + this.getId() +" : "+ this.getMessage();
	    }
	}
    /**
     * It is default time interval between obtaining log session status.
     */
    private static final int SLEEP_TIME = 3;
    
    private static int SessionId;
    
    public static final int UPDATE_CHECKPOINT = 1;
    /**
     * It is default time interval between checks session.
     */
    private static final int CHECK_TIME = 12;
	
    /*
     * Session RC codes.
     */
    public static final int NO_DEFINED = 0;
    public static final int COMPLETED = 1;
    public static final int RUNNING = 2;
    public static final int FAILED = 3;
    public static final int CANCELED = 4;
    public static final int CANCELLING = 5;
    
    
    /**
     * This query obtains completed status of the session.
     *   Host variables are:<br/>
     *   1. (IN) session ID<br/>
     *   2. (IN) with_update<br/>
     *   3. (OUT) collect task return code<br/>
     *   4. (OUT) status message<br/>
     *   5. (OUT) status message param<br/>
     *   6. (OUT) status node<br/>
     *   7. (OUT) percent completed<br/>
     *   8. (OUT) start time stamp<br/>
     *   9. (OUT) end time stamp<br/>
     *   10. (OUT) canceled<br/>.
     */
    private static final String SP_SESSION_RESULT =
        "CALL SYSTOOLS.ARY_GET_SESSION_RESULT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * This procedure closes all inactive session for the database.<br/>
     * Procedure parameters are:<br/>
     * 1. (IN) Database name.<br/>
     * 2. (IN) Database creation time in UTC.<br/>
     * 3. (IN) Closing time. (Current instance time in UTC).
     */
    private static final String SP_CLOSE_SESSION =
        "CALL SYSTOOLS.ARY_CLOSE_SESSIONS_BY_DBNAME(?)";
    
    /**
     * This SQL query is used for obtaining rows from table <code>SYSTOOLS.ARY_SESSIONLOG</code>
     *   for the session which is not stage "aryplan -o generate".
     */
    private static final String COMMON_SL_QUERY =
        "SELECT A.POS, I.MESSAGE, I.PARAM1, I.PARAM2, I.PARAM3, I.PARAM4, I.START_TS, I.PARTITION_ID, I.MESS_TYPE " +
        "FROM SYSTOOLS.ARY_SESSIONLOG I, " +
        "(SELECT ROW_NUMBER() OVER (ORDER BY T.START_TS) AS POS, T.ID AS ID FROM SYSTOOLS.ARY_SESSIONLOG T" +
        " WHERE (T.SESSION_ID = ? AND T.SESSION_STAGE <> 'PG')) A" +
        " WHERE (I.ID = A.ID AND A.POS > ?) ORDER BY A.POS FETCH FIRST 1000 ROWS ONLY FOR READ ONLY";

    
    /**
     * Close all datastore sessions for the database.
     */
    public void closeCurrentSession(
        String dbName)
    {
        //--> close session for database <code>dbName</code>
        CallableStatement callStmt = null;
        try
        {
            callStmt = DataStoreUtil.getDsConnection().prepareCall(SP_CLOSE_SESSION);
            callStmt.setString(1, dbName);
            callStmt.execute();
            DataStoreUtil.getDsConnection().commit();
        }
        catch (SQLException ex) 
        {
        	ConsoleWriter.println("Error occured on closing session");
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
        }
    }
    
    /**
     * This method returns current session status from tables
     *   <code>SYSTOOLS.ARY_SESSION</code> and <code>SYSTOOLS.ARY_TASK</code>
     *   via procedure <code>SYSTOOLS.ARY_GET_SESSION_RESULT</code>.
     */
    public int getSessionStatus()
    {
        CallableStatement callStmt = null;
        int rc = -1;
        SessionStatusStruct sss = new SessionStatusStruct();
        
        try
        {
            callStmt = DataStoreUtil.dsConnection.prepareCall(SP_SESSION_RESULT);
            callStmt.setInt(1, SessionId);
            callStmt.setInt(2, UPDATE_CHECKPOINT);
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.registerOutParameter(4, Types.VARCHAR);
            callStmt.registerOutParameter(5, Types.VARCHAR);
            callStmt.registerOutParameter(6, Types.INTEGER);
            callStmt.registerOutParameter(7, Types.INTEGER);
            callStmt.registerOutParameter(8, Types.TIMESTAMP);
            callStmt.registerOutParameter(9, Types.TIMESTAMP);
            callStmt.registerOutParameter(10, Types.VARCHAR);
            callStmt.execute();
            DataStoreUtil.dsConnection.commit();
            rc = callStmt.getInt(3);
        }
        catch (SQLException ex) 
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
        finally
        {
            SqlUtil.closeStatement(callStmt);
        }
        return rc;
    }
    
	@Override
	public void run(){
        int loopCounter = 0;
        int sessionLogId = 0;
        
        for (;;)
        {
            try
            {
                sleep(SLEEP_TIME * 1000);
                loopCounter += SLEEP_TIME;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            
          //--> check session on looping
            /*if ((loopCounter % CHECK_TIME) == 0)
            {
                closeCurrentSession(ATestCases.TargetDB);
            }*/
            
            int scss = getSessionStatus();
            
            List<SessionStatusStruct> slogList = getCommonSessionLog(sessionLogId);
            
            int maxLogLen = 0;
            //--> print session log

            Object[] sArgs;
            for (SessionStatusStruct sss : slogList)
            {
                sessionLogId = sss.getId();
                
            	List<String> logs = sss.getParamArray();
                
                String message = logs.get(0);
                logs.remove(0);
                sArgs = logs.toArray(new Object[logs.size()]);

                String log = Messages.getMessage(message, sArgs);
                //--> get max log width
                if (maxLogLen < log.length())
                {
                    maxLogLen = log.length();
                }

                ConsoleWriter.println(log);
            }
            if (scss == COMPLETED){
            	if (scss == FAILED){
                	ConsoleWriter.println("Session Failed");
                	break;
                }
            	
            	break;
            }
        }
	}

	
	
	private List<SessionStatusStruct> getCommonSessionLog(int sessionLogId){
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		List<SessionStatusStruct> slogList = new ArrayList<SessionStatusStruct>();
        	
		try {
			pStmt = DataStoreUtil.dsConnection.prepareStatement(COMMON_SL_QUERY);
			pStmt.setInt(1, SessionId);
			pStmt.setInt(2, sessionLogId);
			rs = pStmt.executeQuery();
			while (rs.next())
            {
				SessionStatusStruct sss = new SessionStatusStruct();	
                sss.setId(rs.getInt(1)); //--> ID
                sss.setMessage(rs.getString(2)); //--> KEY
                /*String param1 = isMessageWithSQLSubMessage(sss.getMessage()) ? 
                                getInternalSqlMessage(conn, rs.getString(3)) :
                                rs.getString(3);*/
                sss.setStrParam1(rs.getString(3)); //--> CHAR_PARAM1
                sss.setStrParam2(rs.getString(4)); //--> CHAR_PARAM2
                sss.setStrParam3(rs.getString(5)); //--> CHAR_PARAM3
                sss.setStrParam4(rs.getString(6)); //--> CHAR_PARAM4
//                sss.setStartTs(rs.getTimestamp(7)); //--> START_TS
                sss.setPartition(rs.getInt(8)); //--> PARTITION_ID
                sss.setMessType(rs.getString(9)); //--> MESS_TYPE
                
                slogList.add(sss);
            }
			DataStoreUtil.getDsConnection().commit();
			
		} catch (SQLException ex) 
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
        finally
        {
            SqlUtil.closeStatement(pStmt);
            SqlUtil.closeResultSet(rs);
        }

		return slogList;
	}
	
	/**
	 * Constructor
	 * @param SessionId
	 */
	
	public SessionLogWriter(int SessionId) {
		this.SessionId = SessionId;
	}


	
}
