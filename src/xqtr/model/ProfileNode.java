package xqtr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class ProfileNode extends ModelNode {

	private static Integer profileCounter = 1;
	private HashMap<String,String> variables;
	private List<ProfileNode> subProfiles = new LinkedList<ProfileNode>();
	private List<ParameterNode> parameters = new LinkedList<ParameterNode>();
	private ModelNode parent;	//Solo deberia tener Profile o Program
	private Boolean executable = true;

	private void addNewProfile(Element profileNode, HashMap<String, String> variables) {
		this.subProfiles.add(new ProfileNode(this, profileNode, variables));
	}

	private void addNewParameter(Element parameterNode, HashMap<String, String> variables) {
		this.parameters.add(ParameterNode.newParameter(parameterNode, variables));
	}

	protected List<ParameterNode> getParameters() {
		List<ParameterNode> params = parent.getParameters();
		params.addAll(parameters);
		return params;
	}

	protected void setUnexecutable(String motivo) {
		if(executable)
			this.logError("Profile " + this.getAttribute("name") + " is not Executable because " + motivo);
		executable = false;
		subProfiles.forEach(subProfile -> subProfile.setUnexecutable("parent profile is not Executable"));
	}

	ProfileNode(ModelNode p, Element profileNode, HashMap<String, String> inheritedVariables){

		HashMap<String, String> declaredVariables;
		parent = p;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = this.deepCopyVariables(inheritedVariables);
		declaredVariables.putAll(this.getVariables(profileNode));
		variables = declaredVariables;

		initializeAttributes(profileNode, inheritedVariables);

		parameterTags.forEach(parameterTag -> {
			getChildNodesWithTag(profileNode, parameterTag).forEach((parameterNode) -> {
				this.addNewParameter(parameterNode, declaredVariables);
			});
		});

		getChildNodesWithTag(profileNode, profileTag).forEach((subProfileNode) -> {
			this.addNewProfile(subProfileNode, declaredVariables);
		});

		checkConsistency();

	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("args");

		return attributesKeys;
	}

	protected String commandVariable() {
		return "args";
	}

	protected List<String> neccesaryAttributes() {

		List<String> attributesKeys = new ArrayList<>();

		attributesKeys.add("name");

		return attributesKeys;
	}

	protected Boolean isExecutable() {
		return executable;
	}

	private String defaultProfileName() {
		String defaultProfileName = "Default" + profileCounter.toString();
		profileCounter = profileCounter + 1;
		return defaultProfileName;
	}

	protected List<String> getArgumentIds() {
		List<String> arguments = new LinkedList<>();
		getParameters().forEach(parameter -> arguments.add(parameter.getAttribute("id")));
		return arguments;
	}

	protected void checkCommandAttribute() {
		List<String> var = getArgumentIds();
		variables.forEach((id,value) -> var.add(id));
		if(hasAttribute(commandVariable())) {
			preProcessVaraibles(getAttribute(commandVariable()), var);
		}
	}

	protected void checkNeccesaryAttributes() {
		if(!this.neccesaryAttributes().stream().allMatch(attribute -> attributes.containsKey(attribute)))
			this.setUnexecutable("does not have all the necessary attributes (" + this.neccesaryAttributes().toString() + ").");
	}

	protected void checkParametersConsistency() {
		if(!parameters.stream().allMatch(parameter -> parameter.isExecutable())) {
			setUnexecutable("a parameter is not Executable");
		}
	}

	protected void checkName() {
		if(!this.hasAttribute("name"))
			this.setAttribute("name", defaultProfileName());
	}

	protected void checkConsistency() {
		checkCommandAttribute();
		checkNeccesaryAttributes();
		checkParametersConsistency();
	}

	private Boolean hasClass(String className) {
		return this.hasAttribute("class") && this.getAttribute("class").contains(className);
	}

	private Boolean isHidden() {
		return this.hasClass("hidden");
	}

	protected List<String> getProfilesNames() {

		List<String> profiles = new LinkedList<String>();
		
		// TODO
		if(!this.isHidden()) profiles.add((this.isExecutable() ? "" : "!") + this.getAttribute("name"));

		subProfiles.forEach(profile -> profiles.addAll(profile.getProfilesNames()));

		return profiles;
	}

	protected ProfileNode getProfile(String profileName) {

		ProfileNode searchedProfile;

		if(getAttribute("name") != null && getAttribute("name").equals(profileName))
			return this;

		for(ProfileNode prof : subProfiles)
			if((searchedProfile = prof.getProfile(profileName)) != null)
				return searchedProfile;

		return null;
	}

	protected String getCommand(HashMap<String, String> arguments) {
		HashMap<String, String> var = deepCopyVariables(variables);
		arguments.forEach((id,value) -> var.put(id, value));
		return parent.getCommand(arguments) + replaceVariables(getAttribute("args"), var);
	}

	protected List<ParameterNode> getParametersTopDown() {
		List<ParameterNode> param = new LinkedList<>();
		param.addAll(parameters);
		subProfiles.forEach(subProfile -> param.addAll(subProfile.getParametersTopDown()));
		return param;
	}
}
