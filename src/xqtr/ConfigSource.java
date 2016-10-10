package xqtr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import xqtr.util.Support;
import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class ConfigSource extends TextDialog {
	
	private String result = new String();
	
	public ConfigSource() {
		
		String configFilePath = Application.properties.get("config.file.path");
		
		try {
			result = Files.lines(Paths.get(configFilePath))
						.reduce("", (a, b) -> a + "\n" + b).substring(1);
		} catch (IOException e) {
			Support.displayMessage("Error: " + configFilePath + " not found");
			Support.delay(() -> dispose());
		}
		
		result = "<font face=\"monospace\" size=3>" + result.replaceAll("<(.+?)>", "<b>&lt;$1&gt;</b>")
					.replaceAll("\"(.+?)\"", "</b>\"$1\"<b>").replaceAll("\\n", "<br>")
					.replaceAll("\\s", "&nbsp;") + "</font>";
		
		displayText(result);
		setTitle("Configuration");
		setSize(720, 480);
		setVisible(true);
	}

}
