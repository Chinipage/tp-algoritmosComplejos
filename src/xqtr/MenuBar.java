package xqtr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import xqtr.util.Support;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private List<JMenu> menus = new ArrayList<>();
	private List<JMenuItem> items = new ArrayList<>(); 
	
	private ButtonGroup programs = new ButtonGroup();
	private ButtonGroup profiles = new ButtonGroup();
	
	public MenuBar() {
		setVisible(false);
		Support.setTimeout(1000, () -> {
			addMenus();
			setVisible(Boolean.parseBoolean(Application.properties.get("menubar.visible")));
		});
	}
	
	private void addMenus() {
		addProgramAndProfile();
		add("File/-");
		add("File/_Execute|E", e -> Application.frame.execute()).setEnabled(false);
		add("File/_Reload Config|R", e -> {
			getItem("Execute").setEnabled(false);
			Application.frame.runButton.setEnabled(false);
			Application.frame.load();
			Support.delay(() -> Application.controller.loadConfig());
		});
		add("File/-");
		add("File/_Quit|Q", e -> Support.delay(() -> System.exit(0)));
		
		add("_Edit/Undo").setAction(Application.undoHandler.getUndoAction());
		add("Edit/Redo").setAction(Application.undoHandler.getRedoAction());
		add("Edit/-");
		addEditUtils();
		
		add("_View/");
		addViewToggles();
		
		add("_Tools/");
		add("Tools/_Configuration", e -> new ConfigSource());
		add("Tools/_Parameters", e -> new Parameters(Application.frame.page.print())).setEnabled(false);
		add("Tools/Command _History", e -> new History());
		add("Tools/Error _Log", e -> new ErrorLog());
		
		add(Box.createHorizontalGlue());
		add("_Help/_About " + Application.name, e -> Application.frame.showAboutDialog());
	}
	
	private void addProgramAndProfile() {
		add("_File/_Program/");
		add("File/P_rofile/");
		
		JMenu program = getMenu("Program");
		JMenu profile = getMenu("Profile");
		
		Support.setInterval(500, s -> {
			program.setEnabled(Application.frame.programSelector.getItemCount() > 0);
			profile.setEnabled(Application.frame.profileSelector.getItemCount() > 0 &&
					Application.controller.hasCurrentProgram());
		});
	}
	
	private void setRadioItems(List<String> items, JMenu menu, ButtonGroup group, ActionListener action) {
		menu.removeAll();
		Collections.list(group.getElements()).forEach(item -> group.remove(item));
		items.forEach(item -> {
			JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem(item);
			radioItem.addActionListener(action);
			menu.add(radioItem);
			group.add(radioItem);
		});
	}
	
	public void setPrograms(List<String> items) {
		setRadioItems(items, getMenu("Program"), programs, e -> {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
			Application.frame.programSelector.setSelectedItem(item.getText());
		});
	}
	
	public void setProfiles(List<String> items) {
		setRadioItems(items, getMenu("Profile"), profiles, e -> {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
			Application.frame.profileSelector.setSelectedItem(item.getText());
		});
	}
	
	private void selectRadioItem(String text, ButtonGroup group) {
		Collections.list(group.getElements()).forEach(item -> 
			item.setSelected(item.getText().equals(text))
		);
	}
	
	public void selectProgram(String text) {
		selectRadioItem(text, programs);
	}
	
	public void selectProfile(String text) {
		selectRadioItem(text, profiles);
	}
	
	private void addEditUtils() {
		JMenuItem cutItem = add("Edit/Cut"); 
		cutItem.setAction(new DefaultEditorKit.CutAction());
		cutItem.setText("Cut");
		cutItem.setMnemonic('T');
		cutItem.setAccelerator(KeyStroke.getKeyStroke("control X"));
		
		JMenuItem copyItem = add("Edit/Copy"); 
		copyItem.setAction(new DefaultEditorKit.CopyAction());
		copyItem.setText("Copy");
		copyItem.setMnemonic('C');
		copyItem.setAccelerator(KeyStroke.getKeyStroke("control C"));
		
		JMenuItem pasteItem = add("Edit/Paste"); 
		pasteItem.setAction(new DefaultEditorKit.PasteAction());
		pasteItem.setText("Paste");
		pasteItem.setMnemonic('P');
		pasteItem.setAccelerator(KeyStroke.getKeyStroke("control V"));
		
		Action action = Support.find(a -> a.getValue(Action.NAME).equals(DefaultEditorKit.selectAllAction),
				Support.list(new JTextField().getActions()));
		
		add("Edit/_Delete").setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		JMenuItem selectAllItem = add("Edit/Select All");
		selectAllItem.setAction(action);
		selectAllItem.setText("Select All");
		selectAllItem.setMnemonic('S');
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke("control A"));
	}
	
	private void addViewToggles() {
		JMenu viewMenu = getMenu("View");
		
		JCheckBoxMenuItem headerItem = new JCheckBoxMenuItem("Header");
		headerItem.setMnemonic('H');
		headerItem.setSelected(Application.frame.header.isVisible());
		headerItem.addItemListener(e -> {
			Application.frame.header.setVisible(e.getStateChange() == ItemEvent.SELECTED);
		});
		viewMenu.add(headerItem);
		
		JCheckBoxMenuItem footerItem = new JCheckBoxMenuItem("Footer");
		footerItem.setMnemonic('F');
		footerItem.setSelected(Application.frame.footer.isVisible());
		footerItem.addItemListener(e -> {
			Application.frame.footer.setVisible(e.getStateChange() == ItemEvent.SELECTED);
		});
		viewMenu.add(footerItem);
	}
	
	public JMenu getMenu(String name) {
		return Support.find(o -> o.getText().equals(name), menus);
	}
	
	public JMenuItem getItem(String name) {
		return Support.find(o -> o.getText().equals(name), items);
	}
	
	public JMenuItem add(String path) {
		return add(path, (ActionListener) null);
	}
	
	public JMenuItem add(String path, ActionListener action) {
		
		Optional<Character> accelerator = Optional.empty();
		if(path.charAt(path.length() - 2) == '|') {
			accelerator = Optional.of(path.charAt(path.length() - 1));
			path = path.substring(0, path.length() - 2);
		}
		
		String[] names = path.split("/");
		boolean asItem = path.charAt(path.length() - 1) != '/';
		
		JComponent parent = this;
		for(String name : names) {
			
			if(asItem && name.equals(names[names.length - 1])) {
				if(name.equals("-")) {
					((JMenu) parent).addSeparator();
				} else {
					JMenuItem item = new JMenuItem(name.replaceFirst("_", ""));
					Support.getMnemonic(name).ifPresent(mnemonic -> item.setMnemonic(mnemonic));
					item.addActionListener(action);
					item.setEnabled(action != null);
					accelerator.ifPresent(a -> 
						item.setAccelerator(KeyStroke.getKeyStroke(a.charValue(), ActionEvent.CTRL_MASK))
					);
					parent.add(item);
					items.add(item);
					return item;
				}
				break;
			}
			
			JMenu _menu = getMenu(name);
			if(_menu != null) {
				parent = _menu;
			} else {
				JMenu menu = new JMenu(name.replaceFirst("_", ""));
				Support.getMnemonic(name).ifPresent(mnemonic -> menu.setMnemonic(mnemonic));
				parent.add(menu);
				parent = menu;
				menus.add(menu);
			}
		}
		return null;
	}
}
