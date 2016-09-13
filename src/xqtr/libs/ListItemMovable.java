package xqtr.libs;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 *  TransferHandler subclass that gives drag & drop capabilities to JList items.
 *  Usage:
 *  	JList list = new JList();
 *  	list.setDragEnabled(true);
 *		list.setDropMode(DropMode.INSERT);
 *		list.setTransferHandler(new ListItemMovable());
 *  Adapted from:
 *  	http://stackoverflow.com/questions/16586562/reordering-jlist-with-drag-and-drop
 */
@SuppressWarnings("serial")
public class ListItemMovable extends TransferHandler {
	private final DataFlavor localObjectFlavor;
	
	public ListItemMovable() {
		localObjectFlavor = new ActivationDataFlavor(
				Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
	}
	
	protected Transferable createTransferable(JComponent c) {
		JList<?> list = (JList<?>) c;
		indices = list.getSelectedIndices();
		Object[] transferedObjects = list.getSelectedValuesList().toArray();
		return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
	}
	
	public boolean canImport(TransferSupport info) {
		return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
	}
	
	public int getSourceActions(JComponent c) {
		return MOVE;
	}
	
	public boolean importData(TransferSupport info) {
		if(!canImport(info)) {
			return false;
		}
		JList<?> target = (JList<?>) info.getComponent();
		JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
		@SuppressWarnings("unchecked")
		DefaultListModel<Object> model = (DefaultListModel<Object>) target.getModel();
		int index = dl.getIndex();
		int max = model.getSize();
		if(index<0 || index>max) {
			index = max;
		}
		addIndex = index;
		try {
			Object[] values = (Object[])info.getTransferable().getTransferData(localObjectFlavor);
			addCount = values.length;
			for(int i=0; i<values.length; i++) {
				int idx = index++;
				model.add(idx, values[i]);
				target.addSelectionInterval(idx, idx);
			}
			return true;
		} catch(UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return false;
	}
	
	protected void exportDone(JComponent c, Transferable data, int action) {
		cleanup(c, action == MOVE);
	}
	
	private void cleanup(JComponent c, boolean remove) {
		if(remove && indices != null) {
			JList<?> source = (JList<?>) c;
			DefaultListModel<?> model = (DefaultListModel<?>) source.getModel();
			if(addCount > 0) {
				for(int i=0; i<indices.length; i++) {
					if(indices[i]>=addIndex) {
						indices[i] += addCount;
					}
				}
			}
			for(int i=indices.length-1; i>=0; i--) {
				model.remove(indices[i]);
			}
		}
		indices  = null;
		addCount = 0;
		addIndex = -1;
	}
	
	private int[] indices = null;
	private int addIndex  = -1; // Location where items were added
	private int addCount  = 0;  // Number of items added.
}