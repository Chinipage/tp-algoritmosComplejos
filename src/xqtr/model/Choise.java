package xqtr.model;

import org.w3c.dom.Element;

public class Choise extends Parameter {

	Choise(Element parameterNode) {

		this.name = parameterNode.getAttribute("name");
		this.id = parameterNode.getAttribute("id");
		this.value = parameterNode.getAttribute("value");
	}
}
