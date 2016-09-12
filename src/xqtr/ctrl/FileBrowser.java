package xqtr.ctrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import xqtr.util.Button;
import xqtr.util.FileTypeFilter;

@SuppressWarnings("serial")
public class FileBrowser extends Control implements FocusListener, ActionListener {

	private JTextField pathField = new JTextField();
	private Button browseButton = new Button("Browse");
	private JFileChooser fileChooser = new JFileChooser();
	private boolean saveModeEnabled = false;
	private String defaultExtension;
	
	public FileBrowser() {
		this("");
	}
	
	public FileBrowser(String value) {
		
		setValue(value);
		
		pathField.setFont(defaultFont);
		pathField.setEditable(false);
		pathField.addFocusListener(this);
		pathField.addMouseListener(new MouseClickedAdapter());
		browseButton.addActionListener(this);
		
		add(pathField);
		add(createSeparator());
		add(browseButton);
	}
	
	public void setAllowMultiple(boolean newValue) {
		fileChooser.setMultiSelectionEnabled(newValue);
	}

	public void focusGained(FocusEvent e) {
		browseButton.setMnemonic('B');
	}
	
	public void focusLost(FocusEvent e) {
		browseButton.setMnemonic(0);
	}
	
	public void setValue(String value) {
		pathField.setText(value);
	}
	
	public void setFormat(String format) {
		FileFilter filter = new FileTypeFilter(format);
		fileChooser.setFileFilter(filter);
		
		int i = format.indexOf(" ");
		this.defaultExtension = i == -1 ? format : "." + format.substring(0, i);
	}
	
	public void setSaveModeEnabled(boolean saveMode) {
		saveModeEnabled = saveMode;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		while(true) {
			int r = saveModeEnabled ? fileChooser.showSaveDialog(null) : fileChooser.showOpenDialog(null);
			if(r != JFileChooser.APPROVE_OPTION) break;
			File selectedFile = fileChooser.getSelectedFile();
			if(!saveModeEnabled && !selectedFile.exists()) continue;
			String filePath = fileChooser.getSelectedFile().getPath();
			if(saveModeEnabled && !fileChooser.getFileFilter().accept(selectedFile)) {
				filePath += defaultExtension;
			}
			setValue(filePath);
			break;
		}
	}
	
	private class MouseClickedAdapter extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {
			
			actionPerformed(null);
		}
	}
}