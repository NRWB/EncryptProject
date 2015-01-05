package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBarFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private MenuBarFactory() {
		
	}
	
	public static JMenuBar createMenuBar(Controller c) {
		JMenuBar result = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		result.add(fileMenu);
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		
		JMenu settingsMenu = new JMenu("Settings");
		result.add(settingsMenu);
		JMenuItem advItem = new JMenuItem("Advanced");
		settingsMenu.add(advItem);
		JMenuItem logItem = new JMenuItem("Log");
		settingsMenu.add(logItem);
		
		JMenu helpMenu = new JMenu("Help");
		result.add(helpMenu);
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);
		JMenuItem helpItem = new JMenuItem("Help");
		helpMenu.add(helpItem);
		JMenuItem devItem = new JMenuItem("Development");
		helpMenu.add(devItem);
		
		return result;
	}

}
