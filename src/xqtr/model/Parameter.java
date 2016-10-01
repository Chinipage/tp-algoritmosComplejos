package xqtr.model;

import java.util.HashMap;

import org.w3c.dom.Element;

public abstract class Parameter extends ModelNode {

	protected String id;
	protected String value;

	public static Parameter newParameter(Element parameterNode, HashMap<String, String> variables) {
	
		Parameter newParameter;
		
		switch(parameterNode.getTagName()) {
	
			case "check":
				newParameter = new Check(parameterNode, variables);
				break;
			case "choise":
				newParameter = new Choice(parameterNode, variables);
				break;
			case "color":
				newParameter = new Color(parameterNode, variables);
				break;
			case "file":
				newParameter = new File(parameterNode, variables);
				break;
			case "range":
				newParameter = new Range(parameterNode, variables);
				break;
			case "seq":
				newParameter = new Sequence(parameterNode, variables);
				break;
			case "text":
				newParameter = new Text(parameterNode, variables);
				break;
			default:
				newParameter = new Text(parameterNode, variables);
		}

		return newParameter;
	}

}
