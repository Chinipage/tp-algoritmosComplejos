package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class ProgramNode extends ModelNode {

	private LinkedList<ProfileNode> profiles = new LinkedList<ProfileNode>();

	private void addNewProfile(Element profileNode, HashMap<String, String> declaredVaraibles) {
		this.profiles.add(new ProfileNode(this, profileNode, declaredVaraibles));
	}

	public ProgramNode(Element programNode, HashMap<String, String> variables){

		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = this.deepCopyVariables(variables);
		declaredVariables.putAll(this.getVariables(programNode));
	
		this.initializeAttributes(programNode, variables);

		this.elementList(programNode.getElementsByTagName(profileTag)).forEach((profileNode) -> {
			this.addNewProfile(profileNode, declaredVariables);
		});

	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("bin");

		return attributesKeys;
	}

	protected List<String> neccesaryAttributes() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("name");
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

	protected String getCommand() {
		return this.getAttribute("bin");
	}

}
