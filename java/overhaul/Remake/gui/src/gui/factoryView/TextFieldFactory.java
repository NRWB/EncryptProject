package gui.factoryView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import intermediaryControl.Controller;

public class TextFieldFactory {

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private TextFieldFactory() {
		
	}
	
	public static JTextField createTextField(Controller c) {
		JTextField result = new JTextField();
		result.setColumns(10);
		
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				if (s.contains("selectOutputButton")) {
					result.setText(c.getOutputFolder().toString());
				}
			}
		});
		
		return result;
	}
	
}
