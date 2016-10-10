package xqtr.util;


import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Button extends JButton {
	
	public Button(String label) {
		
		Support.setTimeout(100, () -> 
			addActionListener((ActionListener) this.getTopLevelAncestor())
		);
		init(label);
	}
	
	public Button(String label, ActionListener listener) {
		
		addActionListener(listener);
		init(label);
	}
	
	private void init(String label) {
		
		setText(label.replaceFirst("_", ""));
		Support.getMnemonic(label).ifPresent(mnemonic -> setMnemonic(mnemonic));
		setFocusable(false);
	}
	
}
