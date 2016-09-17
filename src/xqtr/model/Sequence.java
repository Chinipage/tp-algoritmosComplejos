package xqtr.model;

import org.w3c.dom.Element;

public class Sequence extends Parameter {

	private String type;
	private String format;

	Sequence(Element parameterNode) {

		this.name = parameterNode.getAttribute("name");
		this.id = parameterNode.getAttribute("id");
		this.value = parameterNode.getAttribute("value");
		this.type = parameterNode.getAttribute("type");
		this.format = parameterNode.getAttribute("format");
	}

}
