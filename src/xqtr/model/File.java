package xqtr.model;

import org.w3c.dom.Element;

public class File extends Parameter {

	private String format;

	File(Element parameterNode) {
		this.name = parameterNode.getAttribute("name");
		this.id = parameterNode.getAttribute("id");
		this.value = parameterNode.getAttribute("value");
		this.format = parameterNode.getAttribute("format");
	}

}
