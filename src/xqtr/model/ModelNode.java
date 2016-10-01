package xqtr.model;

import java.util.HashMap;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import xqtr.util.Support;

public abstract class ModelNode {
	
	protected String name;

	/*Config XML tags*/
	protected String programTag(){
		return "program";
	}

	protected String profileTag(){
		return "profile";
	}

	protected String parameterTag(){
		return "parameter";
	}

	protected String variableTag(){
		return "var";
	}

	/*Variables management (quizas esto deberia estar en support, no se)*/
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

	protected HashMap<String, String> getVariables(Element node){

		HashMap<String, String> variables = new HashMap<String, String>();

		Support.elementList(node.getElementsByTagName(this.variableTag())).forEach((globalVariable) -> {
			variables.put(globalVariable.getAttribute("id"), globalVariable.getAttribute("value"));
		});

		System.out.println("Node variables of " + node.getNodeName());
		variables.forEach((id, value) -> {
			System.out.println("id: " + id + " value: " + value);
		});

		return variables;
	}

}
