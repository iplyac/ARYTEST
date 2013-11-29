package com.rs.utils;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.db2.jcc.DB2SimpleDataSource;

import junit.framework.Assert;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;



public class AryTestsMain {
	public static void main(String[] args) throws Exception  {
		TestRunner runner = new TestRunner();
		TestSuite suite = new TestSuite();
		// ---------- For Tests
		System.setProperty("ary.path", "C:\\re4100");
		System.setProperty("instance.name", "DB2");
		System.setProperty("instance.port", "50000");
		System.setProperty("db.name", "carolv1");
		System.setProperty("ds.name", "datastor");
		System.setProperty("ds.user", "DB2ADMIN");
		System.setProperty("db.user", "DB2ADMIN");
		System.setProperty("ds.pw", "R0cket");
		System.setProperty("db.pw", "R0cket");
		
		// ---------- For TestLink
	/*	System.setProperty("tl.api.url", "http://testlink.rocketsoftware.com/testlink/lib/api/xmlrpc/v1/xmlrpc.php");
		System.setProperty("tl.dev.key", "dfae6dd9d5e8ab97f316bcd89f7c676c");

		TestLinkUtil.connect( "http://testlink.rocketsoftware.com/testlink/lib/api/xmlrpc/v1/xmlrpc.php", 
							  "dfae6dd9d5e8ab97f316bcd89f7c676c");
		int TEST_CASE_ID = 832514;
		int TEST_CASE_EXTERNAL_ID = 1023;
		
		int TEST_CASE_STEP_NUM = 0;
		int TEST_CASE_VERSION = 1;
		
		String FileName = TestLinkUtil.getFileNameFromExpectedResults("RE-"+ TEST_CASE_EXTERNAL_ID, TEST_CASE_VERSION, TEST_CASE_STEP_NUM);
		String xml = TestLinkUtil.getAttachmentContent(TEST_CASE_ID, TEST_CASE_EXTERNAL_ID, FileName);
		
//		String xml = "<testcaseresults><results><result>INSERTINTO\"IPLYAC\".\"MU\"(\"INDEXKEY1\",\"INDEXKEY2\",\"VCHAR20\",\"LVCHAR\",\"BLOB30\")VALUES(25,'30','c_T#-&amp;]L#LXXq1a','c_T#-&amp;]L#LXXq1a625062504950018750c_T#-&amp;]L#LXXq1a',BLOB(X'635F54232D265D4C234C5858713161'))</result></results></testcaseresults>";
		
		
		List <String> expectedResultsList = XmlParser.getResultsStringList(xml);
		System.out.println(expectedResultsList.size());
		for (String s: expectedResultsList)
			System.out.println(s);*/
		
		/*PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out,System.getProperty("file.encoding")));
		out.println("qqqq");
		out.flush();*/
		DataStoreUtil.connect("localhost", "50000", "datasto2", "DB2ADMIN", "Rocket12");
		CallableStatement pStmt = null;
		int sid = 0;
		ResultSet rs = null;
		pStmt = DataStoreUtil.getDsConnection().prepareCall("CALL SYSTOOLS.ARY_CREATE_SESSION(?, ?, ?, ?)");
		pStmt.setInt(1, 3);
		pStmt.setInt(2, 0);
		pStmt.setString(3, "ARYLA");
		pStmt.setInt(4, sid);
		
		pStmt.execute();
		System.out.println(pStmt.getInt(4));
	    }
	}
