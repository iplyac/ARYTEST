package com.rs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.zip.ZipInputStream;

import org.apache.ws.commons.util.Base64;
import org.apache.ws.commons.util.Base64.DecodingException;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.model.Attachment;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

public class TestLinkUtil {

    static TestLinkAPI api = null;
    
    /**
     * Connecting to test
     * 
     * @param url URL to xmlrpc (Example <a>http://testlink.rocketsoftware.com/testlink/lib/api/xmlrpc/v1/xmlrpc.php</a>) 
     * @param devKey Personal API access key (you can get it from "my settings" of testlink)
     */
    
	static public void connect(String url, String devKey) 
	{
		URL testlinkURL = null;
		try {
			testlinkURL = new URL(url);
		} catch (MalformedURLException mue) {
			mue.printStackTrace(System.err);
			System.exit(-1);
		}

		try {
			api = new TestLinkAPI(testlinkURL, devKey);
		} catch (TestLinkAPIException te) {
			te.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	/**
	 * Get sql key for query from "actions" column
	 * 
	 * @param fullTestCaseExternalId Full external test case id (Example RE-727)
	 * @param TestCaseVersion fullTestCaseExternalId
	 * @param stepnum Step number
	 * @return
	 */
	
	static public String getSqlKeyFromActions(String fullTestCaseExternalId, int TestCaseVersion, int stepnum)
	{
		return api.getTestCaseByExternalId(fullTestCaseExternalId, TestCaseVersion)
				  .getSteps().get(stepnum).getActions().replaceAll("\\<[^>]*>|\\s","");
	}

	/**
	 * Get archive file name from "expected results" column
	 * 
	 * @param fullTestCaseExternalId Full external test case id (Example RE-727)
	 * @param TestCaseVersion Test case version
	 * @param stepnum Step number
	 * @return
	 */
	
	static public String getFileNameFromExpectedResults(String fullTestCaseExternalId, int TestCaseVersion, int stepnum)
	{
		return api.getTestCaseByExternalId(fullTestCaseExternalId, TestCaseVersion)
				  .getSteps().get(stepnum).getExpectedResults().replaceAll("\\<[^>]*>|\\s","");
	}
	
	/**
	 * Get xml of expected results from test case attachment
	 * 
	 * @param testCaseId Internal test case id (Example 831617)
	 * @param testCaseExternalId External test case id (Example 727)
	 * @param FileName File name of zip archive with xml file of expected results
	 * @return String with xml
	 * @throws DecodingException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	
	static public String getAttachmentContent(int testCaseId,
			int testCaseExternalId, String FileName) throws DecodingException, UnsupportedEncodingException, Exception {
		
		Attachment[] attachments = api.getTestCaseAttachments(testCaseId,testCaseExternalId);
		
		byte[] decodedCompressedAttachment = null;
		byte[] decompressedData = null;
		
		for (Attachment attachment : attachments)
			if (attachment.getFileName().equals(FileName))
				//decode attached file from Base64
				decodedCompressedAttachment = Base64.decode(attachment.getContent());

		decompressedData = decompress(decodedCompressedAttachment);
		
		return (new String(decompressedData));
	}
	
/**
 * Unzip byte array
 * 
 * @param contentBytes byte array to unzip
 * 
 */
	public static byte[] decompress(byte[] contentBytes)
			throws IOException{
			
			byte[] buffer = new byte[1024];
		
			InputStream input = new ByteArrayInputStream(contentBytes);
	        ByteArrayOutputStream out = new ByteArrayOutputStream(contentBytes.length);
	        
	        ZipInputStream zip = new ZipInputStream(input);
	        
	        int len = 0;
	        if (zip.getNextEntry() != null)
	        {
	        	while ((len = zip.read(buffer)) > 0) {
		       		out.write(buffer, 0, len);
		            } 
	        }
			zip.close();
			out.close();
	        return out.toByteArray();
	    }
	
}
