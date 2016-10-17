package xqtr.model;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xqtr.Application;
import xqtr.util.Support;

public abstract class ModelNode {

	private static FileWriter fw; //No se si hace falta tenerlo en una variable para que no se lo lleve el gc.
	private static PrintWriter errorPw = null;
	protected HashMap<String, String> attributes = new HashMap<String, String>();

	protected static final String programTag = "program";
	protected static final String profileTag = "profile";
	protected static final List<String> parameterTags = 
			Support.listFromString("file, seq, range, text, choicegroup, check");
	protected static final String choiceTag = "choice";
	protected static final String variableTag = "var";
	protected static final String xmlVersion = "1.0";

	protected String getAttribute(String key) {
		return attributes.get(key);
	}

	protected void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	protected Boolean hasAttribute(String attributeName) {
		return attributes.containsKey(attributeName);
	}

	protected Boolean openErrorLogFile() {
		
		File errorLogFile = new File(Application.properties.get("error.log.path"));

		try {
			fw = new FileWriter(errorLogFile, true); //True para usar append
			errorPw = new PrintWriter(fw);
		} catch (Exception e) {
			errorPw = null;
			return false;
		}

		return true;
	}

	protected void closeErrorLogFile() {
		if(errorPw != null) errorPw.close();
	}

	protected void logError(String error) {
		if(errorPw != null) {
			errorPw.println(LocalDateTime.now().toString() + " " + error);
		}
	}

	protected void preProcessVaraibles(String string, HashMap<String, String> variableIds) {
		processVariables(string, variableIds, false);
	}

	protected void preProcessVaraibles(String string, List<String> variableIds) {
		HashMap<String, String> mapedVariables = new HashMap<>();
		variableIds.forEach(id -> mapedVariables.put(id, ""));
		preProcessVaraibles(string, mapedVariables);
	}

	protected String replaceVariables(String string, HashMap<String, String> variables) {
		return processVariables(string, variables, true);
	}

	protected String processVariables(String string, HashMap<String, String> variables, Boolean replace){

		StringBuffer replacedString = new StringBuffer();
		Pattern variablePattern = Pattern.compile("(?:\\{)([^}]*)(?:\\})"),
				idPattern = Pattern.compile("([A-Za-z_][A-Za-z_0-9]*)"),
				equationPattern = Pattern.compile("^([0-9]+\\s*[-+*/]\\s*[0-9]+(\\s*[-+*/]\\s*[0-9]+)*)$");
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
		Matcher variableMatcher = variablePattern.matcher(string); 

		while(variableMatcher.find()) {

			StringBuffer resultBuffer = new StringBuffer();
			String resultReplacement;
			Matcher idMatcher = idPattern.matcher(variableMatcher.group(1)), equationMatcher;

			//Reemplazo las variables con los ids que tenga en el HashMap.
			while(idMatcher.find()) {
				if(variables.containsKey(idMatcher.group(1)))
						idMatcher.appendReplacement(resultBuffer, variables.get(idMatcher.group(1)));
				else {
					setUnexecutable("the variable " + idMatcher.group(1) + " could not be replaced");
					idMatcher.appendReplacement(resultBuffer, "");
				}
			}

			idMatcher.appendTail(resultBuffer);
			resultReplacement = resultBuffer.toString();

			//Si lo que quedo es una ecuacion, resuelvo la ecuacion.
			equationMatcher = equationPattern.matcher(resultBuffer);
			if(equationMatcher.matches())
				try {
					resultReplacement = engine.eval(resultReplacement).toString();
				} catch (ScriptException e1) {
					e1.printStackTrace();
					this.setUnexecutable(e1.getMessage());
				}

			variableMatcher.appendReplacement(replacedString, resultReplacement);
		}

		variableMatcher.appendTail(replacedString);

		return replace ? replacedString.toString() : string;
	}

	protected Boolean isValidVariableId(String id) {
		Pattern pattern = Pattern.compile("^([A-Za-z_][A-Za-z_0-9]*)$");
	    return pattern.matcher(id).matches();
	}

	protected HashMap<String, String> getVariables(Element node){

		HashMap<String, String> variables = new HashMap<String, String>();

		this.elementList(node.getElementsByTagName(variableTag)).forEach((variable) -> {
			
			String id = variable.getAttribute("id"),
				value = variable.getAttribute("value");

			if(this.isValidVariableId(id))
				variables.put(id, value);
			else
				this.logError("Invalid variable Id: " + id);
		});

		return variables;
	}

	protected List<Element> elementList(NodeList list) {

		List<Element> elementList = new LinkedList<Element>();

		for(int i=0; i<list.getLength(); i++) {
			elementList.add((Element) list.item(i));
		}

		return elementList;
	}

	protected HashMap<String, String> deepCopyVariables (HashMap<String, String> originalVariables){

		HashMap<String, String> newVariables = new HashMap<String, String>();

		for(Entry<String, String> e : originalVariables.entrySet()) {

			 newVariables.put(e.getKey(), e.getValue());
		}

		return newVariables;
	}

	protected void loadAttributes(Element node) {
		attributesKeys().forEach(attributeKey -> {
			if(node.hasAttribute(attributeKey))
				attributes.put(attributeKey, node.getAttribute(attributeKey));
		});
		checkName();
	}

	protected void checkName() {
		//No hace nada
	}

	protected void replaceAttributes(HashMap<String, String> variables) {
		attributes.forEach((id,value) -> {
			if(!id.equals(commandVariable()))
				setAttribute(id, replaceVariables(value, variables));
		});
	}

	protected void initializeAttributes(Element node, HashMap<String, String> variables) {
		loadAttributes(node);
		replaceAttributes(variables);
	}

	protected  List<String> attributesKeys() {

		LinkedList<String> attributesKeys = new LinkedList<String>();

		attributesKeys.add("name");
		attributesKeys.add("class");

		return attributesKeys;
	}

	protected String commandVariable() {
		return "";
	}

	protected List<String> configurationAttributes() {
		List<String> attributes = attributesKeys();
		attributes.remove(commandVariable());
		return attributes;
	}

	protected String getCommand(Map<String, String> args) {
		//Este metodo se redefine en RootNode, Program y Profile. No se usa en Parameter.
		return "";
	}

	//Por polimorfismo para el reemplazo de variables
	protected void setUnexecutable(String motivo) {
	}
	
	protected List<ParameterNode> getParameters() {
		return new LinkedList<>();
	}

	protected List<Element> getChildNodesWithTag(Element element, String tag) {
		List<Element> childNodes = new LinkedList<>();
		elementList(element.getElementsByTagName(tag)).forEach(child -> {
			if(child.getParentNode().isEqualNode(element))
				childNodes.add(child);
		});
		return childNodes;
	}
}
