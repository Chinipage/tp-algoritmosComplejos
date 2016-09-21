package xqtr.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Control extends JPanel {
	
	static Font defaultFont = new Font(null, Font.BOLD, 12);
	static Dimension defaultSize = new Dimension(0, 29);
	
	Control() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	static Component createSeparator() {
		return createSeparator(5);
	}
	
	static Component createSeparator(int length) {
		return Box.createRigidArea(new Dimension(length, 0));
	}
}
