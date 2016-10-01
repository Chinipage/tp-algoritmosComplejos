package xqtr;


import java.util.List;

import xqtr.model.RootNode;
import xqtr.util.Support;

public class Controller {

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

	public void loadConfig(){
		modelRootNode = new RootNode(Support.parseXML("Config2.xml"));
	}

	public List<String> getExecutableProgramNames(){

		return modelRootNode.getExecutableProgramNames();
	}
}
