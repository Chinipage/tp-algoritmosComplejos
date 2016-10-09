package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class Program extends ModelNode {

	private LinkedList<Profile> profiles = new LinkedList<Profile>();

	private void addNewProfile(Element profileNode, HashMap<String, String> declaredVaraibles) {
		this.profiles.add(new Profile(this, profileNode, declaredVaraibles));
	}

	public Program(Element programNode, HashMap<String, String> variables){

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

	protected List<String> getExecutableProfilesNames() {

		List<String> profilesNames = new LinkedList<String>();

		profiles.forEach(profile ->	{
			if(profile.isExecutable())
				profilesNames.addAll(profile.getExecutableProfilesNames());	
		});

		return profilesNames;
	}

	protected Profile getProfile(String profileName) {

		Profile searchedProfile;

		for(Profile prof : profiles)
			if((searchedProfile = prof.getProfile(profileName)) != null)
				return searchedProfile;

		return null;
	}

	protected String getCommand() {
		return this.getAttribute("bin");
	}
}
