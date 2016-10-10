package xqtr.model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class RootNode extends ModelNode {

	private LinkedList<ProgramNode> programs = new LinkedList<ProgramNode>();

	public RootNode(Element rootElement, File errorLogFile){

		HashMap<String, String> variables;

		this.openErrorLogFile(errorLogFile);

		variables = this.getVariables(rootElement);

		this.elementList(rootElement.getElementsByTagName(programTag)).forEach(programNode -> {
			this.addNewProgram(programNode, variables);
		});

		this.closeErrorLogFile();
	}

	private void addNewProgram(Element programNode, HashMap<String, String> variables){

		this.programs.add(new ProgramNode(programNode, variables));
	}

	private ProgramNode getProgram(String programName) {
		return programs.stream().filter(program -> program.getAttribute("name").equals(programName)).findFirst().orElse(null);
	}

	public List<String> getProgramsNames() {

		List<String> programsList = new LinkedList<String>();
		
		programs.forEach(program -> {
			programsList.add(program.getAttribute("name"));
		});

		return programsList;
	}

	public List<String> getExecutableProfilesNames(String programName) {

		List<String> executableProfilesNames = new LinkedList<String>();
		ProgramNode program = this.getProgram(programName);

		if((program != null))
			executableProfilesNames.addAll(program.getExecutableProfilesNames());
		
		return executableProfilesNames;
	}

	protected ProfileNode getProfile(String programName, String profileName) {

		ProgramNode program;
		ProfileNode profile = null;

		if((program = this.getProgram(programName)) != null)
			profile = program.getProfile(profileName);

		return profile;
		
	}

	public List<ParameterNode> getParameters(String programName, String profileName) {

		ProfileNode profile;
		List<ParameterNode> parameters = new LinkedList<ParameterNode>();

		if((profile = this.getProfile(programName, profileName)) != null)
			parameters.addAll(profile.getParameters());

		return parameters;
	}

	public String getCommand(String programName, String profileName, HashMap<String, String> arguments) {
		
		String command = null;
		ProfileNode profile;

		if((profile = this.getProfile(programName, profileName)) != null)
				command = this.replaceVariables(profile.getCommand(), arguments);

		return command; 
	}

}
