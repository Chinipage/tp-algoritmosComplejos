package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

public class Sequence extends Parameter {

	private String type;
	private String format;

	Sequence(Element parameterNode, HashMap<String, String> variables) {

		this.name = this.replaceVariables(parameterNode.getAttribute("name"), variables);
		this.id = this.replaceVariables(parameterNode.getAttribute("id"), variables);
		this.value = this.replaceVariables(parameterNode.getAttribute("value"), variables);
		this.type = this.replaceVariables(parameterNode.getAttribute("type"), variables);
		this.format = this.replaceVariables(parameterNode.getAttribute("format"), variables);
	}

}
