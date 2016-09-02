package xqtr.util;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ComboBox extends JComboBox<String> {
	
	private List<String> model;
	
	public ComboBox() {
		
		this(null);
	}
	
	public ComboBox(List<String> model) {
		
		this.model = model;
		addItems();
		
		Support.setTimeout(100, () -> 
			addItemListener((ItemListener) SwingUtilities.getRoot(this))
		);
	}
	
	private void addItems() {
		
		List<String> items = model != null ? model : new ArrayList<String>();
		addItem("");
		items.forEach(item -> addItem(item));
	}
}
