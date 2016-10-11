package xqtr.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import xqtr.util.Support;

@SuppressWarnings("serial")
public abstract class AbstractControl extends JPanel {
	
	static Font defaultFont = new Font(null, Font.BOLD, 12);
	static Dimension defaultSize = new Dimension(0, 29);
	
	private boolean required;
	
	AbstractControl() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	static Component createSeparator() {
		return createSeparator(5);
	}
	
	static Component createSeparator(int length) {
		return Box.createRigidArea(new Dimension(length, 0));
	}
	
	public abstract String getValue();
	
	public abstract void setValue(String value);
	
	public boolean isEmpty() {
		return getValue().isEmpty();
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired() {
		required = true;
	}
	
	private String oldValue;
	
	public void addChangeListener(Runnable runnable) {
		oldValue = getValue();
		Support.setInterval(500, s -> {
			if(!getValue().equals(oldValue)) {
				runnable.run();
				oldValue = getValue();
			}
		});
	}
}
