package xqtr.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;

import xqtr.libs.FileItemMovable;

@SuppressWarnings("serial")
public class FileList extends Dialog implements ActionListener {
	
	private CustomListModel model;
    private JList<JLabel> list = new JList<>();
    private JFileChooser fileChooser;
    boolean saveModeEnabled;
	
	public FileList(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
		saveModeEnabled = fileChooser.getDialogType() == JFileChooser.SAVE_DIALOG;
		
		setModel(new ArrayList<File>());
		list.setCellRenderer(new CustomCellRenderer());
        list.setTransferHandler(new FileItemMovable());
		list.setDropMode(DropMode.INSERT);
		list.setDragEnabled(true);
		Support.addKeyBinding(list, "DELETE", e -> removeSelectedItems());
		Support.addMouseListener(list, "RIGHTCLICK", e -> removeSelectedItems());
		Support.addMouseListener(list, "DOUBLECLICK", e -> editSelectedItem());
		Support.addKeyListener(list, "ENTER", e -> editSelectedItem());
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.add(new Button("_Add"));
		toolbar.add(new Button("_Edit"));
		toolbar.add(new Button("_Remove"));
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(new Button("_Clear"));
		toolbar.add(new Button("_Sort"));
		
		add(toolbar, BorderLayout.NORTH);
		add(scrollPane);
		
		Support.delay(() -> list.requestFocus());
		
		setTitle("Files");
		setSize(360, 240);
		setMinimumSize(new Dimension(240, 120));
		Support.addKeyBinding(getRootPane(), "SPACE", e -> dispose());
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
		case "Edit":
			editSelectedItem();
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
	
	private JLabel labelFromFile(File file) {
		JLabel label = new JLabel(file.getName());
		label.setToolTipText(file.getPath());
		return label;
	}
	
	private boolean areFilesValid(List<File> files) {
		if(saveModeEnabled) {
			return files.stream().allMatch(f -> Support.getFileExtension(f).isEmpty() || hasValidExtension(f));
		} else {
			return files.stream().allMatch(f -> f.exists() && hasValidExtension(f));
		}
	}
	
	private boolean addFiles(File[] files, int index) {
		List<File> fileList = Support.list(files);
		if(!areFilesValid(fileList)) {
			Support.displayMessage("Warning: Invalid file");
			return false;
		}
		fileList.forEach(file -> {
			String filePath = file.getPath();
			if(saveModeEnabled && !fileChooser.getFileFilter().accept(file)) {
				filePath += getDefaultExtension();
			}
			model.add(index, labelFromFile(new File(filePath)));
		});
		return true;
	}
	
	private void addItems() {
		while(fileChooser.showDialog(null, "Add") == JFileChooser.APPROVE_OPTION
				&& !addFiles(fileChooser.getSelectedFiles(), model.getSize()));
	}
	
	private boolean hasValidExtension(File file) {
		FileFilter[] filters = fileChooser.getChoosableFileFilters();
		if(filters.length == 1) return true;
		return ((FileTypeFilter)filters[1]).getExtensions().contains(Support.getFileExtension(file));
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
			this.model = Support.map(file -> labelFromFile(file), files);
		}
		
		public int getSize() {
			return model.size();
		}
		
		public void add(int index, JLabel item) {
			if(!areFilesValid(Support.list(new File(item.getToolTipText())))) return;
			model.add(index, item);
			fireIntervalAdded(this, index, index);
		}
		
		public JLabel remove(int index) {
			JLabel label = model.remove(index);
			fireIntervalRemoved(this, index, index);
			return label;
	    }
		
		public JLabel getElementAt(int index) {
			return model.get(index);
		}
		
		public void clear() {
			int i = getSize();
			model.clear();
			fireIntervalRemoved(this, 0, i);
		}
		
		public List<File> asList() {
			return Support.map(item -> new File(item.getToolTipText()), model);
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
	
	private void editSelectedItem() {
		int index = list.getSelectionModel().getMinSelectionIndex();
		if(index == -1) return;
        JLabel item = model.getElementAt(index);
        String input = JOptionPane.showInputDialog("Edit file path", item.getToolTipText());
        if(input == null) return;
        File file = new File(input.trim());
        if(addFiles(new File[]{file}, index)) {
        	model.remove(index+1);
        	list.setSelectedIndex(index);
        }
	}
}
