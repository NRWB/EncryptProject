package gui.factoryView;

import javax.swing.JLabel;

public final class LabelFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private LabelFactory() {
		
	}
	
	public static JLabel createFilesLabel() {
		JLabel label = new JLabel("Files");
		return label;
	}
	
	public static JLabel createOutputDirLabel() {
		JLabel label = new JLabel("Output Dir.");
		return label;
	}
	
	public static JLabel createPasswordLabel() {
		JLabel label = new JLabel("Password");
		return label;
	}
	
	public static JLabel createProgressLabel() {
		JLabel label = new JLabel("Progress:");
		return label;
	}
	
}