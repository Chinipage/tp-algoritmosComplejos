package xqtr.model;

import org.w3c.dom.Element;

public class Text extends Parameter {

	private String format;

	Text(Element parameterNode) {

		this.name = parameterNode.getAttribute("name");
		this.id = parameterNode.getAttribute("id");
		this.value = parameterNode.getAttribute("value");
		this.format = parameterNode.getAttribute("format");
	}
}
