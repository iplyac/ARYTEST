package com.rs.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlParser {

	protected final static String XPATH = "./testcaseresults/results/child::result";
	/**
	 * Parse xml and return list of strings by xpath <code>./testcaseresults/results/child::result</code>
	 * @param xml
	 * @return
	 * @throws XPathExpressionException
	 */
	static public List<String> getResultsStringList(String xml)
			throws XPathExpressionException {

		List<String> ResultsStringList = new ArrayList<String>();
		xml = xmlCleaning(xml);

		NodeList resultNodes = getResultNodelist(xml);
		for (int i = 0; i < resultNodes.getLength(); i++)
			ResultsStringList.add(resultNodes.item(i).getTextContent().replace(" ", ""));
		return ResultsStringList;
	}
	
	static public NodeList getResultNodelist(String xml) throws XPathExpressionException{
		  XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
		  XPath xPath = factory.newXPath();
		  
		  return (NodeList) xPath.evaluate(XPATH, new InputSource(new  StringReader(xml)), XPathConstants.NODESET);
	}
	
	static public String xmlCleaning(String xml){
		
		xml = xml.replace("<div>", "");
		xml = xml.replace("</div>", "");
		
		xml = xml.replace("<p>", "");
		xml = xml.replace("</p>", "");
		
		xml = xml.replace("&lt;", "<");
		xml = xml.replace("&gt;", ">");
		
		xml = xml.replace("&quot;", "\"");
		
		xml = xml.replace("&#39;", "'");
		
		xml = xml.replace("&nbsp;", "");
		
		return xml;
	}
}
