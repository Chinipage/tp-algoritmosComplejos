package xqtr;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import xqtr.util.Section;

@SuppressWarnings("serial")
public class Page extends Section {

	public Page() {
		JLabel label = new JLabel("Hello, world.", SwingUtilities.CENTER);
		add(label);
	}
}
