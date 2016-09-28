package xqtr.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import xqtr.util.ComboBox;
import xqtr.util.RadioGroup;
import xqtr.util.Support;

@SuppressWarnings("serial")
public class ChoiceBox extends Control {

	private Map<String, String> model;
	private ComboBox comboBox;
	private RadioGroup radioGroup;
	private boolean comboMode;
	
	public ChoiceBox(String model) {
		this(Support.dictFromString(model));
	}
	
	public ChoiceBox(List<String> model) {
		this(model.stream().collect(Collectors.toMap(String::new, String::new)));
	}
	
	public ChoiceBox(Map<String, String> model) {
		
		this.model = model;
		renderControl();
	}
	
	public boolean isComboMode() {
		return comboMode || model.size() > 5;
	}
	
	public void useComboMode(boolean value) {
		comboMode = value;
		renderControl();
	}
	
	private void renderControl() {
	
		removeAll();
		if(isComboMode()) {
			comboBox = new ComboBox(new ArrayList<>(model.keySet()));
			add(comboBox);
		} else {
			radioGroup = new RadioGroup(new ArrayList<>(model.keySet()));
			add(radioGroup);
		}
	}
	
	public void setValue(String value) {
		
		if(isComboMode()) {
			comboBox.setSelectedItem(value);
		} else {
			radioGroup.setSelectedItem(value);
		}
	}
	
	public String getValue() {
		
		if(isComboMode()) {
			return model.get(comboBox.getSelectedItem());
		} else {
			return model.get(radioGroup.getSelectedItem());
		}
	}
	
	public ComboBox getComboBox() {
		return comboBox;
	}
}
