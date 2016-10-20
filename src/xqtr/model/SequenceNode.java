package xqtr.model;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.util.Support;
import xqtr.view.SequenceView;

public class SequenceNode extends ParameterNode {

	SequenceNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("type");
		attributesKeys.add("min");
		attributesKeys.add("max");
		attributesKeys.add("step");

		return attributesKeys;
	}
	
	public String getType() {
		return getAttribute("type");
	}
	
	public Double getMinimum() {
		return Support.doubleFromString(getAttribute("min"));
	}
	
	public Double getMaximum() {
		return Support.doubleFromString(getAttribute("max"));
	}
	
	public Double getStep() {
		return Support.doubleFromString(getAttribute("step"));
	}

	public SequenceView getView() {
		SequenceView view = new SequenceView(getType());
		
		view.setMinimum(getMinimum());
		view.setMaximum(getMaximum());
		view.setStep(getStep());
		
		return view;
	}
}
