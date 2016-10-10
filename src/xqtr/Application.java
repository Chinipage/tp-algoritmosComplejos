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

import java.io.File;

import javax.swing.UIManager;

import xqtr.libs.UndoHandler;
import xqtr.util.Support;

public class Application {
	
	public static final String name = "XQTR";
	public static final String version = "0.8";
	public static final String configPath = "Config.xml";
	public static final File errorLogPath = new File("error.log");
	
	public static Frame frame;
	public static Controller controller;
	public static UndoHandler undoHandler = new UndoHandler();

	public static void main(String[] args) {
		Support.delay(() -> {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			controller = Controller.getInstance();
			frame = new Frame(controller); 
			frame.setVisible(true);
		});
	}   
}
