package xqtr.model;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

public class TextNode extends ParameterNode {

	TextNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {
		return super.attributesKeys();
	}
}
