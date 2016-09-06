package xqtr.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.LayoutFocusTraversalPolicy;

@SuppressWarnings("serial")
public class RadioGroup extends JPanel {
	
	private ButtonGroup buttons = new ButtonGroup();
	private List<AbstractButton> buttonList;
	
	private JRadioButton selected;
	private KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	
	public RadioGroup(List<String> model) {
		setLayout(new WrapLayout(WrapLayout.LEADING));
		
		model.forEach(item -> createRadioButton(item));
		buttonList = Collections.list(buttons.getElements());
		setTraversalPolicy();
	}
	
	private void createRadioButton(String text) {
		JRadioButton button = new JRadioButton(text);
		button.addFocusListener(new FocusGained());
		buttons.add(button);
		setArrowKeyTraversal(button);
		
		InputMap imap = button.getInputMap();
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "tabForward");
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, java.awt.event.InputEvent.SHIFT_DOWN_MASK), "tabBackward");
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "arrowForward");
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "arrowBackward");
		
		ActionMap amap = button.getActionMap();
		amap.put("tabForward", new TabForward());
		amap.put("tabBackward", new TabBackward());
		amap.put("arrowForward", new ArrowForward());
		amap.put("arrowBackward", new ArrowBackward());
		
		add(button);
	}
	
	private void setTraversalPolicy() {
		setFocusTraversalPolicyProvider(true);
		((Container) this).setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
			
			public Component getFirstComponent(Container focusCycleRoot) {
				return selected != null ? (Component) selected : super.getFirstComponent(focusCycleRoot);
			}
			
			public Component getLastComponent(Container focusCycleRoot) {
				return selected != null ? (Component) selected : super.getLastComponent(focusCycleRoot);
			}
		});
	}
	
	private void setArrowKeyTraversal(JRadioButton radioButton) {
		
		radioButton.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, new HashSet<>());
		radioButton.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, new HashSet<>());
	}
	
	private class FocusGained extends FocusAdapter {
		
		public void focusGained(FocusEvent e) {
			JRadioButton radioButton = (JRadioButton) e.getSource();
			radioButton.setSelected(true);
			buttonList.forEach(button -> button.setFont(new Font(getFont().getName(), Font.PLAIN, getFont().getSize())));
			radioButton.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
			selected = radioButton;
		}
	}
	
	private class ArrowForward extends AbstractAction {
		
		public void actionPerformed(ActionEvent e) {
			Component after = focusManager.getDefaultFocusTraversalPolicy().getComponentAfter(
					focusManager.getCurrentFocusCycleRoot(), selected);
			if(buttonList.contains(after)) {
				after.requestFocus();
			} else {
				firstButton().requestFocus();
			}
		}
	}
	
	private class ArrowBackward extends AbstractAction {
		
		public void actionPerformed(ActionEvent e) {
			Component before = focusManager.getDefaultFocusTraversalPolicy().getComponentBefore(
					focusManager.getCurrentFocusCycleRoot(), selected);
			if(buttonList.contains(before)) {
				before.requestFocus();
			} else {
				lastButton().requestFocus();
			}
		}
	}

	private class TabForward extends AbstractAction {
		
		public void actionPerformed(ActionEvent e) {
			focusManager.getDefaultFocusTraversalPolicy().getComponentAfter(
					focusManager.getCurrentFocusCycleRoot(), lastButton()).requestFocus();
		}
	}
	
	private class TabBackward extends AbstractAction {
		
		public void actionPerformed(ActionEvent e) {
			focusManager.getDefaultFocusTraversalPolicy().getComponentBefore(
					focusManager.getCurrentFocusCycleRoot(), firstButton()).requestFocus();
		}
	}
	
	private JRadioButton firstButton() {
		return (JRadioButton) buttonList.get(0);
	}
	
	private JRadioButton lastButton() {
		return (JRadioButton) buttonList.get(buttonList.size()-1);
	}
	
	public String getSelected() {
		return selected.getText();
	}
}
