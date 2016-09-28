package xqtr.util;

import java.awt.Dimension;

import javax.swing.JDialog;

import xqtr.Application;

@SuppressWarnings("serial")
public class Dialog extends JDialog {
	
	public Dialog() {
		
		setTitle("Dialog");
		setSize(400, 300);
		setMinimumSize(new Dimension(200, 100));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Support.delay(() -> setLocationRelativeTo(Application.frame));
		Support.addKeyBinding(getRootPane(), "ESCAPE", e -> dispose());
	}
}
