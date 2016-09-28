/**
 * XQTR App
 * TP Algoritmos II
 * UTN FRBA - 2016
 * 
 * Grupo 3
 * Martínez, Andrés
 * Rodríguez Arias, Mariano
 * Vigilante, Federico
 */

package xqtr;

import javax.swing.UIManager;

import xqtr.util.Support;

public class Application {
	
	public static String name = "XQTR";
	public static String version = "0.4";
	public static Frame frame;

	public static void main(String[] args) {
		
		Support.delay(() -> {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			frame = new Frame(Controller.getInstance()); 
			frame.setVisible(true);
		});
	}
}
