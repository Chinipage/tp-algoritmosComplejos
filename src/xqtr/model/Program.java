package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;

import xqtr.util.Support;

public class Program extends ModelNode {

	private LinkedList<Profile> profiles = new LinkedList<Profile>();

	private String bin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void addNewProfile(Element profileNode, HashMap<String, String> declaredVaraibles) {
		this.profiles.add(new Profile(profileNode, declaredVaraibles));
	}

	public Program(Element programNode, HashMap<String, String> variables){

		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = Support.deepCopyVariables(variables);
		declaredVariables.putAll(this.getVariables(programNode));

		this.name = this.replaceVariables(programNode.getAttribute("name"), variables);
		this.bin = this.replaceVariables(programNode.getAttribute("bin"), variables);

		Support.elementList(programNode.getElementsByTagName(this.profileTag())).forEach((profileNode) -> {
			this.addNewProfile(profileNode, declaredVariables);
		});

	}

}
