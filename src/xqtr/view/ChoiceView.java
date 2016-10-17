package xqtr.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import xqtr.util.ComboBox;
import xqtr.util.RadioGroup;
import xqtr.util.Support;

@SuppressWarnings("serial")
public class ChoiceView extends Control {

	private Map<String, String> model;
	private ComboBox comboBox;
	private RadioGroup radioGroup;
	private boolean comboMode;
	
	public ChoiceView(String model) {
		this(Support.dictFromString(model));
	}
	
	public ChoiceView(List<String> model) {
		this(model.stream().collect(Collectors.toMap(String::new, String::new)));
	}
	
	public ChoiceView(Map<String, String> model) {
		
		this.model = model;
		renderControl();
	}
	
	public boolean isComboMode() {
		return comboMode || model.size() > 5;
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
		
		if(value == null) return;
		model.keySet().stream().filter(k -> model.get(k).equals(value)).findAny().ifPresent(key -> {
			if(isComboMode()) {
				comboBox.setSelectedItem(key);
			} else {
				radioGroup.setSelectedItem(key);
			}
		});
	}
	
	public String getValue() {
		
		if(isComboMode()) {
			return model.get(comboBox.getSelectedItem());
		} else {
			return model.get(radioGroup.getSelectedItem());
		}
	}
}
