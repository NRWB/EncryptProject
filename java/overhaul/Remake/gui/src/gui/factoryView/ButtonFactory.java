package gui.factoryView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import intermediaryControl.Controller;
import gui.GUIWindow;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import backEnd.CryptoEvent;
import backEnd.IO.IOLib;
import backEnd.IO.Log;

public class ButtonFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private ButtonFactory() {
		
	}
	
	public static JButton createSelectFilesButton(JTextPane pane, Controller c) {
		JButton result = new JButton("Select");
		result.setActionCommand("selectFilesButton");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				if (s.contains("selectFilesButton")) {
					// eventually update this to a different form of:
					// Add, Remove, Clear for selecting files
					c.clearData();
				
					List<Path> picks = new ArrayList<Path>(IOLib.selectDirs(c.getLastSaved(), c.getRecursionOpt()));
				
					c.setData(picks);
					try {
						pane.getDocument().remove(0, pane.getDocument().getLength()); // removal of everything
						int count = 0;
						for (Path p : picks) {
							String line = p.toString() + System.getProperty("line.separator");
							pane.getDocument().insertString(count, line, null);
							count += line.length();
						}
					} catch (BadLocationException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		return result;
	}
	
	public static JButton createSelectOutputButton(JTextField t, Controller c) {
		JButton result = new JButton("Select");
		result.setActionCommand("selectOutputButton");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				if (s.contains("selectOutputButton")) {
					Path p = IOLib.selectDir(t, result);
					// IF P == NULL, PROMPT USER w/ J-DIALOG, TELLING THEM THEY NEED TO PICK SOMETHING FIRST
					c.setOutputFolder(p);
				}
			}
		});
		return result;
	}
	
	// maybe check if these icons fail to load?
	// ex. if they fail, then just set the button to show text instead
	public static JButton createStartButton(Controller c, JPasswordField pw, Thread threadHandle) {
		JButton result = new JButton();
		result.setIcon(new ImageIcon(GUIWindow.class.getResource("/resources/start.png")));
		result.setActionCommand("startButton");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				if (s.contains("startButton")) {
					// This will do:
					//   - 1. Check the Controller object's fields to make sure everything is valid to use
					//   - 2. Start the progressBar and start processing files
					//   - 3. Disable itself (the start button), and activate the pause and stop buttons
					if (c.getDataSize() < 1) {
						Log.write("createStartButton; need to select files to use");
					}
					CryptoEvent.run(pw, c);
					threadHandle = new Thread();
				}
			}
		});
		return result;
	}
	
	public static JButton createStopButton(Controller c) {
		JButton result = new JButton();
		result.setIcon(new ImageIcon(GUIWindow.class.getResource("/resources/stop.png")));
		result.setSelected(false); // set initially for false b/c the default state upon opening the program is "off" or inactive
		result.setActionCommand("stopButton");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				if (s.contains("stopButton")) {
					// This will do:
					//   - 1. Prompt, confirming the kill/stop request
					//   - 2. Enable the start button, disable the pause and stop buttons
				}
			}
		});
		return result;
	}
	
	public static JButton createPauseButton(Controller c) {
		JButton result = new JButton();
		result.setIcon(new ImageIcon(GUIWindow.class.getResource("/resources/pause.png")));
		result.setSelected(false); // same idea as the stopButton, this will only be active when the start button is active
		result.setActionCommand("pauseButton");
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				if (s.contains("pauseButton")) {
					// This will do:
					//   - 1. Enable the start button
					//   - 2. pause the progressBar and the processing of files (ex. putting into infinite loop until program closes or
					//   - 3. Disable itself (the pause button)
					//   - 4. leaves the stop button alone (stop could still be pressed to kill all)
				}
			}
		});
		return result;
	}

}
