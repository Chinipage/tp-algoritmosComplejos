package xqtr.libs;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import xqtr.util.Support;

public class UndoHandler implements UndoableEditListener {

	private UndoManager undoManager = new UndoManager();
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();
	
	public void undoableEditHappened(UndoableEditEvent e) {
		undoManager.addEdit(e.getEdit());
		undoAction.updateUndoState();
		redoAction.updateRedoState();
	}
	
	public void handle(JTextComponent textComponent) {
		Support.delay(() -> {
			textComponent.getDocument().addUndoableEditListener(this);
		});
	}
	
	@SuppressWarnings("serial")
	public class UndoAction extends AbstractAction {
		
		public UndoAction() {
			putValue(Action.NAME, "Undo");
			putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
			putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
			setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.undo();
			} catch(CannotUndoException ex) {
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		protected void updateUndoState() {
			
			if(undoManager.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undoManager.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}
	
	@SuppressWarnings("serial")
	public class RedoAction extends AbstractAction {
		
		public RedoAction() {
			putValue(Action.NAME, "Redo");
			putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
			putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
			setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.redo();
			} catch(CannotRedoException ex) {
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}
		
		protected void updateRedoState() {
			
			if(undoManager.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undoManager.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
	
	public Action getUndoAction() {
		return undoAction;
	}
	
	public Action getRedoAction() {
		return redoAction;
	}
}
