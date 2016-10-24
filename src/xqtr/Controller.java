package xqtr;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import xqtr.model.ParameterNode;
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

	public boolean isReady() {
		return modelRootNode != null;
	}
	
	public void whenReady(Runnable runnable) {
		Support.setInterval(1000, s -> {
			if(isReady()) {
				runnable.run();	
				s.shutdown();
			}
		});
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
		modelRootNode = null;
		Support.setTimeout(100, () -> {
			modelRootNode = new RootNode();
		});
	}

	public List<String> getProgramsNames() {

		List<String> programsNames = new LinkedList<String>();

		if(modelRootNode != null)
			programsNames.addAll(modelRootNode.getProgramsNames());

		return programsNames;
	}

	public List<String> getProfilesForCurrentProgram() {

		List<String> executableProfilesNames = new LinkedList<String>();

		if(modelRootNode != null && hasCurrentProgram())
			executableProfilesNames.addAll(modelRootNode.getProfilesNames(currentProgram));

		return executableProfilesNames;
	}

	public List<ParameterNode> getParametersForCurrentProfile() {

		List<ParameterNode> parameters = new LinkedList<ParameterNode>();

		if(modelRootNode != null && hasCurrentProfile())
			parameters.addAll(modelRootNode.getParameters(currentProgram, currentProfile));

		return parameters;
	}

	public String getCommandForCurrentProfile(Map<String, String> arguments) {

		String command = null;

		if(modelRootNode != null && hasCurrentProfile())
			command = modelRootNode.getCommand(currentProgram, currentProfile, arguments);
		
		return command;
	}
	
	public boolean hasCurrentProgram() {
		return currentProgram != null && !currentProgram.trim().isEmpty();
	}
	
	public boolean hasCurrentProfile() {
		return hasCurrentProgram() && currentProfile != null && !currentProfile.trim().isEmpty();
	}

	//Fin interfaz con Modelo--------------------------------------------------------------------
	
}
