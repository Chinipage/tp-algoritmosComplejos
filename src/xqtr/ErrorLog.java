package xqtr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import xqtr.util.Support;
import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class ErrorLog extends TextDialog {
	
	private String result = new String();
	
	public ErrorLog() {
		
		String errorLogPath = Application.properties.get("error.log.path");
		
		try {
			result = Files.lines(Paths.get(errorLogPath))
						.reduce("", (a, b) -> a + "\n" + b).substring(1);
		} catch (IndexOutOfBoundsException e) {
			result = "";
		} catch (IOException e) {
			Support.displayMessage("Error: " + errorLogPath + " not found");
			Support.delay(() -> dispose());
		}
		
		result = "<font face=\"monospace\" size=3><b>" + result.replaceFirst(":", ":</b>")
					.replaceAll("\\n", "<br>") + "</font>";
		
		displayText(result);
		setTitle("Error Log");
		setSize(720, 480);
		setVisible(true);
	}

}
