package xqtr;

import xqtr.util.Support;
import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class ErrorLog extends TextDialog {
	
	public ErrorLog() {
		
		String result = Support.readFile(Application.properties.get("error.log.path"));

		if(result == null) {
			Support.delay(() -> dispose());
		} else {
			result = result.replaceAll("(?m)(^.+?)\\s", "<b>$1</b> ");
			result = "<font face=\"monospace\" size=3>" + result.replaceAll("\\n", "<br>") + "</font>";
			displayText(result);
		}
		
		setTitle("Error Log");
		setSize(720, 480);
		setVisible(true);
	}

}
