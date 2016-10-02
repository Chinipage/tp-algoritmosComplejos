package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.util.Support;

public class Profile extends ModelNode {

	private LinkedList<Profile> subProfiles = new LinkedList<Profile>();
	private LinkedList<Parameter> parameters = new LinkedList<Parameter>();

	private void addNewProfile(Element profileNode, HashMap<String, String> variables) {
		this.subProfiles.add(new Profile(profileNode, variables));
	}

	private void addNewParameter(Element parameterNode, HashMap<String, String> variables) {

		this.parameters.add(Parameter.newParameter(parameterNode, variables));

	}

	Profile(Element profileNode, HashMap<String, String> variables){

		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = this.deepCopyVariables(variables);
		declaredVariables.putAll(this.getVariables(profileNode));

		this.initializeAttributes(profileNode, variables);

		this.elementList(profileNode.getElementsByTagName(profileTag)).forEach((subProfileNode) -> {
			this.addNewProfile(subProfileNode, declaredVariables);
		});

		this.elementList(profileNode.getElementsByTagName(parameterTag)).forEach((parameterNode) -> {
			this.addNewParameter(parameterNode, declaredVariables);
		});

	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("args");

		return attributesKeys;
	}

	protected List<String> neccesaryAttributes() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("name");

		return attributesKeys;
	}

	protected Boolean isExecutable() {

		return (Support.allSatisfy(this.neccesaryAttributes(), attribute -> {return attributes.containsKey(attribute);}) &&
				Support.allSatisfy(parameters, parameter -> {return parameter.isExecutable();}));

	}
}
