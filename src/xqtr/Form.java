package xqtr;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Form {
	
	private Map<String, JComponent> elements;
	private Dimension labelWidth;
	
	public Form() {
		elements = new HashMap<>();
	}
	
	public Form(JComponent container, Map<String, JComponent> elements) {
		
		this.elements = elements;
		labelWidth = calculateLabelWidth();
	}
	
	private Dimension calculateLabelWidth() {
		return new JLabel(elements.keySet().stream().max(Comparator.comparing(String::length))
				.get() + ":").getPreferredSize();
//		return elements.keySet().stream().mapToInt( k -> k.length() ).max().getAsInt() * 10;
	}
	
	public void addElement(String label, JComponent field) {
		elements.put(label, field);
	}
	
	public void placeIn(JComponent container) {
		
		if(labelWidth == null) {
			labelWidth = calculateLabelWidth();
		}
		
		JPanel outer = new JPanel();
		outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
		
		for(Map.Entry<String, JComponent> entry : elements.entrySet()) {
		    String labelText = entry.getKey() + ":";
		    JComponent field = entry.getValue();
		    
		    JPanel inner = new JPanel();
		    inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
		    
		    JLabel label = new JLabel(labelText, JLabel.RIGHT);
		    label.setPreferredSize(labelWidth);
		    
		    inner.add(label);
		    inner.add(Box.createRigidArea(new Dimension(5, 0)));
		    inner.add(field != null ? field : new JPanel());
		    outer.add(inner);
		    outer.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		
		container.add(outer);
	}
}