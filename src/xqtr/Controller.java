package xqtr;


import java.util.List;

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

	public void loadConfig() {
		modelRootNode = new RootNode(Support.parseXML(Application.configPath));
	}

	public List<String> getExecutableProgramNames() {

		return modelRootNode.getProgramsNames();
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
}
