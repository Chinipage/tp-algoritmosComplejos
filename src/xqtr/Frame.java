package xqtr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import xqtr.util.Button;
import xqtr.util.ComboBox;
import xqtr.util.Form;
import xqtr.util.Section;
import xqtr.util.Support;

@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener, ItemListener {

	public MenuBar menu;
	public Section header;
	public Section footer;
	public Page page;
	public Button runButton;
	
	private Controller controller;
	private ComboBox programSelector;
	private ComboBox profileSelector;
	
	public Frame(Controller controller) {
		this.controller = controller;
		initUI();
	}

	private void initUI() {
		
		JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
		loadingLabel.setIcon(new ImageIcon("Spinner.gif"));
		add(loadingLabel); 
		
		menu = new MenuBar();
		setJMenuBar(menu);
		
		Support.setTimeout(50, () -> {
			header = makeHeader();
			add(header, BorderLayout.NORTH);
			
			footer = makeFooter();
			add(footer, BorderLayout.SOUTH);
			
			page = new Page();
			remove(loadingLabel);
			add(page);
			revalidate();
		});
		
		setTitle(Application.name);
		setFrameSize();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("Icon.png").getImage());
	}
	
	private void setFrameSize() {
		setMinimumSize(new Dimension(360, 240));
		int width = Integer.parseInt(Application.properties.get("frame.width"));
		int height = Integer.parseInt(Application.properties.get("frame.height"));
		setSize(width, height);
		if(Boolean.parseBoolean(Application.properties.get("frame.maximized"))) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}
	
	private Section makeHeader() {
		
		Section header = new Section();
		header.setVisible(Boolean.parseBoolean(Application.properties.get("header.visible")));
		header.setBorder(0, 0, 1, 0);
		
		programSelector = new ComboBox();
		profileSelector = new ComboBox();
		
		Form form = new Form();
		form.addElement("Program", programSelector);
		form.addElement("Profile", profileSelector);
		form.placeIn(header);			
		
		controller.whenReady(() -> {
			programSelector.setModel(controller.getProgramsNames());
			profileSelector.setModel(Support.map(e -> e, "(Default)"));
		});
		
		return header;
	}
	
	private Section makeFooter() {
		
		Section footer = new Section();
		footer.setVisible(Boolean.parseBoolean(Application.properties.get("footer.visible")));
		footer.setBorder(1, 0, 0, 0);
		
		runButton = new Button("E_xecute");
		runButton.setEnabled(false);
		Button aboutButton = new Button("_About");
		
		footer.add(runButton, BorderLayout.EAST);
		footer.add(aboutButton, BorderLayout.WEST);
		
		return footer;
	}
	
	public void actionPerformed(ActionEvent event) {
		
		switch(event.getActionCommand()) {
		case "Execute":
			execute();
			break;
		case "About":
			showAboutDialog();
			break;
		}
	}
	
	public void execute() {
		new Result("ping -c 3 google.com");
	}
	
	public void showAboutDialog() {
		String msg = "<html><h1>" + Application.name + "</h1>";
		msg += "<h2>Executor App v" + Application.version + "</h2>";
		msg += "<h3>Made by:</h3>";
		msg += "<ul><li>&nbsp;Martínez, Andrés</li>";
		msg += "<li>&nbsp;Rodríguez Arias, Mariano</li>";
		msg += "<li>&nbsp;Vigilante, Federico</li></ul>";
		msg += "<h3>Made for:</h3>";
		msg += "<ul><li>&nbsp;Sznajdleder, Pablo Augusto</li>";
		msg += "<li>&nbsp;Algoritmos Complejos para Estructuras de Datos Avanzadas";
		msg += new String(new char[15]).replace("\0", "&nbsp;");
		msg += "</li><li>&nbsp;Facultad Regional Buenos Aires</li>";
		msg += "<li>&nbsp;Universidad Tecnológica Nacional</li></ul>";
		msg += "<h3>Made with:</h3>";
		msg += "<ul><li>&nbsp;Ubuntu 16.04 Xenial Xerus</li>";
		msg += "<li>&nbsp;Eclipse Neon 4.6 for Java Developers</li>";
		msg += "<li>&nbsp;Java SE Development Kit 8 (OpenJDK 8)</li>";
		msg += "<li>&nbsp;Git version control system with GitHub</li></ul>";
		msg += "</html>";
		Support.displayMessage("About: " + msg);
	}
	
	public void itemStateChanged(ItemEvent event) {
		
		if(event.getStateChange() == ItemEvent.SELECTED) {
			
			if(event.getSource() == programSelector) {
				String programName = event.getItem().toString();
				setTitle(programName.isEmpty() ? Application.name : programName + " - " + Application.name);
				controller.setCurrentProgram(programName);
			} else if(event.getSource() == profileSelector) {
				controller.setCurrentProfile(event.getItem().toString());
				menu.getItem("Parameters").setEnabled(true);
			}
		}
	}
}
