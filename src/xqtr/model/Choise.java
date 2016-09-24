package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

import xqtr.Controller;

public class Choise extends Parameter {

	Choise(Element parameterNode, HashMap<String, String> variables) {

		Controller controller = Controller.getInstance();

		this.name = controller.replaceVariables(parameterNode.getAttribute("name"), variables);
		this.id = controller.replaceVariables(parameterNode.getAttribute("id"), variables);
		this.value = controller.replaceVariables(parameterNode.getAttribute("value"), variables);
	}
}
