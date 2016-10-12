package xqtr;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import xqtr.util.Section;
import xqtr.util.Support;
import xqtr.view.AbstractControl;
import xqtr.view.ChoiceView;
import xqtr.view.FileView;
import xqtr.view.RangeView;
import xqtr.view.SequenceView;
import xqtr.view.TextView;
import xqtr.model.FileNode;
import xqtr.model.ParameterNode;
import xqtr.model.SequenceNode;
import xqtr.util.Form;

@SuppressWarnings("serial")
public class Page extends Section {
	
	private JScrollPane scrollPane;
	private JLabel loadingLabel;
	private boolean isLoading;
	
	private Form form;
	
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
		loadingLabel.setIcon(new ImageIcon("Spinner.gif"));
		
		toggleLoading();
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
			System.out.println(Application.controller.getParametersForCurrentProfile().size());
			Application.controller.getParametersForCurrentProfile().forEach(param -> add(param));
			form.placeIn((Section) scrollPane.getViewport().getView());
			
			Support.setTimeout(100, () -> {
				toggleLoading();
				if(Application.controller.hasCurrentProfile()) {
					Application.frame.runButton.setEnabled(true);
					Application.frame.menu.getItem("Execute").setEnabled(true);
				}
			});
		});
	}
	
	public void clear() {
		((Section)scrollPane.getViewport().getView()).removeAll();
		form = new Form();
		repaintFrame();
	}
	
	private void add(ParameterNode param) {
		
		AbstractControl control = null;
		if(param instanceof FileNode) {
			FileNode fileModel = (FileNode) param;
			FileView fileView = new FileView();
			fileView.setFormat(fileModel.getFormat());
			fileView.setSaveModeEnabled(fileModel.hasClass("save"));
			fileView.setMultiModeEnabled(fileModel.hasClass("multiple"));			
			control = fileView;
		} else if(param instanceof SequenceNode) {
			SequenceNode sequenceModel = (SequenceNode) param;
			
		}
		
		if(control != null) {
			String label = Support.getOr(param.getName(), Support.capitalize(param.getID()));
			control.setValue(param.getValue());
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

		return "<html>" + form.submit().entrySet().stream()
				.map(e -> "<b>" + e.getKey().replaceFirst("\\*$", "") + ":</b> " + e.getValue())
				.collect(Collectors.joining("<br>"));
	}
	
	private void exampleForm1() {
		
		SequenceView sequence = new SequenceView();
		sequence.setMinimum(0.0);
		sequence.setStep(0.1);
		sequence.setUnit("min.");
		
		RangeView range = new RangeView(0, 100, 80);
		range.setUnit("%");
		
		FileView audioSource = new FileView();
		audioSource.setFormat("mp3 wav aac");
		audioSource.setMultiModeEnabled(true);
		
		FileView imageSource = new FileView();
		imageSource.setFormat("jpg jpeg png");
		
		FileView videoTarget = new FileView();
		videoTarget.setFormat("mp4 mov avi");
		videoTarget.setSaveModeEnabled(true);
		videoTarget.setValue("out.mp4");
		
		TextView textField = new TextView();
		
		form.addElement("Audio source", audioSource);
		form.addElement("Image source", imageSource);
		form.addElement("Video target", videoTarget);
		form.addElement("Limit duration", sequence);
		form.addElement("Adjust volume", range);
		form.addElement("Test text", textField);
	}
	
	private void exampleForm2() {
		
		SequenceView seqFrom = new SequenceView(SequenceView.TIME);
		SequenceView seqTo = new SequenceView(SequenceView.TIME);
		seqTo.setValue("00:10:00");
		
		form.addElement("input", new FileView());
		form.addElement("output", new FileView(Support.map(File::new, "trimmed-video.avi")));
		form.addElement("from", seqFrom);
		form.addElement("to", seqTo);
	}
	
	private void exampleForm3() {
		
		ChoiceView choice = new ChoiceView("Clockwise: -1; Counterclockwise: 1");
		choice.setValue("Counterclockwise");
		
		form.addElement("input", new FileView());
		form.addElement("output", new FileView());
		form.addElement("direction", choice);
	}
	
	private void exampleForm4() {
		
		form.addElement("Files to merge", new FileView());
		form.addElement("Combined file", new FileView(Support.map(File::new, "merge.wav")));
	}
}
