package xqtr.util;

import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ComboBox extends JComboBox<String> {
	
	public ComboBox() {
		
		this(new String[0]);
	}
	
	public ComboBox(String[] model) {
		
		super(model);
		Support.setTimeout(100, () -> 
			addItemListener((ItemListener) SwingUtilities.getRoot(this))
		);
	}
}
