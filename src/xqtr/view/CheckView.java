package xqtr.view;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class CheckView extends Control {
	
	private JCheckBox checkBox;
	private String nvalue = "no";
	private String yvalue = "yes";
	
	public CheckView() {
		this("");
	}
	
	public CheckView(String text) {
		
		if(text == null) text = "";
		checkBox = new JCheckBox(text);
		add(checkBox);
	}
	
	public void setSelected(boolean value) {
		checkBox.setSelected(value);
	}
	
	public boolean isSelected() {
		return checkBox.isSelected();
	}
	
	public String getValue() {
		
		return isSelected() ? yvalue : nvalue;
	}
	
	public void setValue(String value) {
		
		checkBox.setText(value);
	}
	
	public void setValueForNo(String value) {
		if(value != null) {
			nvalue = value;
		}
	}
	
	public void setValueForYes(String value) {
		if(value != null) {
			yvalue = value;
		}
	}
	
}
