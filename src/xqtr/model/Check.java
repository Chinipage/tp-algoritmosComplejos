package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

import xqtr.Controller;

public class Check extends Parameter {

	private String yvalue;
	private String nvalue;

	Check(Element parameterNode, HashMap<String, String> variables) {

		Controller controller = Controller.getInstance();

		this.name = controller.replaceVariables(parameterNode.getAttribute("name"), variables);
		this.id = controller.replaceVariables(parameterNode.getAttribute("id"), variables);
		this.value = controller.replaceVariables(parameterNode.getAttribute("value"), variables);
		this.yvalue = controller.replaceVariables(parameterNode.getAttribute("yvalue"), variables);
		this.nvalue = controller.replaceVariables(parameterNode.getAttribute("nvalue"), variables);
	}
	
}
