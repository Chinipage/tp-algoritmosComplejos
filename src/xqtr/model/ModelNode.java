package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class ModelNode {

	protected HashMap<String, String> attributes = new HashMap<String, String>();
	protected final String programTag = "program";
	protected final String profileTag = "profile";
	protected final String parameterTag = "parameter";
	protected final String variableTag = "var";

	protected String getAttribute(String key) {
		return attributes.get(key);
	}

	protected void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	protected String replaceVariables(String attribute, HashMap<String, String> variables){

		String replacedAttribute = attribute;

		/*No pude usar el metodo foreach con una expresion lambda ya que no puedo usar dentro
		 * de esa misma espresion las variables locales del metodo que la contiene
		 */
		for(Entry<String, String> e : variables.entrySet()) {

			 String variableName = e.getKey();
			 String variableValue = e.getValue();

			 replacedAttribute = replacedAttribute.replaceAll("\\{" + variableName + "\\}", variableValue);
		}

		return replacedAttribute;

	}

	protected Boolean isValidVariableId(String id) {
		Pattern pattern = Pattern.compile("^[_A-Za-z][A-Z-a-z0-9]+$");
	    return pattern.matcher(id).matches();
	}

	protected HashMap<String, String> getVariables(Element node){

		HashMap<String, String> variables = new HashMap<String, String>();

		this.elementList(node.getElementsByTagName(variableTag)).forEach((variable) -> {
			
			String id = variable.getAttribute("id"), value = variable.getAttribute("value");

			if(this.isValidVariableId(id))
				variables.put(id, this.replaceVariables(value, variables));
		});

		return variables;
	}

	public List<Element> elementList(NodeList list) {

		List<Element> elementList = new LinkedList<Element>();

		for(int i=0; i<list.getLength(); i++) {
			elementList.add((Element) list.item(i));
		}

		return elementList;
	}

	public HashMap<String, String> deepCopyVariables (HashMap<String, String> originalVariables){

		HashMap<String, String> newVariables = new HashMap<String, String>();

		for(Entry<String, String> e : originalVariables.entrySet()) {

			 newVariables.put(e.getKey(), e.getValue());
		}

		return newVariables;
	}

	protected void initializeAttributes(Element node, HashMap<String, String> variables) {

		this.attributesKeys().forEach(attributeKey -> {
			if(node.hasAttribute(attributeKey))
				attributes.put(attributeKey, this.replaceVariables(node.getAttribute(attributeKey), variables));
		});

	}

	protected  List<String> attributesKeys() {

		LinkedList<String> attributesKeys = new LinkedList<String>();

		attributesKeys.add("name");
		attributesKeys.add("class");

		return attributesKeys;
	}

}
