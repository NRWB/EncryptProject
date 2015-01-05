package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.JProgressBar;

public class ProgressBarFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private ProgressBarFactory() {
		
	}
	
	public static JProgressBar createProgressBar(Controller c) {
		JProgressBar result = new JProgressBar();
		
		return result;
	}

}
