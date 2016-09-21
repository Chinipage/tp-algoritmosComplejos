package xqtr.view;

import javax.swing.JLabel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class RangeBox extends Unitable {
	
	JSlider slider;
	
	public RangeBox(int min, int max) {
		this(min, max, (max - min) / 2);
	}
	
	public RangeBox(int min, int max, int value) {
		this(min, max, value, (max - min) * 1 / 10);
	}
	
	public RangeBox(int min, int max, int value, int step) {
		
		slider = new JSlider(min, max);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setMajorTickSpacing((max - min) / 2);
		slider.setFont(defaultFont);
		setStep(step);
		setValue(value);
		
		add(new JLabel(Integer.toString(min)));
		add(createSeparator());
		add(slider);
		add(createSeparator());
		add(new JLabel(Integer.toString(max)));
	}
	
	public void setMinimum(int min) {
		slider.setMinimum(min);
	}
	
	public void setMaximum(int max) {
		slider.setMaximum(max);
	}
	
	public void setStep(int step) {
		int range = slider.getMaximum() - slider.getMinimum();
		slider.setMinorTickSpacing(range * step / range);
	}
	
	public void setValue(int value) {
		slider.setValue(value);
	}
}
