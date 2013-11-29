package com.rs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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

public class XmlMakerFromFile {
	public static class Settings{
		
	@Parameter(names = {"-source", "-s"}, description = "Source of sql", descriptionKey = "Source of sql", required = true)
	protected String source;
	
	@Parameter(names = {"-target", "-t"}, description = "Output xml file", descriptionKey = "Output xml file", required = true)
	protected String target;
	
	@Parameter(names = {"-delimeter", "-d"}, description = "Symbol code for delimeter", descriptionKey = "Symbol code for delimeter")
	protected int delimeter = 59;
	
	@Parameter(names = {"--help", "-help", "-?"}, help = true, description = "Print help")
	private boolean help;
	}
	
	public static void main(String[] args) throws Exception {

		PrintWriter printer = new PrintWriter(new OutputStreamWriter(System.out, System.getProperty("file.encoding")));
		
		Settings settings = new Settings();
		JCommander cmd = new JCommander(settings, args);
		cmd.setProgramName("XmlMakerFromFile");
		FileInputStream FileReader = null;
		
		if (settings.help)cmd.usage();
		else
			try {

				File file = new File(settings.source);
				
				FileReader = new FileInputStream(file);
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document doc = docBuilder.newDocument();

				Element rootElement = doc.createElement("testcasesetup");
				doc.appendChild(rootElement);

				Element sql = doc.createElement("SQL");
				rootElement.appendChild(sql);

				Element sqlStatement;
				int chrCode, stmt_id = 0;
				String sqlStatementString = "";
				while((chrCode = FileReader.read()) != -1)
				{
					if (chrCode != settings.delimeter)
						sqlStatementString+=(char)chrCode;
						else
							if ((chrCode = FileReader.read()) == 10)
						{	
							sqlStatement = doc.createElement("statement");
							sqlStatement.setAttribute("stmt_id", String.valueOf(stmt_id++));
							sqlStatement.appendChild(doc.createTextNode(sqlStatementString.replaceAll("\\r|\\n", "")));
							sqlStatementString = "";
							sql.appendChild(sqlStatement);
						}else{
							sqlStatementString+=(char)settings.delimeter+""+(char)chrCode;
						}
				}

				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);

				StreamResult out = new StreamResult(new File(settings.target));
				transformer.transform(source, out);

				System.out.println("Done");

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (FileReader != null)
						FileReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
	}
	
}
