package xqtr.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import xqtr.view.Control;

import javax.swing.JLabel;

public class Form {
	
	private Map<String, Component> elements;
	private Dimension labelSize;
	
	public Form() {
		
		elements = new LinkedHashMap<>();
	}
	
	public Form(JPanel container, Map<String, Component> elements) {
		
		this.elements = elements;
		labelSize = calculatelabelSize();
	}
	
	private Dimension calculatelabelSize() {
		JLabel label = new JLabel();
		FontMetrics fm = label.getFontMetrics(label.getFont());
		String longestLabel = elements.keySet().stream().max(Comparator.comparing(fm::stringWidth)).get();
		int width = fm.stringWidth(longestLabel + ":");
		return new Dimension(width+1, 0);
	}
	
	public void addElement(String label, Component control) {
		
		label = Character.toUpperCase(label.charAt(0)) + label.substring(1);
		elements.put(label, control);
	}
	
	public void placeIn(JPanel container) {
		if(elements.isEmpty()) return;
		
		if(labelSize == null) {
			labelSize = calculatelabelSize();
		}
		
		@SuppressWarnings("serial")
		JPanel outer = new JPanel() {
			public Dimension getPreferredSize() {
				return new Dimension((int)(container.getSize().getWidth()-20),
						(int)super.getPreferredSize().getHeight());
			};
		};
		outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
		
		elements.forEach((name, control) -> {
			JPanel inner = new JPanel();
		    inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
		    
		    JLabel label = new JLabel(name + ":", JLabel.RIGHT);
		    label.setPreferredSize(labelSize);
		    
		    inner.add(label);
		    inner.add(Box.createRigidArea(new Dimension(7, 0)));
		    JPanel fieldPanel = new JPanel(new BorderLayout());
		    if(control != null) fieldPanel.add(control);
		    inner.add(fieldPanel);
		    outer.add(inner);
		    outer.add(Box.createRigidArea(new Dimension(0, 10)));
		});
		
		container.add(outer);
		outer.remove(outer.getComponents().length-1);
	}
	
	public Map<String, String> getWithNames() {
		
		Map<String, String> result = new LinkedHashMap<>();
		elements.forEach((name, control) -> {
			if(control instanceof Control) {
				result.put(name, ((Control) control).getValue());
			}
		});
		return result;
	}
	
	public Map<String, String> getWithIDs() {
		
		Map<String, String> result = new LinkedHashMap<>();
		elements.values().stream().filter(c -> c instanceof Control)
			.forEach(c -> result.put(((Control) c).getID(), ((Control) c).getValue()));
		return result;
	}
	
	public boolean isFilled() {
		
		if(elements.isEmpty()) return false;
		return elements.values().stream().filter(c -> c instanceof Control)
			.allMatch(c -> !((Control) c).getValue().isEmpty());
	}
	
	public void clear() {
		elements.clear();
	}
}