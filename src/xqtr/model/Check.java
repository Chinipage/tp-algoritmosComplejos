package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class Check extends Parameter {

	Check(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {

		LinkedList<String> attributesKeys = new LinkedList<String>();

		attributesKeys.add("yvalue");
		attributesKeys.add("nvalue");
		attributesKeys.addAll(super.attributesKeys());

		return attributesKeys;
	}
}
