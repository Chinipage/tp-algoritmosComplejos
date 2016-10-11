package xqtr.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

@SuppressWarnings("serial")
public class ComboBox extends JComboBox<String> {
	
	public ComboBox() {
		
		this(null);
	}
	
	@SuppressWarnings("unchecked")
	public ComboBox(List<String> model) {
		if(model != null) {
			setModel(model);
		}
		
		Support.setTimeout(500, () ->
			addItemListener((ItemListener) SwingUtilities.getRoot(this))
		);
		
		setModel(new MyComboModel());
		setRenderer(new ComboRenderer());
		Support.addKeyListener(this, "DOWN UP", e -> {
			if(!isPopupVisible()) showPopup();
		});
	}
	
	public void setModel(List<String> model) {
		removeAllItems();
		addItem("");
		model.forEach(item -> addItem(item));
	}
	
	class MyComboModel extends DefaultComboBoxModel<String> {
	    public void setSelectedItem(Object item) {
	        if(!item.toString().isEmpty()) {
	        	super.setSelectedItem(item);
	        }
	    };
	}
	
	class ComboRenderer extends BasicComboBoxRenderer {
		
		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(index == 0) {
				this.setFont(new Font(null, Font.PLAIN, 1));
				setPreferredSize(new Dimension(0, 1));
			} else if(index > 0 && index < list.getModel().getSize()) {
				this.setFont(new Font(null, isSelected ? Font.ITALIC : Font.PLAIN, 12));
				setPreferredSize(new Dimension(0, 25));
			} else {
				this.setFont(new Font(null, Font.BOLD, 12));
				setPreferredSize(new Dimension(0, 29));
				setBorder(new EmptyBorder(6, 6, 6, 6));
			}
			setEnabled(index != 0);
	        return this;
		}
	}

}