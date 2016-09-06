package xqtr;

import java.util.List;

import org.w3c.dom.Element;

import xqtr.util.Support;

public class Model {
	
	private List<String> programs;
	
	public Model() {
		
		Element configXML = Support.parseXML("Config.xml");
		programs = Support.transform(configXML.getElementsByTagName("program"), e -> e.getTextContent());
	}
	
	public List<String> getPrograms() {
		
		return programs;
	}
}