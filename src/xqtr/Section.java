package xqtr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial")
public class Section extends JPanel {
	
	private Border paddingSet;
	private Border borderSet;
	
	Section() {
		setLayout(new BorderLayout(0, 0));
		setPadding(10);
	}
	
	public void setWidth(int width) {
		
		setPreferredSize(new Dimension(width, 0));
	}
	
	public void setHeight(int height) {
		
		setPreferredSize(new Dimension(0, height));
	}
	
	public void setPadding(int padding) {
		
		setPadding(padding, padding, padding, padding);
	}
	
	public void setPadding(int paddingTop, int paddingLeft, int paddingBottom, int paddingRight) {
		
		paddingSet = BorderFactory.createEmptyBorder(paddingTop, paddingLeft,
				paddingBottom, paddingRight);
		setBorder(new CompoundBorder(borderSet, paddingSet));
	}
	
	public void setBorder(int border) {
		
		setBorder(border, border, border, border);
	}
	
	public void setBorder(int borderTop, int borderLeft, int borderBottom, int borderRight) {
		
		borderSet = BorderFactory.createMatteBorder(borderTop, borderLeft,
				borderBottom, borderRight, Color.GRAY);
		setBorder(new CompoundBorder(borderSet, paddingSet));
	}
}