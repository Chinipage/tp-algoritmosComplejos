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

public class Application {
	
	public static String name = "XQTR";
	public static String version = "0.3.2";

	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(() -> {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Model model = new Model();
			Frame mainFrame = new Frame(model);
			mainFrame.setVisible(true);
		});
	}
}
