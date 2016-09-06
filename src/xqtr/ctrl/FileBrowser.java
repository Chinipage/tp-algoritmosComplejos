package xqtr.ctrl;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import xqtr.util.Button;

@SuppressWarnings("serial")
public class FileBrowser extends Control implements FocusListener {

	private JTextField pathField = new JTextField();
	private Button browseButton = new Button("Browse");
	
	public FileBrowser() {
		this("");
	}
	
	public FileBrowser(String value) {
		
		setValue(value);
		
		pathField.setFont(defaultFont);
		pathField.addFocusListener(this);
		
		add(pathField);
		add(createSeparator());
		add(browseButton);
	}

	public void focusGained(FocusEvent e) {
		browseButton.setMnemonic('B');
	}
	
	public void focusLost(FocusEvent e) {
		browseButton.setMnemonic(0);
	}
	
	public void setValue(String value) {
		pathField.setText(value);
	}
}
