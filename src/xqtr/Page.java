package xqtr;

import java.awt.FlowLayout;
import java.io.File;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;

import xqtr.util.Section;
import xqtr.util.Support;
import xqtr.view.ChoiceBox;
import xqtr.view.FileBrowser;
import xqtr.view.RangeBox;
import xqtr.view.SequenceBox;
import xqtr.view.TextField;
import xqtr.util.Form;

@SuppressWarnings("serial")
public class Page extends JScrollPane {

	private Form form = new Form();
	
	public Page() {
		
		setBorder(null);
		setViewportBorder(null);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		
		Section view = new Section();
		view.setLayout(new FlowLayout());
		view.setPadding(10,0,10,0);
		setViewportView(view);
		
		exampleForm1();
		
		form.placeIn(view);
	}
	
	private void exampleForm1() {
		
		SequenceBox sequence = new SequenceBox();
		sequence.setMinimum(0.0);
		sequence.setStep(0.1);
		sequence.setUnit("min.");
		
		RangeBox range = new RangeBox(0, 100, 80);
		range.setUnit("%");
		
		FileBrowser audioSource = new FileBrowser();
		audioSource.setFormat("mp3 wav aac");
		audioSource.setMultiModeEnabled(true);
		
		FileBrowser imageSource = new FileBrowser();
		imageSource.setFormat("jpg jpeg png");
		
		FileBrowser videoTarget = new FileBrowser();
		videoTarget.setFormat("mp4 mov avi");
		videoTarget.setSaveModeEnabled(true);
		videoTarget.setValue("out.mp4");
		
		form.addElement("Audio source", audioSource);
		form.addElement("Image source", imageSource);
		form.addElement("Video target", videoTarget);
		form.addElement("Limit duration", sequence);
		form.addElement("Adjust volume", range);
		form.addElement("Test text", new TextField());
	}
	
	private void exampleForm2() {
		
		SequenceBox seqFrom = new SequenceBox(SequenceBox.TIME);
		SequenceBox seqTo = new SequenceBox(SequenceBox.TIME);
		seqTo.setValue("00:10:00");
		
		form.addElement("input", new FileBrowser());
		form.addElement("output", new FileBrowser(Support.map(File::new, "trimmed-video.avi")));
		form.addElement("from", seqFrom);
		form.addElement("to", seqTo);
	}
	
	private void exampleForm3() {
		
		ChoiceBox choice = new ChoiceBox("Clockwise: -1; Counterclockwise: 1");
		choice.setValue("Counterclockwise");
		
		form.addElement("input", new FileBrowser());
		form.addElement("output", new FileBrowser());
		form.addElement("direction", choice);
	}
	
	private void exampleForm4() {
		
		form.addElement("Files to merge", new FileBrowser());
		form.addElement("Combined file", new FileBrowser(Support.map(File::new, "merge.wav")));
	}
	
	public String print() {
		
		return "<html>" + form.submit().entrySet().stream()
				.map(e -> "<b>" + e.getKey() + ":</b> " + e.getValue())
				.collect(Collectors.joining("<br>"));
	}
}
