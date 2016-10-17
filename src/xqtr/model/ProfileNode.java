package xqtr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public class ProfileNode extends ModelNode {

	private HashMap<String,String> variables;
	private List<ProfileNode> subProfiles = new LinkedList<ProfileNode>();
	private List<ParameterNode> parameters = new LinkedList<ParameterNode>();
	private ModelNode parent = null;	//Solo deberia tener Profile o Program
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
			logError("Profile " + getAttribute("name") + " is not Executable because " + motivo);
		executable = false;
		subProfiles.forEach(subProfile -> subProfile.setUnexecutable("parent profile is not Executable"));
	}

	public ProfileNode(ModelNode p, Element profileNode, HashMap<String, String> inheritedVariables) {

		HashMap<String, String> declaredVariables;
		parent = p;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = deepCopyVariables(inheritedVariables);
		declaredVariables.putAll(getVariables(profileNode));
		variables = declaredVariables;

		initializeAttributes(profileNode, inheritedVariables);

		parameterTags.forEach(parameterTag -> {
			getChildNodesWithTag(profileNode, parameterTag).forEach((parameterNode) -> {
				addNewParameter(parameterNode, declaredVariables);
			});
		});

		getChildNodesWithTag(profileNode, profileTag).forEach((subProfileNode) -> {
			addNewProfile(subProfileNode, declaredVariables);
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

	protected List<String> necessaryAttributes() {

		List<String> attributesKeys = new ArrayList<>();

		attributesKeys.add("name");

		return attributesKeys;
	}

	protected Boolean isExecutable() {
		return executable;
	}

	protected ProgramNode program() {
		return parent.program();
	}

	private String defaultProfileName() {
		return program().defaultProfileName();
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
		if(!necessaryAttributes().stream().allMatch(attribute -> attributes.containsKey(attribute)))
			setUnexecutable("does not have all the necessary attributes (" + necessaryAttributes().toString() + ").");
	}

	protected void checkParametersConsistency() {
		if(!parameters.stream().allMatch(parameter -> parameter.isExecutable())) {
			setUnexecutable("a parameter is not Executable");
		}
	}

	protected void checkName() {
		if(!hasAttribute("name"))
			setAttribute("name", defaultProfileName());
	}

	protected void checkConsistency() {
		checkCommandAttribute();
		checkNeccesaryAttributes();
		checkParametersConsistency();
	}

	private Boolean hasClass(String className) {
		return hasAttribute("class") && this.getAttribute("class").contains(className);
	}

	private Boolean isHidden() {
		return hasClass("hidden");
	}

	protected List<String> getProfilesNames() {

		List<String> profiles = new LinkedList<String>();
		
		if(!isHidden()) profiles.add((isExecutable() ? "" : "!") + getAttribute("name"));

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

	protected String getCommand(Map<String, String> arguments) {
		HashMap<String, String> var = deepCopyVariables(variables);
		String tmp = "";
		arguments.forEach((id,value) -> var.put(id, value));
		tmp = parent.getCommand(arguments);
		if(hasAttribute("args"))
			 tmp = tmp + replaceVariables(getAttribute("args"), var);
		return tmp;
	}

	protected List<ParameterNode> getParametersTopDown() {
		List<ParameterNode> param = new LinkedList<>();
		param.addAll(parameters);
		subProfiles.forEach(subProfile -> param.addAll(subProfile.getParametersTopDown()));
		return param;
	}

	protected List<ProfileNode> getAllProfiles() {
		List<ProfileNode> allProfiles = new LinkedList<>();
		allProfiles.add(this);
		subProfiles.forEach(subProfile -> allProfiles.addAll(subProfile.getAllProfiles()));
		return allProfiles;
	}
}
