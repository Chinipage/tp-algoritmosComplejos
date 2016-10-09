package xqtr.model;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

public abstract class Parameter extends ModelNode {

	public static Parameter newParameter(Element parameterNode, HashMap<String, String> variables) {
	
		Parameter newParameter;
		
		switch(parameterNode.getTagName()) {
	
			case "check":
				newParameter = new Check(parameterNode, variables);
				break;
			case "choice":
				newParameter = new Choice(parameterNode, variables);
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

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("id");
		attributesKeys.add("value");

		return attributesKeys;
	}

	protected List<String> neccesaryAttributes() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("id");
		attributesKeys.add("value");

		return attributesKeys;
	}

	protected Boolean isExecutable() {

		return this.neccesaryAttributes().stream().allMatch(attribute -> attributes.containsKey(attribute));
	}

	//Interfaz para la vista---------------------------------------------------------------------
	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public Boolean hasAttribute(String attributeName) {
		return attributes.containsKey(attributeName);
	}

	public Boolean hasClass(String className) {
		return this.hasAttribute("class") || this.getAttribute("class").contains(className);
	}

}
