package xqtr;

import xqtr.util.Support;
import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class ConfigSource extends TextDialog {
	
	public ConfigSource() {
		
		String result = Support.readFile(Application.properties.get("config.file.path"));
		
		if(result == null) {
			Support.delay(() -> dispose());
		} else {
			result = "<font face=\"monospace\" size=3>" + result.replaceAll("<(.+?)>", "<b>&lt;$1&gt;</b>")
			.replaceAll("\"(.+?)\"", "</b>\"$1\"<b>").replaceAll("\\n", "<br>")
			.replaceAll("\\s", "&nbsp;") + "</font>";
			displayText(result);
		}
		
		setTitle("Configuration");
		setSize(720, 480);
		setVisible(true);
	}

}
