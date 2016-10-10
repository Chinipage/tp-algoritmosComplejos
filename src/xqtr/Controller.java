package xqtr;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import xqtr.model.Parameter;
import xqtr.model.RootNode;
import xqtr.util.Support;

public class Controller {
	
	private String currentProgram;
	private String currentProfile;

	private static Controller instance = null;
	private RootNode modelRootNode;

	public static Controller getInstance() {
		if (instance == null) {
			synchronized (Controller.class) {
				if (instance == null) {
					instance = new Controller();
					instance.loadConfig();
				}
			}
		}
		return instance;
	}

	public void setCurrentProgram(String programName) {
		currentProgram = programName;
	}
	
	public void setCurrentProfile(String profileName) {
		currentProfile = profileName;
	}
	
	public String getCurrentProgram() {
		return currentProgram;
	}
	
	public String getCurrentProfile() {
		return currentProfile;
	}

	//Interfaz con Modelo------------------------------------------------------------------------

	public void loadConfig() {
		modelRootNode = new RootNode(Support.parseXML(Application.configPath), Application.errorLogPath);
	}

	//TODO Deprecado. Usar getProgramsNames().
	public List<String> getExecutableProgramNames() {

		List<String> executableProgramsNames = new LinkedList<String>();

		if(modelRootNode != null)
			executableProgramsNames.addAll(modelRootNode.getProgramsNames());
	
		return executableProgramsNames;
	}

	public List<String> getProgramsNames() {

		List<String> programsNames = new LinkedList<String>();

		if(modelRootNode != null)
			programsNames.addAll(modelRootNode.getProgramsNames());

		return programsNames;
	}

	public List<String> getExecutableProfilesNames(String programName) {

		List<String> executableProfilesNames = new LinkedList<String>();

		if(modelRootNode != null)
			executableProfilesNames.addAll(modelRootNode.getExecutableProfilesNames(programName));

		return executableProfilesNames;
	}

	public List<Parameter> getParameters(String programName, String profileName) {

		List<Parameter> parameters = new LinkedList<Parameter>();

		if(modelRootNode != null)
			parameters.addAll(modelRootNode.getParameters(programName, profileName));

		return parameters;
	}

	public String getCommand(String programName, String profileName, HashMap<String, String> arguments) {

		String command = null;

		if(modelRootNode != null)
			command = modelRootNode.getCommand(programName, profileName, arguments);
	
		return command;
	}

	//Fin interfaz con Modelo--------------------------------------------------------------------
	
}
