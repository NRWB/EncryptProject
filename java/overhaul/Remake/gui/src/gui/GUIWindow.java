package gui;

import gui.factoryView.FrameFactory;
import gui.factoryView.MenuBarFactory;
import intermediaryControl.Controller;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class GUIWindow {
	
	// redo, use this:
	// https://wiki.eclipse.org/images/7/78/Eric_Clayberg_-_Buiding-Amazing-UIs-With-WindowBuilder.pdf

	// --
	// misc. links
	// http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/SimpleTableDemoProject/src/components/SimpleTableDemo.java
	// http://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
	// http://stackoverflow.com/questions/2135223/obtaining-focus-on-a-jpanel
	// http://docs.oracle.com/javase/tutorial/uiswing/misc/action.html
	// http://stackoverflow.com/questions/4606815/jframe-subclass-and-actionlistener-interface-implementation
	// http://stackoverflow.com/questions/9930679/pass-arguments-into-jbutton-actionlistener
	// http://docs.oracle.com/javase/tutorial/uiswing/events/componentlistener.html

	private JFrame frame;

	public GUIWindow() {
		initializeBase();
	}

	private void initializeBase() {
		this.frame = new JFrame();
		this.frame.setPreferredSize(new Dimension(550, 420));
		this.frame.setBounds(100, 100, 550, 420);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
	}
	
	public void create() {
		initializeLayers();
	}
	
	private void initializeLayers() {
		Controller c = new Controller(); // initially there is no settings applied from user, so no args.
		final JMenuBar menuBar = MenuBarFactory.createMenuBar(c);
		this.frame.setJMenuBar(menuBar);
		FrameFactory.createMainFrame(this.frame.getContentPane(), c); // pass the Controller object in order to update it
	}
}
