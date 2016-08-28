/**
 * XQTR App - TP Algoritmos II 2016
 */
package xqtr;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

public class App {
	public static void main(String[] args) {
		
		interfazBasica();
		/*JFrame f = new JFrame();
		JLabel l = new JLabel("こんにちは世界");
		l.setBounds(30, 20, 350, 50);
		l.setFont(l.getFont().deriveFont(50.0f));
		f.add(l);
		f.setSize(400,100);
		f.setLayout(null);
		f.setVisible(true);*/
	}

	//Prueba de interfaz basica para tp

	public static void interfazBasica(){

		//Panel superior. Aca se selecciona la app

		JLabel labelApp = new JLabel("Aplicacion:");

		JPanel panelSuperior = new JPanel();
		panelSuperior.add(labelApp);
		panelSuperior.setBackground(Color.red); //Para ver bien donde queda cada uno, despues lo borro
		panelSuperior.setPreferredSize(new Dimension( 600, 50 ));
		panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSuperior.setVisible(true);

		//Panel del medio. Configuraciones particulares de cada app
		JPanel panelMedio = new JPanel();
		panelMedio.setBackground(Color.blue); //Para ver bien donde queda cada uno, despues lo borro
		panelMedio.setPreferredSize(new Dimension( 600, 250 ));
		panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelMedio.setVisible(true);

		//Panel inferior. Consola y botones de "Ejecutar" "Cancelar" y "About"
		JTextArea consola = new JTextArea();
		consola.setPreferredSize(new Dimension(550, 300));
		JScrollPane scrollConsola = new JScrollPane (consola, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JPanel panelConsola = new JPanel();
		panelConsola.add(scrollConsola);

		JLabel labelConsola = new JLabel("Consola:");

		JPanel panelLabelConsola = new JPanel();
		panelLabelConsola.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabelConsola.add(labelConsola);

		JPanel panelInferior = new JPanel();
		panelInferior.add(panelLabelConsola, FlowLayout.LEFT);
		panelInferior.add(panelConsola);
		panelInferior.setBackground(Color.green); //Para ver bien donde queda cada uno, despues lo borro
		panelInferior.setPreferredSize(new Dimension( 600, 450 ));
		panelInferior.setVisible(true);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.add(panelSuperior);
		panelPrincipal.add(panelMedio);
		panelPrincipal.add(panelInferior);
		panelPrincipal.setVisible(true);

		//Frame de la App
		JFrame framePrincipal = new JFrame("Basheador");
		framePrincipal.add(panelPrincipal);
		framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framePrincipal.setSize(600, 750);
		framePrincipal.setResizable(false);
		framePrincipal.setVisible(true);
	}
}
