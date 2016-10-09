package xqtr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class ConfigSource extends TextDialog {
	
	private String result;
	
	public ConfigSource() {
		
		try {
			result = Files.lines(Paths.get(Application.configPath))
						.reduce("", (a, b) -> a + "\n" + b).substring(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		result = result.replaceAll("<(.+?)>", "<b>&lt;$1&gt;</b>").replaceAll("\\n", "<br>");
		
		displayText(result);
		setTitle("Configuration");
		setSize(720, 480);
		setVisible(true);
	}

}
