package xqtr.ctrl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import xqtr.util.ComboBox;
import xqtr.util.RadioGroup;

@SuppressWarnings("serial")
public class Choice extends Control {

	private LinkedHashMap<String, String> model;
	private ComboBox comboBox;
	private RadioGroup radioGroup;
	
	public Choice(LinkedHashMap<String, String> model) {
		
		this.model = model;
		renderControl();
	}
	
	private void renderControl() {
		
		if(model.size() > 5) {
			comboBox = new ComboBox(new ArrayList<>(model.keySet()));
			add(comboBox);
		} else {
			radioGroup = new RadioGroup(new ArrayList<>(model.keySet()));
			add(radioGroup);
		}
	}
}
