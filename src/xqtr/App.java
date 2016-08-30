/**
 * XQTR App - TP Algoritmos II 2016
 */
package xqtr;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class App extends JFrame implements ActionListener, ItemListener {

	private JComboBox<String> programComboBox;
	private String[] programs;
	
	public App() {

		initUI();
	}

	private void initUI() {
		
		Section topSection = new Section();
		topSection.setHeight(80);
		topSection.setBorder(0, 0, 1, 0);
		
		programs = new String[]{"", "Guava", "Lozenge", "Cashew", "Goon", "Wobble", "Bobbin", "Noodle"};
		programComboBox = new JComboBox<>(programs);
		programComboBox.addItemListener(this);
		
		Form form = new Form();
		form.addElement("Program", programComboBox);
		form.addElement("Profile", new JComboBox<>());
		form.placeIn(topSection);
		
		Section bottomSection = new Section();
		bottomSection.setHeight(50);
		bottomSection.setBorder(1, 0, 0, 0);
		
		Button runButton = new Button("_Execute");
		Button aboutButton = new Button("_About");
		
		bottomSection.add(runButton, BorderLayout.EAST);
		bottomSection.add(aboutButton, BorderLayout.WEST);
		
		add(topSection, BorderLayout.NORTH);
		add(bottomSection, BorderLayout.SOUTH);

		setTitle("XQTR");
		setSize(480, 640);
		setMinimumSize(new Dimension(360, 420));
		setLocationRelativeTo(null); // centra la ventana en la pantalla
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent event) {
		
		System.out.println("Button pressed: " + event.getActionCommand());
	}
	
	public void itemStateChanged(ItemEvent event) {
		
		if(event.getStateChange() == ItemEvent.SELECTED) {
			String programName = event.getItem().toString();
			setTitle(programName + " - XQTR");
			System.out.println("Selected program: " + programName);
		}
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				App app = new App();
				app.setVisible(true);
			}
		});
	}
}

// public static void main(String[] args) {

// interfazBasica();
// }

// Prueba de interfaz basica para tp

// public static void interfazBasica(){
//
// //Panel superior. Aca se selecciona la app
//
// JLabel labelApp = new JLabel("Aplicacion:");
//
// JPanel panelSuperior = new JPanel();
// panelSuperior.add(labelApp);
// panelSuperior.setBackground(Color.red); //Para ver bien donde queda cada uno,
// despues lo borro
// panelSuperior.setPreferredSize(new Dimension( 600, 50 ));
// panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
// panelSuperior.setVisible(true);
//
// //Panel del medio. Configuraciones particulares de cada app
// JPanel panelMedio = new JPanel();
// panelMedio.setBackground(Color.blue); //Para ver bien donde queda cada uno,
// despues lo borro
// panelMedio.setPreferredSize(new Dimension( 600, 250 ));
// panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
// panelMedio.setVisible(true);
//
// //Panel inferior. Consola y botones de "Ejecutar" "Cancelar" y "About"
// JTextArea consola = new JTextArea();
// consola.setPreferredSize(new Dimension(550, 300));
// JScrollPane scrollConsola = new JScrollPane (consola,
// JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
// JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//
// JPanel panelConsola = new JPanel();
// panelConsola.add(scrollConsola);
//
// JLabel labelConsola = new JLabel("Consola:");
//
// JPanel panelLabelConsola = new JPanel();
// panelLabelConsola.setLayout(new FlowLayout(FlowLayout.LEFT));
// panelLabelConsola.add(labelConsola);
//
// JPanel panelInferior = new JPanel();
// panelInferior.add(panelLabelConsola, FlowLayout.LEFT);
// panelInferior.add(panelConsola);
// panelInferior.setBackground(Color.green); //Para ver bien donde queda cada
// uno, despues lo borro
// panelInferior.setPreferredSize(new Dimension( 600, 450 ));
// panelInferior.setVisible(true);
//
// JPanel panelPrincipal = new JPanel();
// panelPrincipal.add(panelSuperior);
// panelPrincipal.add(panelMedio);
// panelPrincipal.add(panelInferior);
// panelPrincipal.setVisible(true);
//
// //Frame de la App
// JFrame framePrincipal = new JFrame("Basheador");
// framePrincipal.add(panelPrincipal);
// framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// framePrincipal.setSize(600, 750);
// framePrincipal.setResizable(false);
// framePrincipal.setVisible(true);
// }
// }
