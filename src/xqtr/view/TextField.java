package xqtr.view;

import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class TextField extends Control {
	
	private JPasswordField textField = new JPasswordField();
	
	public TextField() {
		this("");
	}
	
	public TextField(String value) {
		setValue(value);
		
		textField.setPreferredSize(defaultSize);
		textField.setFont(defaultFont);
		
		setConcealed(false);
		add(textField);
	}
	
	public void setConcealed(boolean concealed) {
		
		if(concealed) {
			textField.setEchoChar('•');
		} else {
			textField.setEchoChar((char)0);
		}
	}
	
	public String getValue() {
		return new String(textField.getPassword());
	}
	
	public void setValue(String value) {
		textField.setText(value);
	}
}
