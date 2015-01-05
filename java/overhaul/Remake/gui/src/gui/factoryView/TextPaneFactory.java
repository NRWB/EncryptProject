package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.JTextPane;

public class TextPaneFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private TextPaneFactory() {
		
	}
	
	public static JTextPane createFileList(Controller c) {
		JTextPane result = new JTextPane();
		result.setEditable(false);
		// selectFilesButton

		return result;
	}

}
