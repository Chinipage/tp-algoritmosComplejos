package xqtr.model;

import org.w3c.dom.Element;

public abstract class Parameter {

	protected String name;
	protected String id;
	protected String value;

	public static Parameter newParameter(Element parameterNode) {
	
		Parameter newParameter;
		
		switch(parameterNode.getTagName()) {
	
			case "check":
				newParameter = new Check(parameterNode);
				break;
			case "choise":
				newParameter = new Choise(parameterNode);
				break;
			case "color":
				newParameter = new Color(parameterNode);
				break;
			case "file":
				newParameter = new File(parameterNode);
				break;
			case "range":
				newParameter = new Range(parameterNode);
				break;
			case "seq":
				newParameter = new Sequence(parameterNode);
				break;
			case "text":
				newParameter = new Text(parameterNode);
				break;
			default:
				newParameter = new Text(parameterNode);
		}

		return newParameter;
	}

}
