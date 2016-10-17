package xqtr.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import xqtr.view.ChoiceView;

public class ChoiceGroupNode extends ParameterNode {

	private List<ChoiceNode> choiceNodes = new LinkedList<>();

	protected void addNewChoice(Element choiceNode, HashMap<String, String> variables) {
		choiceNodes.add(new ChoiceNode(choiceNode, variables));
	}

	public ChoiceGroupNode(Element choiceGroupNode, HashMap<String, String> inheritedVariables) {
		
		HashMap<String, String> declaredVariables;

		/*Genero el nuevo diccionario de variables para los perfiles*/
		declaredVariables = deepCopyVariables(inheritedVariables);
		declaredVariables.putAll(getVariables(choiceGroupNode));

		initializeAttributes(choiceGroupNode, inheritedVariables);

		getChildNodesWithTag(choiceGroupNode, choiceTag).forEach((parameterNode) -> {
			addNewChoice(parameterNode, declaredVariables);
		});
	}

	//Interfaz-------------------------------------------------------------------------
	
	public List<ChoiceNode> getChoices() {
		return choiceNodes;
	}
	
	public ChoiceView getView() {
		
		Map<String, String> model = new HashMap<>();
		
		choiceNodes.forEach(choice -> {
			model.put(choice.getName(), choice.getValue());
		});
		
		return new ChoiceView(model);
	}
}
