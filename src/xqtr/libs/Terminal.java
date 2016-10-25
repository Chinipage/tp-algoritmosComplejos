package xqtr.libs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import xqtr.util.Support;

interface CommandListener {
	void commandOutput(String text);
	void commandCompleted(int result);
	void commandFailed(Exception exp);
}

@SuppressWarnings("serial")
public class Terminal extends JPanel implements CommandListener {

	private JTextPane textArea;
	private int userInputStart;
	private Command command;
	
	public Terminal(String commandText) {
		
		command = new Command(this);
		setLayout(new BorderLayout());
		textArea = new JTextPane();
		textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setInputAttributes();
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
		
		Support.setTimeout(100, () -> {
			if(!command.isRunning()) {
				command.execute(commandText);
			}
		});
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
			cmd = cmd.replaceAll("\\s+",  " ").trim();
			if(cmd.isEmpty()) return;
			
			List<String> cmds = Support.listFromString("bash, -c, " + cmd);
			
			runner = new ProcessRunner(listener, cmds);
		}
		
		public void send(String cmd) {
			runner.write(cmd);
		}
		
		public void abort() {
			runner.interrupt();
		}
	}
	
	private class ProcessRunner extends Thread {
		
		private List<String> cmd;
        private CommandListener listener;
        private Process process;
        
        public ProcessRunner(CommandListener listener, List<String> cmd) {
        	this.cmd = cmd;
            this.listener = listener;
            start();
        }
        
        public void run() {
        	
        	try {
            	ProcessBuilder processBuilder = new ProcessBuilder(cmd);
    			process = processBuilder.start();
    			StreamReader stream = new StreamReader(listener,
    					process.getInputStream(), process.getErrorStream());
    			
    			int result = process.waitFor();
    			stream.join();
    	       	
    	        listener.commandCompleted(result);
    		} catch(InterruptedException e) {
    			process.destroy();
    		} catch (Exception e) {
    			listener.commandFailed(e);
    		}
        }
        
        public void write(String text) {
        	if(process == null || !process.isAlive()) return;
            try {
            	OutputStream outputStream = process.getOutputStream();
    			outputStream.write(text.getBytes());
    			outputStream.flush();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
	}
	
	private class StreamReader extends Thread {
		
		private InputStream stream;
		private InputStream error;
        private CommandListener listener;
        
        public StreamReader(CommandListener listener, InputStream stream, InputStream error) {
        	this.stream = stream;
        	this.error = error;
            this.listener = listener;
            start();
        }
        
        public void run() {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(error));
			boolean start = true;
			try {
				int value = -1;
				Thread.sleep(100);
				do {
					while(start && reader.ready()) {
						setErrorAttributes();
						listener.commandOutput(reader.readLine() + "\n");
					}
					if(value > -1) {
						String output = Character.toString((char) value);
						setOutputAttributes();
						listener.commandOutput(output);
						start = output.equals("\\n");
					}
					setInputAttributes();
				} while((value = stream.read()) != -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	private class CustomDocumentFilter extends DocumentFilter {
		
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
        		throws BadLocationException {
            if(offset >= userInputStart) super.insertString(fb, offset, string, attr);
        }

        public void remove(FilterBypass fb, int offset, int length)
        		throws BadLocationException {
        	if(offset == userInputStart || offset + 1 == userInputStart) {
        		Support.delay(() -> setInputAttributes());
        	}
            if(offset >= userInputStart) super.remove(fb, offset, length);
        }
        
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
        		throws BadLocationException {
            if(offset >= userInputStart) super.replace(fb, offset, length, text, attrs);
        }
    }
	
	public void commandOutput(String text) {
		appendText(text);
	}
	
	public void commandCompleted(int result) {
		setInfoAttributes();
		appendText("\n----------\nProcess complete (Exit status " + result + ")\n");
		Support.delay(() -> textArea.setEditable(false));
	}
	
	public void commandFailed(Exception ex) {
		setErrorAttributes();
		appendText(ex.getMessage());
		Support.delay(() -> textArea.setEditable(false));
	}
	
	public void terminate() {
		
		command.abort();
		
		boolean finalNewline = textArea.getText().lastIndexOf("\n") == textArea.getText().length() - 1;
		String text = (finalNewline ? "" : "\n") + "\n----------\nProcess terminated\n";
		
		setInfoAttributes();
		appendText(text);
		Support.delay(() -> textArea.setEditable(false));
	}
	
	private void appendText(String text) {
		textArea.replaceSelection(text);
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		userInputStart = len;
    }
	
	private void setOutputAttributes() {
		setAttributes(Color.BLACK, false);
	}
	
	private void setInputAttributes() {
		setAttributes(Color.BLACK, true);
	}
	
	private void setErrorAttributes() {
		setAttributes(Color.RED, true);
	}
	
	private void setInfoAttributes() {
		setAttributes(Color.BLUE, true);
	}
	
	private void setAttributes(Color color, boolean bold) {
		StyleContext styleContext = StyleContext.getDefaultStyleContext();
		AttributeSet attributeSet = SimpleAttributeSet.EMPTY;
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Foreground, color);
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Bold, bold);
		textArea.setCharacterAttributes(attributeSet, false);
	}
}
