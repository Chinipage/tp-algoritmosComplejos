package xqtr;

import java.awt.FlowLayout;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import xqtr.util.Section;
import xqtr.util.Support;
import xqtr.view.Control;
import xqtr.model.ParameterNode;
import xqtr.util.Form;

@SuppressWarnings("serial")
public class Page extends Section {
	
	private JScrollPane scrollPane;
	private JLabel loadingLabel;
	private boolean isLoading;
	public Form form;
	
	public Page() {
		setPadding(5);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		Section view = new Section();
		view.setLayout(new FlowLayout());
		view.setPadding(5,0,5,0);
		scrollPane.setViewportView(view);
		
		loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
		loadingLabel.setIcon(new ImageIcon(Support.getImageResource("Spinner.gif")));
		
		form = new Form();
		
		Support.setInterval(500, s -> {
			boolean isExecutable = form.isFilled();
			if(Application.controller.isReady()) {
				Application.frame.runButton.setEnabled(isExecutable);
				Application.frame.menu.getItem("Execute").setEnabled(isExecutable);
			}
		});
	}
	
	public void toggleLoading() {
		if(isLoading) {
			remove(loadingLabel);
			add(scrollPane);
			isLoading = false;
		} else {
			remove(scrollPane);
			add(loadingLabel);
			isLoading = true;
		}
		repaintFrame();
	}
	
	public void load() {
		toggleLoading();
		clear();
		
		Application.controller.whenReady(() -> {
			Application.controller.getParametersForCurrentProfile().forEach(param -> add(param));
			form.placeIn((Section) scrollPane.getViewport().getView());
			
			Support.setTimeout(100, () -> {
				toggleLoading();
			});
		});
	}
	
	public void clear() {
		((Section)scrollPane.getViewport().getView()).removeAll();
		form.clear();
		repaintFrame();
	}
	
	private void add(ParameterNode param) {
		Control control = param.getView();
		if(control != null) {
			String label = Support.getOr(param.getName(), Support.capitalize(param.getID()));
			control.setID(param.getID());
			control.setValue(param.getValue());
			control.setUnit(param.getUnit());
			form.addElement(label, control);
		}
	}
	
	private void repaintFrame() {
		Support.delay(() -> {
			Application.frame.repaint();
			Application.frame.revalidate();
		});
	}
	
	public String print() {
		
		return "<html>" + form.getWithNames().entrySet().stream()
				.map(e -> "<b>" + e.getKey().replaceFirst("\\*$", "") + ":</b> " + e.getValue())
				.collect(Collectors.joining("<br>"));
	}
}
