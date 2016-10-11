package xqtr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import xqtr.util.Support;
import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class History extends TextDialog {
	
	private static final String path = Application.properties.get("cmd.history.path");
	
	public History() {
		
		String result = Support.readFile(path);
		
		if(result == null) {
			Support.delay(() -> dispose());
		} else {
			result = "<font face=\"monospace\" size=3>" + result.replaceAll("(?m)^(\\S+ )", "<b>$1</b>")
			.replaceAll("\\n", "<br>") + "</font>";
			displayText(result);
		}
		
		setTitle("Command History");
		setSize(720, 480);
		setVisible(true);
	}
	
	public static void init() {
		try {
			new File(path).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String command) {
		try {
			Files.write(Paths.get(path), (command + "\n").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
