package xqtr.model;

import java.util.LinkedList;

import org.w3c.dom.Element;

import xqtr.util.Support;

public class Program {

	private LinkedList<Profile> profiles = new LinkedList<Profile>();

	protected String name;
	private String bin;


	private void addNewProfile(Element profileNode) {
		this.profiles.add(new Profile(profileNode));
	}

	Program(Element programNode){

		this.name = programNode.getAttribute("name");
		this.bin = programNode.getAttribute("bin");

		Support.elementList(programNode.getElementsByTagName(Controller.profileTag())).forEach((profileNode) -> {
			this.addNewProfile(profileNode);
		});

	}

}
