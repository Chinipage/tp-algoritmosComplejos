package xqtr.util;

import java.util.HashMap;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Button extends JButton {
	
	private static HashMap<String, Integer> keyMap;
	
	public Button(String label) {
		
		Support.setTimeout(100, () -> 
			addActionListener((ActionListener) this.getTopLevelAncestor())
		);
		init(label);
	}
	
	public Button(String label, ActionListener listener) {
		
		addActionListener(listener);
		init(label);
	}
	
	private void init(String label) {
		
		determineMnemonic(label);
		setFocusable(false);
	}
	
	private void determineMnemonic(String label) {
		if(keyMap == null) initKeyMap();
		
		setText(label.replaceFirst("_", ""));
		
		int mnemonicIndex = label.indexOf("_");
		if(mnemonicIndex == -1 || mnemonicIndex == label.length() - 1) return;
		
		setMnemonic(getText().charAt(mnemonicIndex));
		setDisplayedMnemonicIndex(mnemonicIndex);
	}
	
	public void setMnemonic(char mnemonic) {
		setMnemonic(keyMap.get(Character.toString(mnemonic).toUpperCase()));
	}
	
	private void initKeyMap() {
		
		keyMap = new HashMap<>();
		keyMap.put("A", KeyEvent.VK_A);
		keyMap.put("B", KeyEvent.VK_B);
		keyMap.put("C", KeyEvent.VK_C);
		keyMap.put("D", KeyEvent.VK_D);
		keyMap.put("E", KeyEvent.VK_E);
		keyMap.put("F", KeyEvent.VK_F);
		keyMap.put("G", KeyEvent.VK_G);
		keyMap.put("H", KeyEvent.VK_H);
		keyMap.put("I", KeyEvent.VK_I);
		keyMap.put("J", KeyEvent.VK_J);
		keyMap.put("K", KeyEvent.VK_K);
		keyMap.put("L", KeyEvent.VK_L);
		keyMap.put("M", KeyEvent.VK_M);
		keyMap.put("N", KeyEvent.VK_N);
		keyMap.put("O", KeyEvent.VK_O);
		keyMap.put("P", KeyEvent.VK_P);
		keyMap.put("Q", KeyEvent.VK_Q);
		keyMap.put("R", KeyEvent.VK_R);
		keyMap.put("S", KeyEvent.VK_S);
		keyMap.put("T", KeyEvent.VK_T);
		keyMap.put("U", KeyEvent.VK_U);
		keyMap.put("V", KeyEvent.VK_V);
		keyMap.put("W", KeyEvent.VK_W);
		keyMap.put("X", KeyEvent.VK_X);
		keyMap.put("Y", KeyEvent.VK_Y);
		keyMap.put("Z", KeyEvent.VK_Z);
	}
}
