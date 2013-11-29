package com.rs.tests;

//import junit.framework.JUnit4TestAdapter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.rs.session.AryLogAnalysis;
import com.rs.session.ArySLR;
import com.rs.utils.AryCredentials;
import com.rs.utils.ConsoleWriter;

import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;

@SuppressWarnings("serial")
@RunWith(JUnit4.class)
public abstract class ATestCases extends TestSuite {
   /**
   * Path to recovery expert installation directory
   */
	public final static String ARY_PATH = System.getProperty("ary.path");
	
	/**
	 * Instance name
	 */
	public final static String DB2InstanceName = System.getProperty("instance.name");
	
	/**
	 * Instance port
	 */
	public final static String DB2InstancePort = System.getProperty("instance.port");
	
	/**
	 * Target database name
	 */
	public final static String TargetDB = System.getProperty("db.name");
	
	/**
	 * Data store database name
	 */
	protected final static String DSName = System.getProperty("ds.name");
	
	/**
	 * Data store database user name
	 */
	public final static String DSUser = System.getProperty("ds.user");
	
	/**
	 * Data store database user password
	 */
	public final static String DSPassword = System.getProperty("ds.pw");
	
	/**
	 * Database user name
	 */
	public final static String DBUser = System.getProperty("db.user");
	
	/**
	 * Log path
	 */
	public final static String LOGPATH = System.getProperty("log.path");
	
	/**
	 * Backup path
	 */
	protected final static String BACKUPPATH = System.getProperty("backup.path");
	
	/**
	 * Database user password
	 */
	public final static String DBPassword = System.getProperty("db.pw");
	
	/**
	 * TestLink url URL to xmlrpc default <a>http://testlink.rocketsoftware.com/testlink/lib/api/xmlrpc/v1/xmlrpc.php</a>
	 */
	protected final static String testlinkURL = System.getProperty("tl.api.url");
	
	/**
	 * TestLink API access key
	 */
	protected final static String testlinkKey = System.getProperty("tl.dev.key");
	
	/**
	 * Host name (by default current host)
	 */
	public static String HostName = "";
	
	static {
		
		try {
			HostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			ConsoleWriter.println("Cannot resolve local hostname");
			e.printStackTrace();
		}
	}
	
	/**
	 * Internal test case id
	 */
	protected static int TEST_CASE_ID;
	
	/**
	 * External test case id
	 */
	protected static int TEST_CASE_EXTERNAL_ID;
	
	/**
	 * Test case step number
	 */
	protected static int TEST_CASE_STEP_NUM;
	
	/**
	 * Test case version
	 */
	protected static int TEST_CASE_VERSION;
	
	/**
	 * Credentials for sessions (means -U -u -P -p keys)
	 * @see AryCredentials
	 */
	protected static final AryCredentials _credentials = new AryCredentials();
	
	/**
	 * Path to SQL
	 */
	public static Path PathToSQL = Paths.get(System.getProperty("user.dir"),"src","com","rs","tests","testsetup");
	
	public static boolean withLob = false;
	
	protected static final ArySLR slr = new ArySLR();
	protected static final AryLogAnalysis la = new AryLogAnalysis();
	
public ATestCases() {
	
	}


@Before
	public void setUp(){}

}