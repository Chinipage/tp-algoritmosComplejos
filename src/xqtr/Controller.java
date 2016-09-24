package xqtr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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

	/*Config XML tags*/
	public String programTag(){
		return "program";
	}

	public String profileTag(){
		return "profile";
	}

	public String parameterTag(){
		return "parameter";
	}

	public String variableTag(){
		return "var";
	}

	/*Variables management (quizas esto deberia estar en support, no se)*/
	public String replaceVariables(String attribute, HashMap<String, String> variables){

		String replacedAttribute = attribute;

		/*No pude usar el metodo foreach con una expresion lambda ya que no puedo usar dentro
		 * de esa misma espresion las variables locales del metodo que la contiene
		 */
		for(Entry<String, String> e : variables.entrySet()) {

			 String variableName = e.getKey();
			 String variableValue = e.getValue();

			 replacedAttribute = replacedAttribute.replaceAll("\\{" + variableName + "\\}", variableValue);
		}

		return replacedAttribute;

	}

	public HashMap<String, String> getVariables(Element node){

		HashMap<String, String> variables = new HashMap<String, String>();

		Support.elementList(node.getElementsByTagName(this.variableTag())).forEach((globalVariable) -> {
			variables.put(globalVariable.getAttribute("id"), globalVariable.getAttribute("value"));
		});

		System.out.println("Node variables of " + node.getNodeName());
		variables.forEach((id, value) -> {
			System.out.println("id: " + id + " value: " + value);
		});

		return variables;
	}

	/*Load Config Xml*/
	private void addNewProgram(Element programNode, HashMap<String, String> variables){

		this.programs.add(new Program(programNode, variables));
	}

	public void loadConfig(){

		Element configXML = Support.parseXML("Config2.xml");
		HashMap<String, String> globalVariables = this.getVariables(configXML);

		Support.elementList(configXML.getElementsByTagName(Controller.getInstance().programTag())).forEach((programNode) -> {
			this.addNewProgram(programNode, globalVariables);
		});
	}

	public List<String> getExecutableProgramNames(){

		List<String> programsNamesList = new LinkedList<String>();

		this.programs.forEach((program) -> {
			programsNamesList.add(program.getName());
		});

		return programsNamesList;
	}
}
