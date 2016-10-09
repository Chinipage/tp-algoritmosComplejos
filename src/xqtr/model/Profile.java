package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class Profile extends ModelNode {

	private LinkedList<Profile> subProfiles = new LinkedList<Profile>();
	private LinkedList<Parameter> parameters = new LinkedList<Parameter>();
	private ModelNode parent;	//Solo deberia tener Profile o Program

	private void addNewProfile(Element profileNode, HashMap<String, String> variables) {
		this.subProfiles.add(new Profile(this, profileNode, variables));
	}

	private void addNewParameter(Element parameterNode, HashMap<String, String> variables) {

		this.parameters.add(Parameter.newParameter(parameterNode, variables));

	}

	protected List<Parameter> getParameters() {
		return parameters;
	}

	Profile(ModelNode p, Element profileNode, HashMap<String, String> variables){

		HashMap<String, String> declaredVariables;
		parent = p;

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

		return this.neccesaryAttributes().stream().allMatch(attribute -> attributes.containsKey(attribute)) &&
				parameters.stream().allMatch(parameter -> parameter.isExecutable());

	}

	private Boolean hasClass(String className) {
		return this.hasAttribute("class") || this.getAttribute("class").contains(className);
	}

	private Boolean isHidden() {
		return this.hasClass("hidden");
	}

	protected List<String> getExecutableProfilesNames() {

		List<String> profiles = new LinkedList<String>();

		if(!this.isHidden()) profiles.add(this.getAttribute("name"));

		subProfiles.forEach(profile -> {
			if(profile.isExecutable())
				profiles.addAll(profile.getExecutableProfilesNames());	
		});

		return profiles;
	}

	protected Profile getProfile(String profileName) {

		Profile searchedProfile;

		if(this.getAttribute("name") == profileName)
			return this;

		for(Profile prof : subProfiles)
			if((searchedProfile = prof.getProfile(profileName)) != null)
				return searchedProfile;

		return null;
	}

	protected String getCommand() {
		return parent.getCommand() + this.getAttribute("args");
	}
}
