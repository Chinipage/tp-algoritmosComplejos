package xqtr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import xqtr.libs.Terminal;
import xqtr.util.Dialog;

@SuppressWarnings("serial")
public class Result extends Dialog {

	public Result(String command) {
		
		History.log(command);
		Terminal terminal = new Terminal(command);
		add(terminal);
		
		setTitle("Result");
		setSize(480, 360);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				terminal.terminate();
			}
		});
		setVisible(true);
	}
}