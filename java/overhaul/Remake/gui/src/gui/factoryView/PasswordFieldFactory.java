package gui.factoryView;

import intermediaryControl.Controller;

import javax.swing.JPasswordField;

public class PasswordFieldFactory {
	
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * This class is intended for static method use only.
	 * Constructor is thereby made private for preventative measure.
	 */
	private PasswordFieldFactory() {
		
	}
	
	public static JPasswordField createPasswordField(Controller c) {
		JPasswordField passwordField = new JPasswordField();
		passwordField.setColumns(10);
		return passwordField;
	}

}
