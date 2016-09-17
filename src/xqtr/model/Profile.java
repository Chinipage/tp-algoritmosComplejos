package xqtr.model;

import java.util.LinkedList;

import org.w3c.dom.Element;

import xqtr.util.Support;

public class Profile {

	private String name;
	private String args = "";

	private LinkedList<Profile> subProfiles = new LinkedList<Profile>();
	private LinkedList<Parameter> parameters = new LinkedList<Parameter>();

	private void addNewProfile(Element profileNode) {
		this.subProfiles.add(new Profile(profileNode));
	}

	private void addNewParameter(Element parameterNode) {

		this.parameters.add(Parameter.newParameter(parameterNode));

	}

	Profile(Element profileNode){

		this.name = profileNode.getAttribute("name");
		
		if(profileNode.hasAttribute("args")) {
			this.args = profileNode.getAttribute("args");
		}

		Support.elementList(profileNode.getElementsByTagName(Controller.profileTag())).forEach((subProfileNode) -> {
			this.addNewProfile(subProfileNode);
		});

		Support.elementList(profileNode.getElementsByTagName(Controller.parameterTag())).forEach((parameterNode) -> {
			this.addNewParameter(parameterNode);
		});

	}
}
