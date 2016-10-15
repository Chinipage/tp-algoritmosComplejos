package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class ProgramNode extends ModelNode {

	private static Integer programCounter = 1;
	private HashMap<String, String> variables;
	private LinkedList<ProfileNode> profiles = new LinkedList<ProfileNode>();
	private Boolean executable = true;

	private void addNewProfile(Element profileNode, HashMap<String, String> declaredVaraibles) {
		this.profiles.add(new ProfileNode(this, profileNode, declaredVaraibles));
	}

	protected void setUnexecutable(String motivo) {
		if(executable)
			logError("Program " + getAttribute("name") + " is not Executable because " + motivo);
		executable = false;
	}

	public ProgramNode(Element programNode, HashMap<String, String> inheritedVariables){

		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = this.deepCopyVariables(inheritedVariables);
		declaredVariables.putAll(this.getVariables(programNode));
		variables = declaredVariables;
	
		this.initializeAttributes(programNode, inheritedVariables);		
		getChildNodesWithTag(programNode, profileTag).forEach((profileNode) -> {
			this.addNewProfile(profileNode, declaredVariables);
		});

		checkConsistency();
	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("bin");

		return attributesKeys;
	}

	protected String commandVariable() {
		return "bin";
	}

	protected List<String> necessaryAttributes() {

		List<String> attributesKeys = new LinkedList<>();

		attributesKeys.add("bin");

		return attributesKeys;
	}

	protected List<String> getProfilesNames() {

		List<String> profilesNames = new LinkedList<String>();

		profiles.forEach(profile ->	profilesNames.addAll(profile.getProfilesNames()));

		return profilesNames;
	}

	protected ProfileNode getProfile(String profileName) {

		ProfileNode searchedProfile;

		for(ProfileNode prof : profiles)
			if((searchedProfile = prof.getProfile(profileName)) != null)
				return searchedProfile;

		return null;
	}

	protected String getCommand(HashMap<String, String> arguments) {
		HashMap<String, String> var = deepCopyVariables(variables);
		arguments.forEach((id,value) -> var.put(id, value));
		return replaceVariables(getAttribute("bin"), var);
	}

	protected void checkName() {
		if(!this.hasAttribute("name"))
			this.setAttribute("name", defaultProgramName());
	}

	private String defaultProgramName() {
		String defaultProfileName = "Default" + programCounter.toString();
		programCounter = programCounter + 1;
		return defaultProfileName;
	}

	protected List<ParameterNode> getParametersTopDown() {
		List<ParameterNode> parameters = new LinkedList<>();
		profiles.forEach(profile -> parameters.addAll(profile.getParametersTopDown()));
		return parameters;
	}

	protected List<String> getArgumentIds() {
		List<String> arguments = new LinkedList<>();
		getParametersTopDown().forEach(parameter -> arguments.add(parameter.getAttribute("id")));
		return arguments;
	}

	protected void checkCommandAttribute() {
		List<String> var = getArgumentIds();
		variables.forEach((id,value) -> var.add(id));
		preProcessVaraibles(getAttribute(commandVariable()), var);
	}

	protected void checkNeccesaryAttributes() {
		if(!necessaryAttributes().stream().allMatch(attribute -> attributes.containsKey(attribute)))
			setUnexecutable("does not have all the necessary attributes (" + necessaryAttributes().toString() + ").");
	}

	protected void checkConsistency() {
		checkCommandAttribute();
		checkNeccesaryAttributes();
	}

	protected Boolean isExecutable() {
		return executable;
	}
}
