package xqtr.model;

import org.w3c.dom.Element;

public class Range extends Parameter {

	Range(Element parameterNode) {

		this.name = parameterNode.getAttribute("name");
		this.id = parameterNode.getAttribute("id");
		this.value = parameterNode.getAttribute("value");
	}

}
