package xqtr.model;

import java.util.LinkedList;

import org.w3c.dom.Element;

import xqtr.Controller;
import xqtr.util.Support;

public class Program {

	private LinkedList<Profile> profiles = new LinkedList<Profile>();

	public String name;
	private String bin;


	private void addNewProfile(Element profileNode) {
		this.profiles.add(new Profile(profileNode));
	}

	public Program(Element programNode){

		this.name = programNode.getAttribute("name");
		this.bin = programNode.getAttribute("bin");

		Support.elementList(programNode.getElementsByTagName(Controller.profileTag())).forEach((profileNode) -> {
			this.addNewProfile(profileNode);
		});

	}

}
