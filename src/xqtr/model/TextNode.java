package xqtr.model;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.view.TextView;

public class TextNode extends ParameterNode {

	TextNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {
		return super.attributesKeys();
	}
	
	public TextView getView() {
		TextView view = new TextView();
		
		view.setConcealed(hasClass("concealed"));
		
		return view;
	}
}
