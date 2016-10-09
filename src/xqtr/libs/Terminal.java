package xqtr.libs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DocumentFilter;

interface CommandListener {
	void commandOutput(String text);
	void commandCompleted(String cmd, int result);
	void commandFailed(Exception exp);
}

@SuppressWarnings("serial")
public class Terminal extends JPanel implements CommandListener {

	private JTextArea textArea;
	private int userInputStart;
	private Command command;
	
	public Terminal(String commandText) {
		command = new Command(this);
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		add(new JScrollPane(textArea));
		((AbstractDocument) textArea.getDocument()).setDocumentFilter(new CustomDocumentFilter());
		
		InputMap inputMap = textArea.getInputMap();
		ActionMap actionMap = textArea.getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke("control C"), "terminate-process");
		
		Action previousAction = actionMap.get("insert-break");
		actionMap.put("insert-break", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				previousAction.actionPerformed(e);
				try {
					int range = textArea.getCaretPosition() - userInputStart;
					String text = textArea.getText(userInputStart, range).trim();
					userInputStart += range;
					command.send(text + "\n");
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
		actionMap.put("terminate-process", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(textArea.getSelectedText() != null) {
					new DefaultEditorKit.CopyAction().actionPerformed(e);
				} else if(command.isRunning()) {
					terminate();
				}
			}
		});
		
		if(!command.isRunning()) {
			command.execute(commandText);
        }
	}
	
	private class Command {
		
		private CommandListener listener;
		private ProcessRunner runner;
		
		public Command(CommandListener listener) {
			this.listener = listener;
		}
		
		public boolean isRunning() {
			return runner != null && runner.isAlive();
		}
		
		public void execute(String cmd) {
			cmd = cmd.replaceAll("\"", " ").replaceAll("\\s+",  " ").trim();
			if(cmd.isEmpty()) return;
			List<String> values = Arrays.asList(cmd.split(" "));
			runner = new ProcessRunner(listener, values);
		}
		
		public void send(String cmd) {
			runner.write(cmd);
		}
		
		public void abort() {
			runner.interrupt();
		}
	}
	
	private class ProcessRunner extends Thread {
		
		private List<String> cmds;
        private CommandListener listener;
        private Process process;
        
        public ProcessRunner(CommandListener listener, List<String> cmds) {
        	this.cmds = cmds;
            this.listener = listener;
            start();
        }
        
        public void run() {
        	
        	try {
        		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
            	processBuilder.redirectErrorStream();
				process = processBuilder.start();
				StreamReader streamReader = new StreamReader(listener, process.getInputStream());
				
	        	int result = process.waitFor();
	        	
	        	streamReader.join();
	        	String cmd = cmds.stream().collect(Collectors.joining(" "));
	        	listener.commandCompleted(cmd, result);
			} catch(InterruptedException e) {
				process.destroy();
			} catch (Exception e) {
				listener.commandFailed(e);
			}
        }
        
        public void write(String text) {
        	
        	if(process != null && process.isAlive()) {
        		try {
        			OutputStream outputStream = process.getOutputStream();
					outputStream.write(text.getBytes());
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}
	
	private class StreamReader extends Thread {
		
		private InputStream inputStream;
        private CommandListener listener;
        
        public StreamReader(CommandListener listener, InputStream inputStream) {
        	this.inputStream= inputStream;
            this.listener = listener;
            start();
        }
        
        public void run() {
        	
        	try {
        		int value = -1;
				while((value = inputStream.read()) != -1) {
					listener.commandOutput(Character.toString((char) value));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	private class CustomDocumentFilter extends DocumentFilter {
		
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
        		throws BadLocationException {
            if (offset >= userInputStart) super.insertString(fb, offset, string, attr);
        }

        public void remove(FilterBypass fb, int offset, int length)
        		throws BadLocationException {
            if (offset >= userInputStart) super.remove(fb, offset, length);
        }
        
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
        		throws BadLocationException {
            if (offset >= userInputStart) super.replace(fb, offset, length, text, attrs);
        }
    }
	
	public void commandOutput(String text) {
		appendText(text);
	}
	
	public void commandCompleted(String cmd, int result) {
		appendText("\n----------\nProcess complete (Exit status " + result + ")\n");
		SwingUtilities.invokeLater(() -> textArea.setEditable(false));
	}
	
	public void commandFailed(Exception ex) {
		appendText("Command failed - " + ex.getMessage());
	}
	
	private void appendText(String text) {
		SwingUtilities.invokeLater(() -> { 
			textArea.append(text);
			int pos = textArea.getCaretPosition();
			textArea.setCaretPosition(textArea.getText().length());
			userInputStart = pos;
		});
	}
	
	public void terminate() {
		command.abort();
		appendText("\n----------\nProcess terminated\n");
		SwingUtilities.invokeLater(() -> textArea.setEditable(false));
	}
}
