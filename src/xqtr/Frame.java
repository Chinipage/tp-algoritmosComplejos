package xqtr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JFrame;

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
		
		setTitle("XQTR");
		setSize(440, 550);
		setMinimumSize(new Dimension(360, 240));
		setLocationRelativeTo(null); // centra la ventana en la pantalla
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		
		System.out.println("Button pressed: " + event.getActionCommand());
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
