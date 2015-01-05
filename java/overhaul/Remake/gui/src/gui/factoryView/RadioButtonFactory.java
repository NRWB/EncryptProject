package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.JRadioButton;

public class RadioButtonFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private RadioButtonFactory() {
		
	}
	
	public static JRadioButton createDelFilesRadioButton(Controller c) {
		JRadioButton result = new JRadioButton("Delete File?");
		return result;
	}
	
	public static JRadioButton createSearchSubDirRadioButton(Controller c) {
		JRadioButton result = new JRadioButton("Search Sub-Dir.'s ?");
		return result;
	}
	
	// needs to switch between encrypt & decrypt (using icons instead of words, when possible)
	public static JRadioButton createEncryptionRadioButton(Controller c) {
		JRadioButton result = new JRadioButton();
		//result.setIcon(new ImageIcon(foo.class.getResource("/resources/locked.png")));
		// that just makes the button itself disappear, and the icon only shows up
		return result;
	}

}
