package xqtr;

import java.util.List;

import org.w3c.dom.Element;

import xqtr.model.Controller;
import xqtr.util.Support;

public class Model {
	
	private List<String> programs;
	
	public Model() {
		
	/*	Element configXML = Support.parseXML("Config.xml");
		programs = Support.map(e -> e.getTextContent(),
				Support.nodeList(configXML.getElementsByTagName("program")));*/
		Controller.getInstance().loadConfig();
		programs = Controller.getInstance().getExecutableProgramNames();
	}
	
	public List<String> getPrograms() {
		
		return programs;
	}
}
