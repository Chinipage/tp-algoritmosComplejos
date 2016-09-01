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

public class Application {

	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(() -> {
			
			Model model = new Model();
			Frame mainFrame = new Frame(model);
			mainFrame.setVisible(true);
		});
	}
}
