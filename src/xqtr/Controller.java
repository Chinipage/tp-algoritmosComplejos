package xqtr;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.model.Program;
import xqtr.util.Support;

public class Controller {

	private static Controller instance = null;
	private LinkedList<Program> programs = new LinkedList<Program>();

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

	//Config XML tags
	public static String programTag(){
		return "program";
	}

	public static String profileTag(){
		return "profile";
	}

	public static String parameterTag(){
		return "parameter";
	}

	//Load Config Xml
	private void addNewProgram(Element programNode){

		this.programs.add(new Program(programNode));
	}

	public void loadConfig(){

		Element configXML = Support.parseXML("Config2.xml");

		Support.elementList(configXML.getElementsByTagName(Controller.programTag())).forEach((programNode) -> {
			this.addNewProgram(programNode);
		});
	}

	public List<String> getExecutableProgramNames(){

		List<String> programsNamesList = new LinkedList<String>();

		this.programs.forEach((program) -> {
			programsNamesList.add(program.name);
		});

		return programsNamesList;
	}
}
