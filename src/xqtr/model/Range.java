package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

public class Range extends Parameter {

	Range(Element parameterNode, HashMap<String, String> variables) {

		this.name = this.replaceVariables(parameterNode.getAttribute("name"), variables);
		this.id = this.replaceVariables(parameterNode.getAttribute("id"), variables);
		this.value = this.replaceVariables(parameterNode.getAttribute("value"), variables);
	}

}
