package xqtr.view;

import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class TextField extends Control {
	
	private JPasswordField textField = new JPasswordField();
	
	public static boolean CONCEALED = true;
	public static boolean UNCONCEALED = false;
	
	public TextField() {
		this(false);
	}
	
	public TextField(boolean concealed) {
		
		textField.setPreferredSize(defaultSize);
		textField.setFont(defaultFont);
		
		if(!concealed) {
			reveal();
		}
		
		add(textField);
	}
	
	public void conceal() {
		textField.setEchoChar('•');
	}
	
	public void reveal() {
		textField.setEchoChar((char)0);
	}
}
