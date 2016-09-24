package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;

import xqtr.Controller;
import xqtr.util.Support;

public class Profile {

	private String name;
	private String args = "";

	private LinkedList<Profile> subProfiles = new LinkedList<Profile>();
	private LinkedList<Parameter> parameters = new LinkedList<Parameter>();

	private void addNewProfile(Element profileNode, HashMap<String, String> variables) {
		this.subProfiles.add(new Profile(profileNode, variables));
	}

	private void addNewParameter(Element parameterNode, HashMap<String, String> variables) {

		this.parameters.add(Parameter.newParameter(parameterNode, variables));

	}

	Profile(Element profileNode, HashMap<String, String> variables){

		Controller controller = Controller.getInstance();
		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = Support.deepCopyVariables(variables);
		declaredVariables.putAll(controller.getVariables(profileNode));

		this.name = controller.replaceVariables(profileNode.getAttribute("name"), variables);
		
		if(profileNode.hasAttribute("args")) {
			this.args = controller.replaceVariables(profileNode.getAttribute("args"), variables);
		}

		Support.elementList(profileNode.getElementsByTagName(Controller.getInstance().profileTag())).forEach((subProfileNode) -> {
			this.addNewProfile(subProfileNode, declaredVariables);
		});

		Support.elementList(profileNode.getElementsByTagName(Controller.getInstance().parameterTag())).forEach((parameterNode) -> {
			this.addNewParameter(parameterNode, declaredVariables);
		});

	}
}
