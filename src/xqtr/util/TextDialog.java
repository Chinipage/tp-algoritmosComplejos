package xqtr.util;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class TextDialog extends Dialog {
	
	protected JTextPane textPane = new JTextPane();
	
	protected void displayText(String text) {
		
		textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		textPane.setText(text);
		add(new JScrollPane(textPane));
	}
}
