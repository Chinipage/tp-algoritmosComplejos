package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

public class ChoiceGroupNode extends ParameterNode {

	private List<ChoiceNode> choiceNodes = new LinkedList<>();

	protected void addNewChoise(Element choiseNode, HashMap<String, String> variables) {
		choiceNodes.add(new ChoiceNode(choiseNode, variables));
	}

	public ChoiceGroupNode(Element choiseGroupNode, HashMap<String, String> inheritedVariables) {
		
		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = deepCopyVariables(inheritedVariables);
		declaredVariables.putAll(getVariables(choiseGroupNode));

		initializeAttributes(choiseGroupNode, inheritedVariables);

		getChildNodesWithTag(choiseGroupNode, choiseTag).forEach((parameterNode) -> {
			addNewChoise(parameterNode, declaredVariables);
		});
	}

	//Interfaz-------------------------------------------------------------------------
	
	public List<ChoiceNode> getChoices() {
		return choiceNodes;
	}
}
