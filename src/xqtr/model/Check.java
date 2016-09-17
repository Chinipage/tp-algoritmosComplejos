package xqtr.model;

import org.w3c.dom.Element;

public class Check extends Parameter {

	private String yvalue;
	private String nvalue;

	Check(Element parameterNode) {

		this.name = parameterNode.getAttribute("name");
		this.id = parameterNode.getAttribute("id");
		this.value = parameterNode.getAttribute("value");
		this.yvalue = parameterNode.getAttribute("yvalue");
		this.nvalue = parameterNode.getAttribute("nvalue");
	}
	
}
