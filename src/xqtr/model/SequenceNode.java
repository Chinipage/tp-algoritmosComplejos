package xqtr.model;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

public class SequenceNode extends ParameterNode {

	SequenceNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("type");
		attributesKeys.add("format");

		return attributesKeys;
	}

}
