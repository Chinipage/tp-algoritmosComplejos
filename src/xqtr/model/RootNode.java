package xqtr.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	private Integer programCounter = 1;

	public RootNode() {

		HashMap<String, String> variables;

		openErrorLogFile();

		try {
			Element rootElement = parseConfigXML();
			
			variables = getVariables(rootElement);

			getChildNodesWithTag(rootElement, programTag).forEach(programNode -> {
				addNewProgram(programNode, variables);
			});

			programs.forEach(program -> {
				List<ProgramNode> programsFiltered = new LinkedList<>();

				programs.forEach(program1 -> {
					if(program1.getAttribute("name").equals(program.getAttribute("name")))
						programsFiltered.add(program);
				});
				if(programsFiltered.size() > 1) {
					programsFiltered.forEach(programFiltered -> programFiltered.setUnexecutable("there is more than one program with this name"));
				}
			});
		}finally {
			closeErrorLogFile();
		}
	}
	
	private Element parseConfigXML() {
		String configFileName = Application.properties.get("config.file.path");
		InputStream configFile = Support.loadResource(configFileName);
		
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
		programs.add(new ProgramNode(this, programNode, variables));
	}

	private ProgramNode getProgram(String programName) {
		return programs.stream().filter(program -> program.getAttribute("name").equals(programName)).findFirst().orElse(null);
	}

	public List<String> getProgramsNames() {

		List<String> programsList = new LinkedList<String>();
		
		programs.forEach(program -> {
			programsList.add((program.isExecutable() ? "" : "!") + program.getAttribute("name"));
		});

		return programsList;
	}

	public List<String> getProfilesNames(String programName) {

		List<String> executableProfilesNames = new LinkedList<String>();
		ProgramNode program = getProgram(programName);

		if((program != null))
			executableProfilesNames.addAll(program.getProfilesNames());
		
		return executableProfilesNames;
	}

	protected ProfileNode getProfile(String programName, String profileName) {

		ProgramNode program;
		ProfileNode profile = null;

		if((program = getProgram(programName)) != null)
			profile = program.getProfile(profileName);

		return profile;
		
	}

	public List<ParameterNode> getParameters(String programName, String profileName) {

		ProfileNode profile;
		List<ParameterNode> parameters = new LinkedList<ParameterNode>();
		
		if((profile = getProfile(programName, profileName)) != null)
			parameters.addAll(profile.getParameters());

		return parameters;
	}

	public String getCommand(String programName, String profileName, Map<String, String> arguments) {
		
		String command = null;
		ProfileNode profile;

		if((profile = getProfile(programName, profileName)) != null) {
			command = profile.getCommand(arguments);
		}

		return command; 
	}

	protected String defaultProgramName() {
		String defaultProfileName = "Default" + programCounter.toString();
		programCounter = programCounter + 1;
		return defaultProfileName;
	}
}
