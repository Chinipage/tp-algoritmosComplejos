package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;

import xqtr.Controller;
import xqtr.util.Support;

public class Program {

	private LinkedList<Profile> profiles = new LinkedList<Profile>();

	private String name;
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

		Controller controller = Controller.getInstance();
		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = Support.deepCopyVariables(variables);
		declaredVariables.putAll(controller.getVariables(programNode));

		this.name = controller.replaceVariables(programNode.getAttribute("name"), variables);
		this.bin = controller.replaceVariables(programNode.getAttribute("bin"), variables);

		Support.elementList(programNode.getElementsByTagName(Controller.getInstance().profileTag())).forEach((profileNode) -> {
			this.addNewProfile(profileNode, declaredVariables);
		});

	}

}
