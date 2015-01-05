package client;

import gui.GUIWindow;

import java.awt.EventQueue;

import javax.swing.UIManager;

import backEnd.IO.Log;

public class App {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private App() {
		
	}
	
	// http://stackoverflow.com/questions/9247089/how-can-i-clean-up-eclipse-run-configurations
	
	// later, add compression for output logs
	// later, add feature to GUI to open a compressed log file
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Log.init();
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					GUIWindow window = new GUIWindow();
					window.create();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
