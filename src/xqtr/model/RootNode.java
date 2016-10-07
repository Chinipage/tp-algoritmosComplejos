package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class RootNode extends ModelNode {

	private LinkedList<Program> programs = new LinkedList<Program>();

	public RootNode(Element rootElement){

		HashMap<String, String> variables = this.getVariables(rootElement);

		this.elementList(rootElement.getElementsByTagName(programTag)).forEach(programNode -> {
			this.addNewProgram(programNode, variables);
		});
	}

	private void addNewProgram(Element programNode, HashMap<String, String> variables){

		this.programs.add(new Program(programNode, variables));
	}

	private Program getProgram(String programName) {
		return programs.stream().filter(program -> program.getAttribute("name").equals(programName)).findFirst().get();
	}

	public List<String> getProgramsNames() {

		List<String> programsList = new LinkedList<String>();
		
		programs.forEach(program -> {
			programsList.add(program.getAttribute("name"));
		});

		return programsList;
	}

	public List<String> getExecutableProfilesNames(String programName) {
		return this.getProgram(programName).getExecutableProfilesNames();
	}

}
