/**
 * XQTR App - TP Algoritmos II 2016
 */
package xqtr;
import javax.swing.*;

public class App {
	public static void main(String[] args) {
		
		JFrame f = new JFrame();
		JLabel l = new JLabel("こんにちは世界");
		l.setBounds(30, 20, 350, 50);
		l.setFont(l.getFont().deriveFont(50.0f));
		f.add(l);
		f.setSize(400,100);
		f.setLayout(null);
		f.setVisible(true);
	}
}
