package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class SplitPaneFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private SplitPaneFactory() {
		
	}
	
	public static JSplitPane createSplitPane(Controller c) {
		JSplitPane result = new JSplitPane();
		result.setResizeWeight(0.5);
		final JPanel panelA = PanelFactory.createFileListingPanel(c);
		final JPanel panelB = PanelFactory.createBasicSettingsPanel(c);
		result.setLeftComponent(panelA);
		result.setRightComponent(panelB);
		return result;
	}

}
