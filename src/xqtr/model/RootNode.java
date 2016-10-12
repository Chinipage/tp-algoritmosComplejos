package xqtr.model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import xqtr.Application;
import xqtr.util.Support;

public class RootNode extends ModelNode {

	private LinkedList<ProgramNode> programs = new LinkedList<ProgramNode>();

	public RootNode() {

		HashMap<String, String> variables;

		openErrorLogFile();

		Element rootElement = parseConfigXML();
		
		variables = this.getVariables(rootElement);

		this.elementList(rootElement.getElementsByTagName(programTag)).forEach(programNode -> {
			this.addNewProgram(programNode, variables);
		});

		this.closeErrorLogFile();
	}
	
	private Element parseConfigXML() {
		String configFileName = Application.properties.get("config.file.path");
		File configFile = Support.loadResource(configFileName);
		
		if(!Support.getFileExtension(configFile).equals("xml")) {
			Support.displayMessage("Critical Error: Configuration file must be an XML");
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ErrorHandler() { 
				public void warning(SAXParseException exception) throws SAXException {
					Support.displayMessage("Warning: " + getMessage(exception));
				}
				
				public void fatalError(SAXParseException exception) throws SAXException {
					Support.displayMessage("Critical Error: " + getMessage(exception));
				}
				
				public void error(SAXParseException exception) throws SAXException {
					Support.displayMessage("Error: " + getMessage(exception));
				}
				private String getMessage(SAXParseException ex) {
					String m = "There was a problem while reading config parameters.\n";
					m += String.format("[%s:%d:%d] ", configFile, ex.getLineNumber(), ex.getColumnNumber());
					m += ex.getMessage();
					return m;
				}
			});
			Document document = builder.parse(configFile);
			Element root = document.getDocumentElement();
			root.normalize();
			return root;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addNewProgram(Element programNode, HashMap<String, String> variables){

		this.programs.add(new ProgramNode(programNode, variables));
	}

	private ProgramNode getProgram(String programName) {
		return programs.stream().filter(program -> program.getAttribute("name").equals(programName)).findFirst().orElse(null);
	}

	public List<String> getProgramsNames() {

		List<String> programsList = new LinkedList<String>();
		
		programs.forEach(program -> {
			programsList.add(program.getAttribute("name"));
		});

		return programsList;
	}

	public List<String> getProfilesNames(String programName) {

		List<String> executableProfilesNames = new LinkedList<String>();
		ProgramNode program = this.getProgram(programName);

		if((program != null))
			executableProfilesNames.addAll(program.getProfilesNames());
		
		return executableProfilesNames;
	}

	protected ProfileNode getProfile(String programName, String profileName) {

		ProgramNode program;
		ProfileNode profile = null;

		if((program = this.getProgram(programName)) != null)
			profile = program.getProfile(profileName);

		return profile;
		
	}

	public List<ParameterNode> getParameters(String programName, String profileName) {

		ProfileNode profile;
		List<ParameterNode> parameters = new LinkedList<ParameterNode>();

		if((profile = this.getProfile(programName, profileName)) != null)
			parameters.addAll(profile.getParameters());

		return parameters;
	}

	public String getCommand(String programName, String profileName, HashMap<String, String> arguments) {
		
		String command = null;
		ProfileNode profile;

		if((profile = this.getProfile(programName, profileName)) != null)
				command = this.replaceVariables(profile.getCommand(), arguments);

		return command; 
	}

}
