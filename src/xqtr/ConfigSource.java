package xqtr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import xqtr.util.Dialog;

@SuppressWarnings("serial")
public class ConfigSource extends Dialog {
	
	private String result;
	
	public ConfigSource() {
		
		try {
			result = Files.lines(Paths.get(Application.configPath))
						.reduce("", (a, b) -> a + "\n" + b).substring(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		renderUI();
		setTitle("Configuration");
		setSize(720, 480);
		setVisible(true);
	}
	
	private void renderUI() {
		
		JScrollPane scrollPane = new JScrollPane();
		JTextPane textPane = new JTextPane();
		
		textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textPane.setEditable(false);
		textPane.setText(result);
		
		scrollPane.getViewport().add(textPane);
		add(scrollPane);
	}

}
