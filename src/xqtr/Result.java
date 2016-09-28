package xqtr;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import xqtr.util.Dialog;

@SuppressWarnings("serial")
public class Result extends Dialog {
	
	private String result;
	
	public Result(String result) {
		
		this.result = result;
		
		renderUI();
		setTitle("Result");
		setSize(420, 280);
	}
	
	private void renderUI() {
		
		JScrollPane scrollPane = new JScrollPane();
		JTextPane textPane = new JTextPane();
		
		textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		textPane.setText(result);
		
		scrollPane.getViewport().add(textPane);
		add(scrollPane);
	}

}
