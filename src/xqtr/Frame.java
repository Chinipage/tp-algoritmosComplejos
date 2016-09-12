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
import javax.swing.JOptionPane;

import xqtr.util.Button;
import xqtr.util.ComboBox;
import xqtr.util.Form;
import xqtr.util.Section;

@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener, ItemListener {

	private Model model;
	
	private ComboBox programComboBox;
	private ComboBox profileComboBox;
	
	public Frame(Model model) {
		
		this.model = model;
		initUI();
	}

	private void initUI() {
		
		add(makeHeader(), BorderLayout.NORTH);
		add(makeFooter(), BorderLayout.SOUTH);
		
		add(new Page());
		
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
		
		programComboBox = new ComboBox(model.getPrograms());
		profileComboBox = new ComboBox(Arrays.asList("(Default)"));
		
		Form form = new Form();
		form.addElement("Program", programComboBox);
		form.addElement("Profile", profileComboBox);
		form.placeIn(header);
		
		return header;
	}
	
	private Section makeFooter() {
		
		Section footer = new Section();
		footer.setHeight(50);
		footer.setBorder(1, 0, 0, 0);
		
		Button runButton = new Button("_Execute");
		Button aboutButton = new Button("_About");
		
		footer.add(runButton, BorderLayout.EAST);
		footer.add(aboutButton, BorderLayout.WEST);
		
		return footer;
	}
	
	public void actionPerformed(ActionEvent event) {
		
		switch(event.getActionCommand()) {
		case "Execute":
			JOptionPane.showMessageDialog(null, "<html><i>TO-DO");
			break;
		case "About":
			String aboutText = "<html><h1>" + Application.name + "</h1>";
			aboutText += "<h2>Executor App v" + Application.version + "</h2>";
			aboutText += "<h3>Made by:</h3>";
			aboutText += "<ul><li>&nbsp;Martínez, Andrés</li>";
			aboutText += "<li>&nbsp;Rodríguez Arias, Mariano</li>";
			aboutText += "<li>&nbsp;Vigilante, Federico</li></ul>";
			aboutText += "<h3>Made for:</h3>";
			aboutText += "<ul><li>&nbsp;Sznajdleder, Pablo Augusto</li>";
			aboutText += "<li>&nbsp;Algoritmos Complejos para Estructuras de Datos Avanzadas";
			aboutText += new String(new char[15]).replace("\0", "&nbsp;");
			aboutText += "</li><li>&nbsp;Facultad Regional Buenos Aires</li>";
			aboutText += "<li>&nbsp;Universidad Tecnológica Nacional</li></ul>";
			aboutText += "<h3>Made with:</h3>";
			aboutText += "<ul><li>&nbsp;Ubuntu 16.04 Xenial Xerus</li>";
			aboutText += "<li>&nbsp;Eclipse Neon 4.6 for Java Developers</li>";
			aboutText += "<li>&nbsp;Java SE Development Kit 8 (OpenJDK 8)</li>";
			aboutText += "<li>&nbsp;Git version control system with GitHub</li></ul>";
			aboutText += "</html>";
			JOptionPane.showMessageDialog(null, aboutText);
			break;
		}
	}
	
	public void itemStateChanged(ItemEvent event) {
		
		if(event.getStateChange() == ItemEvent.SELECTED) {
			
			if (event.getSource() == programComboBox) {
				String programName = event.getItem().toString();
				setTitle(programName.isEmpty() ? "XQTR" : programName + " - XQTR");
			}
		}
	}
}
