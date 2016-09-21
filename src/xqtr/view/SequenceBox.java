package xqtr.view;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import xqtr.util.Support;

@SuppressWarnings("serial")
public class SequenceBox extends Unitable {
	
	public static final int NUMBER = 0;
	public static final int TIME = 1;
	private int type;
	
	private JSpinner spinner = new JSpinner();
	private SpinnerModel model;
	
	public SequenceBox() {
		this(NUMBER);
	}
	
	public SequenceBox(int type) {
		
		this.type = type;
		switch(type) {
		case TIME:
			
			model = new SpinnerDateModel();
			spinner.setModel(model);
			spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss.SSS"));
			setValue("00:00");
			break;
		case NUMBER:
		default:
			model = new SpinnerNumberModel();
			setValue(0.0);
			spinner.setModel(model);
		}
		
		spinner.setPreferredSize(defaultSize);
		spinner.setFont(defaultFont);
		add(spinner);
	}
	
	public void setMinimum(double min) {
		((SpinnerNumberModel) model).setMinimum(min);
	}
	
	public void setMaximum(double max) {
		((SpinnerNumberModel) model).setMaximum(max);
	}
	
	public void setStep(double step) {
		((SpinnerNumberModel) model).setStepSize(step);
	}
	
	public void setValue(Object value) {
		
		switch(type) {
		case TIME:
			model.setValue(Support.dateFromString((String) value));
			break;
		case NUMBER:
		default:
			model.setValue(value);
		}
	}
}
