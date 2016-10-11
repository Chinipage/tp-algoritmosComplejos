package xqtr;

import xqtr.util.TextDialog;

@SuppressWarnings("serial")
public class Parameters extends TextDialog {
	
	public Parameters(String params) {
	
		displayText(params);
		setTitle("Parameters");
		setSize(420, 280);
		setVisible(true);
	}

}
