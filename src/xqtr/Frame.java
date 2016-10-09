package xqtr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import xqtr.util.Button;
import xqtr.util.Form;
import xqtr.util.Section;
import xqtr.view.ChoiceView;

@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener, ItemListener {

	public MenuBar menu;
	public Section header;
	public Section footer;
	
	private Controller controller;
	private ChoiceView programSelector;
	private ChoiceView profileSelector;
	private Page page;
	
	public Frame(Controller controller) {
		this.controller = controller;
		initUI();
	}

	private void initUI() {
		
		menu = new MenuBar();
		setJMenuBar(menu);
		
		header = makeHeader();
		add(header, BorderLayout.NORTH);
		
		footer = makeFooter();
		add(footer, BorderLayout.SOUTH);
		
		page = new Page();
		add(page);
		
		setTitle(Application.name);
		setSize(440, 550);
		setMinimumSize(new Dimension(360, 240));
		setLocationRelativeTo(null); // centra la ventana en la pantalla
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("Icon.png").getImage());
	}
	
	private Section makeHeader() {
		
		Section header = new Section();
		header.setHeight(90);
		header.setBorder(0, 0, 1, 0);
		
		programSelector = new ChoiceView(controller.getExecutableProgramNames());
		programSelector.useComboMode(true);
		profileSelector = new ChoiceView(Arrays.asList("(Default)"));
		profileSelector.useComboMode(true);
		
		Form form = new Form();
		form.addElement("Program", programSelector);
		form.addElement("Profile", profileSelector);
		form.placeIn(header);
		
		return header;
	}
	
	private Section makeFooter() {
		
		Section footer = new Section();
		footer.setHeight(50);
		footer.setBorder(1, 0, 0, 0);
		
		Button runButton = new Button("E_xecute");
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
		JOptionPane.showMessageDialog(null, msg, "About", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void itemStateChanged(ItemEvent event) {
		
		if(event.getStateChange() == ItemEvent.SELECTED) {
			
			if(event.getSource() == programSelector.getComboBox()) {
				String programName = event.getItem().toString();
				setTitle(programName.isEmpty() ? Application.name : programName + " - " + Application.name);
				controller.setCurrentProgram(programName);
			} else if(event.getSource() == profileSelector.getComboBox()) {
				controller.setCurrentProfile(event.getItem().toString());
				JMenuItem parametersItem = (JMenuItem) menu.get("Parameters");
				parametersItem.setEnabled(true);
				parametersItem.addActionListener(e -> {
					new Parameters(page.print()).setVisible(true);
				});
			}
		}
	}
}
