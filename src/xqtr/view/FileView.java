package xqtr.view;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;

import xqtr.Application;
import xqtr.util.Button;
import xqtr.util.FileTypeFilter;
import xqtr.util.Support;
import xqtr.util.FileList;

@SuppressWarnings("serial")
public class FileView extends AbstractControl {

	private JTextField pathField = new JTextField();
	private Button browseButton = new Button("Browse");
	private JFileChooser fileChooser = new JFileChooser();
	private boolean saveModeEnabled = false;
	private boolean multiModeEnabled = false;
	private String defaultExtension = "";
	private List<String> validExtensions;
	private List<File> model;
	private ActionListener deleteAction = e -> pathField.setText("");
	
	public FileView() {
		this(new ArrayList<>());
	}
	
	public FileView(List<File> model) {
		setModel(model);
		setDefaultDirectory();
		
		Application.undoHandler.handle(pathField);
		pathField.setFont(defaultFont);
		pathField.setEditable(false);
		pathField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { browseButton.setMnemonic('B'); }
			public void focusLost(FocusEvent e) { browseButton.setMnemonic(0); }
		});
		pathField.setTransferHandler(new FileDroppableField());
		Support.addChangeListener(pathField, e -> changeListener(e));
		Support.addMouseListener(pathField, "CLICK", e -> displayFileList());
		Support.addKeyListener(pathField, "SPACE", e -> displayFileList());
		Support.addKeyListener(pathField, "DELETE", deleteAction);
		browseButton.addActionListener(e -> addFilesWithChooser());
		
		add(pathField);
		add(createSeparator());
		add(browseButton);
	}
	
	private void setDefaultDirectory() {
		
		String path = Application.properties.get("browser.default.dir");
		path = path.replaceFirst("^~", System.getProperty("user.home"));
		File dir = new File(path);
		if(dir != null && dir.exists() && dir.isDirectory()) {
			fileChooser.setCurrentDirectory(dir);
		}
	}
	
	public void setModel(List<File> model) {
		
		this.model = Support.map(file -> {
			if(file.getPath().indexOf('/') == -1) {
				file = new File(fileChooser.getCurrentDirectory().getPath() + "/" + file.getPath());
			}
			if(saveModeEnabled && !hasValidExtension(file)) {
				file = new File(file.getPath() + defaultExtension);
			}
			return file;
		}, model);
		
		setPathField();
	}
	
	private void setPathField() {
		if(model.isEmpty()) {
			pathField.setText("");
			pathField.setToolTipText(null);
			return;
		}
		
		if(model.size() == 1) {
			pathField.setText(model.get(0).getName());
			pathField.setToolTipText(model.get(0).getPath());
		} else {
			pathField.setText(String.join("; ", Support.map(f -> f.getName(), model)));
			pathField.setToolTipText(null);
		}
	}
	
	public void setFormat(String format) {
		FileTypeFilter filter = new FileTypeFilter(format);
		fileChooser.setFileFilter(filter);
		validExtensions = filter.getExtensions();
		
		int i = format.indexOf(" ");
		defaultExtension = "." + (i == -1 ? format : format.substring(0, i));
	}
	
	private boolean hasValidExtension(File file) {
		if(validExtensions == null) return true;
		return validExtensions.contains(Support.getFileExtension(file));
	}
	
	public void setSaveModeEnabled(boolean newValue) {
		saveModeEnabled = newValue;
	}
	
	public void setMultiModeEnabled(boolean newValue) {
		multiModeEnabled = newValue;
		fileChooser.setMultiSelectionEnabled(newValue);
	}
	
	private void displayFileList() {
		if(!multiModeEnabled || pathField.getText().isEmpty()) return;
		FileList fileList = new FileList(fileChooser);
		fileList.setModel(model);
		fileList.setVisible(true);
		setModel(fileList.getModel());
	}
	
	private List<File> getSelectedFiles() {
		if(multiModeEnabled) {
			return Support.map(null, fileChooser.getSelectedFiles());
		} else {
			return Support.map(null, fileChooser.getSelectedFile());
		}
	}
	
	private boolean areFilesValid(List<File> files) {
		if(saveModeEnabled) {
			return files.stream().allMatch(f -> Support.getFileExtension(f).isEmpty() || hasValidExtension(f));
		} else {
			return files.stream().allMatch(f -> f.exists() && hasValidExtension(f));
		}
	}
	
	private boolean showFileChooser() {
		int r = saveModeEnabled ? fileChooser.showSaveDialog(null) : fileChooser.showOpenDialog(null);
		return r == JFileChooser.APPROVE_OPTION;
	}
	
	private void addFilesWithChooser() {
		while(showFileChooser()) {
			List<File> files = getSelectedFiles();
			if(areFilesValid(files)) {
				setModel(files);
				break;
			}
			
			Support.displayMessage("Warning: Invalid file");
		}
	}
	
	private class FileDroppableField extends TransferHandler {
		
		@SuppressWarnings("unchecked")
		private List<File> getDroppedFiles(TransferSupport info) {
			List<File> droppedFiles = null;
			if(canImport(info)) try {
				Transferable transferable = info.getTransferable();
				droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
			return droppedFiles;
		}
		
		public boolean canImport(TransferSupport info) {
			return info.isDrop() && info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
		}
		
		public boolean importData(TransferSupport info) {
			List<File> files = getDroppedFiles(info);
			if(files == null) return false;
			files = Support.filter(file -> areFilesValid(Support.map(null, file)), files);
			if(!files.isEmpty()) {
				setModel(files);
			}
			return true;
		}
	}
	
	public void setValue(String value) {
		setModel(Support.map(File::new, Support.listFromString(value)));
	}
	
	public String getValue() {
		if(pathField.getText().isEmpty()) return "";
		return model.stream().map(f -> f.getPath()).reduce("", (a,b) -> a + " \"" + b + "\"").trim();
	}
	
	private void changeListener(ChangeEvent e) {
		JMenuItem deleteItem = Application.frame.menu.getItem("Delete");
		switch((String) e.getSource()) {
		case "focus":
			Support.delay(() -> {
				deleteItem.setEnabled(!pathField.getText().isEmpty());
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
			deleteItem.setEnabled(!pathField.getText().isEmpty());
		}
	}
}