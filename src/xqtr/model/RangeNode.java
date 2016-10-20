package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.util.Support;
import xqtr.view.RangeView;

public class RangeNode extends ParameterNode {

	RangeNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {
		LinkedList<String> attributesKeys = new LinkedList<String>();

		attributesKeys.add("min");
		attributesKeys.add("max");
		attributesKeys.add("step");
		attributesKeys.addAll(super.attributesKeys());
		
		return attributesKeys;
	}
	
	public Integer getMinimum() {
		return Support.integerFromString(getAttribute("min"));
	}
	
	public Integer getMaximum() {
		return Support.integerFromString(getAttribute("max"));
	}
	
	public Integer getStep() {
		return Support.integerFromString(getAttribute("step"));
	}
	
	public RangeView getView() {
		RangeView view = new RangeView();
		
		view.setMinimum(getMinimum());
		view.setMaximum(getMaximum());
		view.setStep(getStep());
		
		return view;
	}

}
