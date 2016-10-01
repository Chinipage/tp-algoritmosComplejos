package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

public class Check extends Parameter {

	private String yvalue;
	private String nvalue;

	Check(Element parameterNode, HashMap<String, String> variables) {

		this.name = this.replaceVariables(parameterNode.getAttribute("name"), variables);
		this.id = this.replaceVariables(parameterNode.getAttribute("id"), variables);
		this.value = this.replaceVariables(parameterNode.getAttribute("value"), variables);
		this.yvalue = this.replaceVariables(parameterNode.getAttribute("yvalue"), variables);
		this.nvalue = this.replaceVariables(parameterNode.getAttribute("nvalue"), variables);
	}
	
}
