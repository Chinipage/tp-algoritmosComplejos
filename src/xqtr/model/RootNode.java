package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.util.Support;

public class RootNode extends ModelNode {

	private LinkedList<Program> programs = new LinkedList<Program>();

	public RootNode(Element rootElement){

		HashMap<String, String> variables = this.getVariables(rootElement);

		Support.elementList(rootElement.getElementsByTagName(this.programTag())).forEach(programNode -> {
			this.addNewProgram(programNode, variables);
		});
	}

	/*Load Config Xml*/
	private void addNewProgram(Element programNode, HashMap<String, String> variables){

		this.programs.add(new Program(programNode, variables));
	}

	public void loadConfig(){

		Element configXML = Support.parseXML("Config2.xml");
		HashMap<String, String> globalVariables = this.getVariables(configXML);

		Support.elementList(configXML.getElementsByTagName(this.programTag())).forEach((programNode) -> {
			this.addNewProgram(programNode, globalVariables);
		});
	}

	public List<String> getExecutableProgramNames() {

		List<String> executablePrograms = new LinkedList<String>();
		
		programs.forEach(program -> {
			executablePrograms.add(program.name);
		});

		return executablePrograms;
	}
}
