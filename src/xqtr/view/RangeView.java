package xqtr.view;

import javax.swing.JLabel;
import javax.swing.JSlider;

import xqtr.util.Support;

@SuppressWarnings("serial")
public class RangeView extends Control {
	
	private JSlider slider = new JSlider();
	private JLabel minLabel = new JLabel();
	private JLabel maxLabel = new JLabel();
	
	private Integer min;
	private Integer max;
	
	public RangeView() {
		this(0, 100);
	}
	
	public RangeView(int min, int max) {
		this(min, max, (max - min) / 2);
	}
	
	public RangeView(int min, int max, int value) {
		this(min, max, value, (max - min) * 1 / 10);
	}
	
	public RangeView(int min, int max, int value, int step) {
		
		this.min = min;
		this.max = max;
		
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setFont(defaultFont);
		
		setMinimum(min);
		setMaximum(max);
		setValue(value);
		setStep(step);
		
		add(minLabel);
		add(createSeparator());
		add(slider);
		add(createSeparator());
		add(maxLabel);
	}
	
	private void updateLimits() {
		
		slider.setMinimum(min);
		slider.setMaximum(max);
		slider.setMajorTickSpacing((max - min) / 2);
		minLabel.setText(Integer.toString(min));
		maxLabel.setText(Integer.toString(max));
	}
	
	public void setMinimum(Integer min) {
		if(min != null) {
			this.min = min;
			updateLimits();
		}
	}
	
	public void setMaximum(Integer max) {
		if(max != null) {
			this.max = max;
			updateLimits();
		}
	}
	
	public void setStep(Integer step) {
		if(step != null) {
			int range = slider.getMaximum() - slider.getMinimum();
			slider.setMinorTickSpacing(range * step / range);
		} else {
			setStep((max - min) * 1 / 10);
		}
	}
	
	public void setValue(String value) {
		Integer intValue = Support.integerFromString(value);
		if(intValue != null) {
			setValue(intValue);
		} else {
			setValue((max - min) / 2);
		}
	}
	
	public void setValue(Integer value) {
		slider.setValue(value);
	}
	
	public String getValue() {
		return Integer.toString(slider.getValue());
	}
}
