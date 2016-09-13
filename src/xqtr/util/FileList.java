package xqtr.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;

import xqtr.libs.ListItemMovable;

@SuppressWarnings("serial")
public class FileList extends JDialog implements ActionListener {
	
	private CustomListModel model;
    private JList<JLabel> list = new JList<>();
    private JFileChooser fileChooser;
	
	public FileList(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
		
		setModel(new ArrayList<File>());
		list.setCellRenderer(new CustomCellRenderer());
        list.setTransferHandler(new ListItemMovable());
		list.setDropMode(DropMode.INSERT);
		list.setDragEnabled(true);
		Support.addKeyBinding(list, "DELETE", e -> removeSelectedItems());
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		Button addButton = new Button("_Add");
		toolbar.add(addButton);
		toolbar.add(new Button("_Remove"));
		toolbar.add(new Button("_Clear"));
		toolbar.addSeparator();
		toolbar.add(new Button("_Sort"));
		
		add(toolbar, BorderLayout.NORTH);
		add(scrollPane);
		
		Support.setTimeout(100, () -> list.requestFocus());
		
		setTitle("Files");
		setSize(360, 240);
		setMinimumSize(new Dimension(240, 120));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setModalityType(ModalityType.APPLICATION_MODAL);
		Support.addKeyBinding(getRootPane(), "ESCAPE SPACE", e -> setVisible(false ));
	}
	
	public void setModel(List<File> files) {
		model = new CustomListModel(files);
		list.setModel(model);
	}
	
	public List<File> getModel() {
		return model.asList();
	}

	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "Add":
			addItems();
			break;
		case "Remove":
			removeSelectedItems();
			break;
		case "Clear":
			model.clear();
			break;
		case "Sort":
			sortItems();
			break;
		}
	}
	
	private void addItems() {
		boolean saveModeEnabled = fileChooser.getDialogType() == JFileChooser.SAVE_DIALOG;
		while(true) {
			if(fileChooser.showDialog(null, "Add") != JFileChooser.APPROVE_OPTION) break;
			List<File> selectedFiles = Arrays.asList(fileChooser.getSelectedFiles());
			if(!saveModeEnabled && selectedFiles.stream().anyMatch(f -> !f.exists() ||
					!isValidExtension(Support.getFileExtension(f)))) {
				JOptionPane.showMessageDialog(null, "Please select a valid file to continue.",
						"Invalid File", JOptionPane.WARNING_MESSAGE);
				continue;
			}
			selectedFiles.forEach(file -> {
				String filePath = file.getPath();
				if(saveModeEnabled && !fileChooser.getFileFilter().accept(file)) {
					filePath += getDefaultExtension();
				}
				model.add(model.getSize(), new JLabel(filePath));
			});
			break;
		}
	}
	
	private boolean isValidExtension(String extension) {
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		if(filters.length == 1) return true;
		return ((FileTypeFilter)filters[1]).getExtensions().contains(extension);
	}
	
	private String getDefaultExtension() {
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		if(filters.length == 1) return "";
		return ((FileTypeFilter)filters[1]).getDefaultExtension();
	}

	private void removeSelectedItems() {
		int i = list.getSelectedIndices().length - 1;
		if(i == -1) return;
		int index = 0;
		while (list.getSelectedIndices().length != 0) {
			index = list.getSelectedIndices()[i--];
			model.remove(index);
		}
		if(index == model.getSize()) {
			index--;
		}
		list.setSelectedIndex(index);
	}
	
	private void sortItems() {
		List<File> sorted = model.asList();
		Collections.sort(sorted);
		model = new CustomListModel(sorted);
		list.setModel(model);
	}
	
	private class CustomListModel extends DefaultListModel<JLabel> {

		private List<JLabel> model;
		
		public CustomListModel(List<File> files) {
			this.model = Support.transform(files, file -> new JLabel(file.getPath()));
		}
		
		public int getSize() {
			return model.size();
		}
		
		public void add(int index, JLabel item) {
			model.add(index, item);
			fireIntervalAdded(this, index, index);
		}
		
		public JLabel remove(int index) {
			JLabel label = model.remove(index);
			fireIntervalRemoved(this, index, index);
			return label;
	    }
		
		public JLabel getElementAt(int index) {
			String path = model.get(index).getText();
			JLabel label = new JLabel(new File(path).getName());
			label.setToolTipText(path);
			return label;
		}
		
		public void clear() {
			int i = getSize();
			model.clear();
			fireIntervalRemoved(this, 0, i);
		}
		
		public List<File> asList() {
			return Support.transform(model, item -> new File(item.getText()));
		}
	}
	
	private class CustomCellRenderer implements ListCellRenderer<JLabel> {

		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value, int index,
				boolean isSelected, boolean cellHasFocus) {
			
			JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
			        isSelected, cellHasFocus);
			
			label.setText(value.getText());
			label.setToolTipText(value.getToolTipText());
			label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			return label;
		}
		
	}
}
