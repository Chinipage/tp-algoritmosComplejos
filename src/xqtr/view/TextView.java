package xqtr.view;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.event.ChangeEvent;

import xqtr.Application;
import xqtr.util.Support;

@SuppressWarnings("serial")
public class TextView extends AbstractControl {
	
	private JPasswordField textField = new JPasswordField();
	private ActionListener deleteAction = e -> textField.replaceSelection("");
	
	public TextView() {
		this("");
	}
	
	public TextView(String value) {
		setValue(value);
		
		Application.undoHandler.handle(textField);
		Support.addChangeListener(textField, e -> changeListener(e));
		textField.putClientProperty("JPasswordField.cutCopyAllowed", true);
		textField.setPreferredSize(defaultSize);
		textField.setFont(defaultFont);	
		
		setConcealed(false);
		add(textField);
	}
	
	public void setConcealed(boolean concealed) {
		
		if(concealed) {
			textField.setEchoChar('�');
		} else {
			textField.setEchoChar((char)0);
		}
	}
	
	public String getValue() {
		return new String(textField.getPassword());
	}
	
	public void setValue(String value) {
		textField.setText(value);
	}
	
	private void changeListener(ChangeEvent e) {
		JMenuItem deleteItem = Application.frame.menu.getItem("Delete");
		switch((String) e.getSource()) {
		case "focus":
			Support.delay(() -> {
				deleteItem.setEnabled(textField.getSelectedText() != null);
				deleteItem.addActionListener(deleteAction);
			});
			break;
		case "blur":
			Support.delay(() -> {
				Component comp = Application.frame.getFocusOwner();
				if(comp == null || !comp.equals(Application.frame.menu.getRootPane())) {
					deleteItem.setEnabled(false);
					deleteItem.removeActionListener(deleteAction);
				}
			});
			break;
		case "change":
			deleteItem.setEnabled(textField.getSelectedText() != null);
		}
	}
}
