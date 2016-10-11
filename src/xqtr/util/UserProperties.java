package xqtr.util;

import java.io.FileInputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class UserProperties extends Properties {
	
	private Properties defaultProperties = new Properties();
	
	public UserProperties() {
		try {
			load(new FileInputStream(Support.loadResource("User.properties")));
		} catch (Exception e) {
			Support.displayMessage("Critical Error: There was a problem loading user properties\n" + e.getMessage());
		}
		
		String defaults = "config.file.path: Config.xml;"
						+ "error.log.path: Error.log;"
						+ "cmd.history.path: History.log;"
						+ "header.visible: true;"
						+ "footer.visible: true;"
						+ "frame.width: 440;"
						+ "frame.height: 550;"
						+ "frame.maximized: false;"
						+ "menubar.visible: true";
		defaultProperties.putAll(Support.dictFromString(defaults));
	}
	
	public String get(String name) {
		String property = getProperty(name);
		if(property == null || property.isEmpty()) {
			property = defaultProperties.getProperty(name);
		}
		return property;
	}
}