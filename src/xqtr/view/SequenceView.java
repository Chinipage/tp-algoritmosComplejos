package xqtr.view;

import java.util.Date;
import java.util.List;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import xqtr.util.Support;

@SuppressWarnings("serial")
public class SequenceView extends Control {
	
	public static final int NUMBER = 0;
	public static final int TIME = 1;
	private static List<String> types = Support.listFromString("number, time");
	
	private int type;
	
	private JSpinner spinner = new JSpinner();
	private SpinnerModel model;
	
	public SequenceView() {
		this(NUMBER);
	}
	
	public SequenceView(String type) {
		this(Support.keyFromValue(types, type.toLowerCase(), 0));
	}
	
	public SequenceView(int type) {
		
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
			setValue("0.0");
			spinner.setModel(model);
		}
		
		spinner.setPreferredSize(defaultSize);
		spinner.setFont(defaultFont);
		add(spinner);
	}
	
	public void setMinimum(Double min) {
		if(min == null) return;
		((SpinnerNumberModel) model).setMinimum(min);
	}
	
	public void setMaximum(Double max) {
		if(max == null) return;
		((SpinnerNumberModel) model).setMaximum(max);
	}
	
	public void setStep(Double step) {
		if(step == null) return;
		((SpinnerNumberModel) model).setStepSize(step);
	}
	
	public void setValue(String value) {
		if(value == null) return;
		switch(type) {
		case TIME:
			model.setValue(Support.dateFromString(value));
			break;
		case NUMBER:
		default:
			model.setValue(Double.parseDouble(value));
		}
	}
	
	public String getValue() {
		switch(type) {
		case TIME:
			Date date = (Date) model.getValue();
			return Support.stringFromDate(date, "HH:mm:ss.SSS");
		case NUMBER:
		default:
			SpinnerNumberModel numberModel = (SpinnerNumberModel) model;
			Object number = numberModel.getValue();
			if(numberModel.getStepSize() instanceof Integer) {
				number = new Integer(((Double) number).intValue());
			}
			return number.toString();
		}
	}
}
