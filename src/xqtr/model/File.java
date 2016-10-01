package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

public class File extends Parameter {

	private String format;

	File(Element parameterNode, HashMap<String, String> variables) {

		this.name = this.replaceVariables(parameterNode.getAttribute("name"), variables);
		this.id = this.replaceVariables(parameterNode.getAttribute("id"), variables);
		this.value = this.replaceVariables(parameterNode.getAttribute("value"), variables);
		this.format = this.replaceVariables(parameterNode.getAttribute("format"), variables);
	}

}
