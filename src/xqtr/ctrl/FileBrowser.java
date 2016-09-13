package xqtr.ctrl;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import xqtr.util.Button;
import xqtr.util.FileTypeFilter;
import xqtr.util.Support;
import xqtr.util.FileList;

@SuppressWarnings("serial")
public class FileBrowser extends Control implements FocusListener {

	private JTextField pathField = new JTextField();
	private Button browseButton = new Button("Browse");
	private JFileChooser fileChooser = new JFileChooser();
	private boolean saveModeEnabled = false;
	private boolean multiModeEnabled = false;
	private String defaultExtension;
	private List<File> files = new ArrayList<>();
	private List<String> validExtensions;
	
	public FileBrowser() {
		this("");
	}
	
	public FileBrowser(String value) {
		
		setValue(value);
		
		pathField.setFont(defaultFont);
		pathField.setEditable(false);
		pathField.addFocusListener(this);
		pathField.addMouseListener(new MouseClickAdapter());
		pathField.addKeyListener(new KeyPressAdapter());
		browseButton.addActionListener(e -> displayFileDialog());
		add(pathField);
		add(createSeparator());
		add(browseButton);
	}

	public void focusGained(FocusEvent e) {
		browseButton.setMnemonic('B');
	}
	
	public void focusLost(FocusEvent e) {
		browseButton.setMnemonic(0);
	}
	
	public void setModel(List<File> model) {
		files = new ArrayList<>(model);
		setFieldText();
	}
	
	public void setValue(String value) {
		if(value.isEmpty()) return;
		if(value.indexOf('/') == -1) {
			value = fileChooser.getCurrentDirectory().getPath() + "/" + value;
		}
		File file = new File(value);
		pathField.setText(file.getName());
		pathField.setToolTipText(file.getPath());
	}
	
	public void setFormat(String format) {
		FileTypeFilter filter = new FileTypeFilter(format);
		fileChooser.setFileFilter(filter);
		validExtensions = filter.getExtensions();
		
		int i = format.indexOf(" ");
		this.defaultExtension = i == -1 ? format : "." + format.substring(0, i);
	}
	
	public void setSaveModeEnabled(boolean newValue) {
		saveModeEnabled = newValue;
	}
	
	public void setMultiModeEnabled(boolean newValue) {
		multiModeEnabled = newValue;
		fileChooser.setMultiSelectionEnabled(newValue);
	}
	
	private class MouseClickAdapter extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {
			displayFileList();
		}
	}
	
	private class KeyPressAdapter extends KeyAdapter {
	
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				displayFileList();
			}
		}
	}
	
	private void displayFileList() {
		if(!multiModeEnabled || pathField.getText().isEmpty()) return;
		FileList fileList = new FileList(fileChooser);
		fileList.setModel(files);
		fileList.setVisible(true);
		setModel(fileList.getModel());
	}
	
	private List<File> getSelectedFiles() {
		if(multiModeEnabled) {
			return Arrays.asList(fileChooser.getSelectedFiles());
		}
		return Arrays.asList(fileChooser.getSelectedFile());
	}
	
	private void displayFileDialog() {
		while(true) {
			int r = saveModeEnabled ? fileChooser.showSaveDialog(null) : fileChooser.showOpenDialog(null);
			if(r != JFileChooser.APPROVE_OPTION) break;
			if(!saveModeEnabled && getSelectedFiles().stream().anyMatch(f -> !f.exists() ||
					!isValidExtension(Support.getFileExtension(f)))) {
				JOptionPane.showMessageDialog(null, "Please select a valid file to continue.",
						"Invalid File", JOptionPane.WARNING_MESSAGE);
				continue;
			}
			files.clear();
			getSelectedFiles().forEach(file -> {
				String filePath = file.getPath();
				if(saveModeEnabled && !fileChooser.getFileFilter().accept(file)) {
					filePath += defaultExtension;
				}
				files.add(new File(filePath));
			});
			
			setFieldText();
			break;
		}
	}
	
	private boolean isValidExtension(String extension) {
		if(validExtensions == null) return true;
		return validExtensions.contains(extension);
	}
	
	private void setFieldText() {
		setValue(String.join("; ", Support.transform(files, f -> f.getName())));
		pathField.setToolTipText(files.size() == 1 ? files.get(0).getPath() : null);
	}
}