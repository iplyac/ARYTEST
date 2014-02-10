package com.rs.utils;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class XmlMaker {
	
	public static class Settings{
	
	@Parameter(names = "-host", description = "Host name", descriptionKey = "Host name")
	protected String host = "localhost";
	
	@Parameter(names = "-port", description = "Instance port")
	protected String port;
	
	@Parameter(names = "-dsname", description = "Datastore database name", required = true)
	protected String dsname;
	
	@Parameter(names = "-dsuser", description = "Datastore user name", required = true)
	protected String dsuser;
	
	@Parameter(names = "-dspassword", description = "Datastore password", required = true)
	protected String dspassword = "Rocket12";

	@Parameter(names = "-sqlkey", description = "SQL key", required = true)
	protected String sqlkey;

	@Parameter(names = "-sessionid", description = "Session id", required = true)
	protected int sessionid;
	
	@Parameter(names = "-filename", description = "Full path file name")
	protected String filename = "output.xml";
	
	@Parameter(names = {"--help", "-help", "-?"}, help = true, description = "Print help")
	private boolean help;

	}
	
	public static void main(String[] args) throws Exception  {
		
		PrintWriter printer = new PrintWriter(new OutputStreamWriter(System.out,System.getProperty("file.encoding")));
		Settings settings = new Settings();
		JCommander cmd = new JCommander(settings, args);
		cmd.setProgramName("XmlMaker");
		if (settings.help) cmd.usage(); else
		try
		{
			
//		  DataStoreUtil.connect(settings.host, settings.port, settings.dsname, settings.dsuser, settings.dspassword);
		  DataStoreUtil.dsConnection = DataStoreUtil.connect(settings.host, settings.port, settings.dsname, settings.dsuser, settings.dspassword);
		  List <String> ResultsList = DataStoreUtil.getObtainedResultsList(settings.sqlkey, settings.sessionid);
			
		  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		  DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		  Document doc = docBuilder.newDocument();

		  Element rootElement = doc.createElement("testcaseresults");
		  doc.appendChild(rootElement);

		  Element results = doc.createElement("results");
		  rootElement.appendChild(results);
		  
		  Element result;
		  int result_id=0;
		  for (String result_string:ResultsList)
		   {
			  result = doc.createElement("result");
			  result.setAttribute("result_id", String.valueOf(result_id++));
	
			  result.appendChild(doc.createTextNode(XmlNormilize(result_string)));
			  results.appendChild(result);
		   }

		  TransformerFactory transformerFactory = TransformerFactory.newInstance();
		  Transformer transformer = transformerFactory.newTransformer();
		  DOMSource source = new DOMSource(doc);

		  StreamResult out =  new StreamResult(new File(settings.filename));
		  transformer.transform(source, out);

		  printer.println("Done");

		}catch(ParserConfigurationException pce){
		  pce.printStackTrace();
		}catch(TransformerException tfe){
		  tfe.printStackTrace();
		}
	}
	
public static String XmlNormilize(String xml){
	
	xml = xml.replace("%", "%%");
	
	/*xml = xml.replace("<", "![CDATA[<]]");
	xml = xml.replace(">", "![CDATA[>]]");
	
	xml = xml.replace("![CDATA[", "<![CDATA[");
	xml = xml.replace("]]", "]]>");*/
	xml = xml.replace("<", "![CDATA[<]]");
	xml = xml.replace(">", "![CDATA[>]]");
	
	xml = xml.replace("![CDATA[>]]", "<![CDATA[>]]>");
	xml = xml.replace("![CDATA[<]]", "<![CDATA[<]]>");
	
	return xml;
}
	
}
