package xqtr.model;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

import xqtr.view.FileView;

public class FileNode extends ParameterNode {

	FileNode(Element parameterNode, HashMap<String, String> variables) {
		this.initializeAttributes(parameterNode, variables);
	}

	protected  List<String> attributesKeys() {

		List<String> attributesKeys = super.attributesKeys();

		attributesKeys.add("format");

		return attributesKeys;
	}
	
	public String getFormat() {
		return getAttribute("format");
	}
	
	public FileView getView() {
		FileView view = new FileView();
		
		view.setFormat(getFormat());
		view.setSaveModeEnabled(hasClass("save"));
		view.setMultiModeEnabled(hasClass("multiple"));
		
		return view;
	}

}
