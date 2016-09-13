package xqtr;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JScrollPane;

import xqtr.util.Section;
import xqtr.ctrl.Choice;
import xqtr.ctrl.FileBrowser;
import xqtr.ctrl.Range;
import xqtr.ctrl.Sequence;
import xqtr.util.Form;
import xqtr.util.RadioGroup;

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
		
		Sequence sequence = new Sequence();
		sequence.setMinimum(0.0);
		sequence.setStep(0.1);
		sequence.setUnit("min.");
		
		Range range = new Range(0, 100, 80);
		range.setUnit("%");
		
		FileBrowser audioSource = new FileBrowser();
		audioSource.setFormat("mp3 wav aac");
		audioSource.setMultiModeEnabled(true);
		
		FileBrowser imageSource = new FileBrowser();
		imageSource.setFormat("jpg jpeg png");
		
		FileBrowser videoTarget = new FileBrowser("out.mp4");
		videoTarget.setFormat("mp4 mov avi");
		videoTarget.setSaveModeEnabled(true);
		
		form.addElement("Audio source", audioSource);
		form.addElement("Image source", imageSource);
		form.addElement("Video target", videoTarget);
		form.addElement("Limit duration", sequence);
		form.addElement("Adjust volume", range);
	}
	
	private void exampleForm2() {
		
		Sequence seqFrom = new Sequence(Sequence.TIME);
		Sequence seqTo = new Sequence(Sequence.TIME);
		seqTo.setValue("00:10:00");
		
		form.addElement("input", new FileBrowser());
		form.addElement("output", new FileBrowser("trimmed-video.avi"));
		form.addElement("from", seqFrom);
		form.addElement("to", seqTo);
	}
	
	private void exampleForm3() {
		
		LinkedHashMap<String, String> model = new LinkedHashMap<>();
		model.put("Clockwise", "-1");
		model.put("Counterclockwise", "1");
		Choice choice = new Choice(model);
		
		form.addElement("input", new FileBrowser());
		form.addElement("output", new FileBrowser());
		form.addElement("direction", choice);
	}
	
	private void exampleForm4() {
		
		form.addElement("Files to merge", new FileBrowser());
		form.addElement("Combined file", new FileBrowser("merge.wav"));
	}
}
