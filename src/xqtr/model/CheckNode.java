package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.view.CheckView;

public class CheckNode extends ParameterNode {

	CheckNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {

		LinkedList<String> attributesKeys = new LinkedList<String>();

		attributesKeys.add("yvalue");
		attributesKeys.add("nvalue");
		attributesKeys.addAll(super.attributesKeys());

		return attributesKeys;
	}
	
	public CheckView getView() {
		CheckView view = new CheckView(getValue());
		
		view.setValueForNo(getAttribute("nvalue"));
		view.setValueForYes(getAttribute("yvalue"));
		view.setSelected(hasClass("selected"));
		
		return view;
	}
}
