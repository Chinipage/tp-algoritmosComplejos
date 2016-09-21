package xqtr.view;

import java.awt.Component;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public abstract class Unitable extends Control {

	private JLabel unitLabel = new JLabel();
	private Component separator = createSeparator(6);
	
	protected void toggleUnit() {
		if(!unitLabel.getText().isEmpty()) {
			add(separator);
			add(unitLabel);
		} else {
			remove(separator);
			remove(unitLabel);
		}
	}
	
	public void setUnit(String unit) {
		unitLabel.setText(unit);
		toggleUnit();
	}
	
}
