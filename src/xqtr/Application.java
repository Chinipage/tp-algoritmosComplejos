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

import xqtr.libs.UndoHandler;
import xqtr.util.Support;
import xqtr.util.UserProperties;

public class Application {
	
	public static final String name = "XQTR";
	public static final String version = "0.9.9";	
	
	public static Frame frame;
	public static Controller controller;
	public static UserProperties properties;
	public static UndoHandler undoHandler = new UndoHandler();

	public static void main(String[] args) {
		Support.delay(() -> {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			properties = new UserProperties();
			History.init();
			controller = Controller.getInstance();
			frame = new Frame(controller); 
			frame.setVisible(true);
		});
	}
}
