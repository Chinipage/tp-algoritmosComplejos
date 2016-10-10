package xqtr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.text.DefaultEditorKit;

import xqtr.util.Support;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private List<JMenu> menus = new ArrayList<>();
	private List<JMenuItem> items = new ArrayList<>(); 
	
	public MenuBar() {
		setVisible(false);
		Support.setTimeout(1000, () -> {
			addMenus();
			setVisible(Boolean.parseBoolean(Application.properties.get("menubar.visible")));
		});
	}
	
	private void addMenus() {
		add("_File/_Program/");
		add("File/P_rofile/");
		add("File/-");
		add("File/_Execute|E", e -> Application.frame.execute());
		add("File/_Reload Config|R", e -> Application.controller.loadConfig());
		add("File/-");
		add("File/_Quit|Q", e -> System.exit(0));
		
		add("_Edit/Undo").setAction(Application.undoHandler.getUndoAction());
		add("Edit/Redo").setAction(Application.undoHandler.getRedoAction());
		add("Edit/-");
		addEditUtils();
		
		add("_View/");
		addViewToggles();
		
		add("_Tools/");
		add("Tools/_Configuration", e -> new ConfigSource());
		add("Tools/_Parameters");
		add("Tools/Command _History", e -> Support.displayMessage("TO-DO"));
		add("Tools/Error _Log", e -> new ErrorLog());
		
		add(Box.createHorizontalGlue());
		add("_Help/_About " + Application.name, e -> Application.frame.showAboutDialog());
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
		
		Action action = Arrays.asList(new JTextField().getActions()).stream().filter(a -> {
			return a.getValue(Action.NAME).equals(DefaultEditorKit.selectAllAction);
		}).findFirst().get();
		
		add("Edit/_Delete").setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		JMenuItem selectAllItem = add("Edit/Select All");
		selectAllItem.setAction(action);
		selectAllItem.setText("Select All");
		selectAllItem.setMnemonic('S');
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke("control A"));
	}
	
	private void addViewToggles() {
		JMenu viewMenu = (JMenu) get("View");
		
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
	
	public MenuElement get(String name) {
		return items.stream().filter(o -> o.getText().equals(name)).findFirst().orElse(
				menus.stream().filter(o -> o.getText().equals(name)).findFirst().orElse(null));
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
			
			Optional<JMenu> _menu = menus.stream().filter(o -> o.getText().equals(name)).findFirst(); 
			if(_menu.isPresent()) {
				parent = _menu.get();
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
