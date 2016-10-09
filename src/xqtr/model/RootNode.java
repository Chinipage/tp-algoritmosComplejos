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
		Program program = this.getProgram(programName);

		if((program != null))
			executableProfilesNames.addAll(program.getExecutableProfilesNames());
		
		return executableProfilesNames;
	}

	protected Profile getProfile(String programName, String profileName) {

		Program program;
		Profile profile = null;

		if((program = this.getProgram(programName)) != null)
			profile = program.getProfile(profileName);

		return profile;
		
	}

	public List<Parameter> getParameters(String programName, String profileName) {

		Profile profile;
		List<Parameter> parameters = new LinkedList<Parameter>();

		if((profile = this.getProfile(programName, profileName)) != null)
			parameters.addAll(profile.getParameters());

		return parameters;
	}

	public String getCommand(String programName, String profileName, HashMap<String, String> arguments) {
		
		String command = null;
		Profile profile;

		if((profile = this.getProfile(programName, profileName)) != null)
				command = this.replaceVariables(profile.getCommand(), arguments);

		return command; 
	}

}
